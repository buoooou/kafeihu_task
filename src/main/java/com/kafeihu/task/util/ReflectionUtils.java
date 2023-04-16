package com.kafeihu.task.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * date:  2022/8/15 17:03
 * 反射util
 *
 * @author kroulzhang
 */
public class ReflectionUtils {

    private ReflectionUtils() {
    }

    /***
     * 根据查找class中的method集合
     * @param clazz
     * @param annotationType
     * @return
     */
    public static final Set<Method> findMethod(Class clazz, Class<? extends Annotation> annotationType) {

        Method[] declaredMethods = org.springframework.util.ReflectionUtils.getAllDeclaredMethods(clazz);
        Set<Method> results = new HashSet<>();

        for (Method curMethod : declaredMethods) {
            Annotation annotation = AnnotationUtils.findAnnotation(curMethod, annotationType);

            if (annotation != null) {
                results.add(curMethod);
            }
        }

        return results;
    }
}
