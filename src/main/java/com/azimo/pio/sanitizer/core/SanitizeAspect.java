package com.azimo.pio.sanitizer.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Aspect
public class SanitizeAspect {

    private static final Logger log = LoggerFactory.getLogger(SanitizeAspect.class);

    @Around("execution(* toString()) && @within(com.azimo.pio.sanitizer.core.Sanitized)")
    public Object sanitizeMethod(ProceedingJoinPoint pjp) throws Throwable {
        try {
            Set<Field> fields = findAnnotatedFields(pjp.getStaticPart().getSignature().getDeclaringType(), Sanitize.class);
            List<PropertyDescriptor> propertyDescriptors = collectPropertyDescriptors(pjp, fields);

            Map<String, String> previousValues = collectPreviousValues(pjp, propertyDescriptors);
            setSanitizedValues(pjp, propertyDescriptors, previousValues);

            Object proceed = pjp.proceed();

            restorePreviousValues(pjp, propertyDescriptors, previousValues);
            return proceed;
        } catch (Exception ex) {
            log.debug("Exception while ProceedingJoinPoint sanitizeMethod()", ex);
            throw ex;
        }
    }

    private List<PropertyDescriptor> collectPropertyDescriptors(final ProceedingJoinPoint pjp, final Set<Field> fields) {
        return fields.stream()
                .filter(field -> field.getType().equals(String.class))
                .map(field -> buildPropertyDescriptor(field.getName(), pjp.getTarget().getClass()))
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private void restorePreviousValues(final ProceedingJoinPoint pjp, final List<PropertyDescriptor> propertyDescriptors, final Map<String, String> previousValues) {
        propertyDescriptors.stream()
                .forEach(propertyDescriptor -> callFieldSetter(previousValues.get(propertyDescriptor.getName()), propertyDescriptor, pjp));
    }

    private void setSanitizedValues(final ProceedingJoinPoint pjp, final List<PropertyDescriptor> propertyDescriptors, final Map<String, String> previousValues) {
        propertyDescriptors.stream()
                .forEach(propertyDescriptor -> callFieldSetter(sanitizeValue(previousValues.get(propertyDescriptor.getName())), propertyDescriptor, pjp));
    }

    private Map<String, String> collectPreviousValues(final ProceedingJoinPoint pjp, final List<PropertyDescriptor> propertyDescriptors) {
        return propertyDescriptors.stream()
                .collect(
                        toMap(propertyDescriptor -> propertyDescriptor.getName(), propertyDescriptor -> callFieldGetter(pjp, propertyDescriptor)));
    }

    private String sanitizeValue(final String value) {
        return value.replaceAll(".", "*");
    }

    private void callFieldSetter(final String value, final PropertyDescriptor propertyDescriptor, final ProceedingJoinPoint pjp) {
        try {
            propertyDescriptor.getWriteMethod().invoke(pjp.getTarget(), value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new SanitizationException(e);
        }
    }

    private String callFieldGetter(final ProceedingJoinPoint pjp, final PropertyDescriptor propertyDescriptor) {
        try {
            return (String)propertyDescriptor.getReadMethod().invoke(pjp.getTarget());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new SanitizationException(e);
        }
    }

    private PropertyDescriptor buildPropertyDescriptor(String fieldName, Class clazz) {
        try {
            return new PropertyDescriptor(fieldName, clazz);
        } catch (IntrospectionException | IllegalArgumentException e) {
            throw new SanitizationException(e);
        }
    }

    public static Set<Field> findAnnotatedFields(Class<?> classs, Class<? extends Annotation> ann) {
        Set<Field> set = new HashSet<>();
        Class<?> c = classs;
        while (c != null) {
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(ann)) {
                    set.add(field);
                }
            }
            c = c.getSuperclass();
        }
        return set;
    }
}