package com.azimo.tukan.log.sanitizer.core;

import com.azimo.tukan.log.structure.AzimoExtendedLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Aspect
public class SanitizeAspect {

    private static final AzimoExtendedLogger log = AzimoExtendedLogger.create(SanitizeAspect.class);

    @Around("execution(* toString()) && @within(com.azimo.tukan.log.sanitizer.core.Sanitized)")
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
                .collect(toList());
    }

    private void restorePreviousValues(final ProceedingJoinPoint pjp, final List<PropertyDescriptor> propertyDescriptors, final Map<String, String> previousValues) {
        propertyDescriptors
                .forEach(propertyDescriptor -> callFieldSetter(previousValues.get(propertyDescriptor.getName()), propertyDescriptor, pjp));
    }

    private void setSanitizedValues(final ProceedingJoinPoint pjp, final List<PropertyDescriptor> propertyDescriptors, final Map<String, String> previousValues) {
        propertyDescriptors
                .forEach(propertyDescriptor -> callFieldSetter(SanitizeUtils.sanitizeValue(previousValues.get(propertyDescriptor.getName())), propertyDescriptor, pjp));
    }

    private Map<String, String> collectPreviousValues(final ProceedingJoinPoint pjp, final List<PropertyDescriptor> propertyDescriptors) {
        return propertyDescriptors.stream()
                .map(entry -> Tuple2.of(entry.getName(), callFieldGetter(pjp, entry)))
                .filter(pair -> pair._2 != null)
                .collect(
                        toMap(pair -> pair._1, pair -> pair._2));
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
            Object invoke = propertyDescriptor.getReadMethod().invoke(pjp.getTarget());
            return invoke != null ? (String) invoke : null;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new SanitizationException(e);
        }
    }

    private PropertyDescriptor buildPropertyDescriptor(String fieldName, Class<?> clazz) {
        try {
            return new PropertyDescriptor(fieldName, clazz);
        } catch (IntrospectionException | IllegalArgumentException e) {
            throw new SanitizationException(e);
        }
    }

    public static Set<Field> findAnnotatedFields(Class<?> clazz, Class<? extends Annotation> ann) {
        Set<Field> set = new HashSet<>();
        Class<?> c = clazz;
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

    private static class Tuple2<VALUE1, VALUE2> {

        private static final long serialVersionUID = 1L;

        public final VALUE1 _1;

        public final VALUE2 _2;

        public Tuple2(VALUE1 t1, VALUE2 t2) {
            this._1 = t1;
            this._2 = t2;
        }

        static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
            return new Tuple2<>(t1, t2);
        }

        public VALUE1 _1() {
            return _1;
        }

        public VALUE2 _2() {
            return _2;
        }
    }
}