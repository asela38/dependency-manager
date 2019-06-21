package com.asela.object.relational.bean;

import com.asela.object.relational.anno.Inject;
import com.asela.object.relational.anno.Provides;
import com.asela.object.relational.provider.H2ConnectionProvider;
import com.sun.tools.javac.util.List;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BeanManager {
    private static BeanManager instances = new BeanManager();

    public static BeanManager getInstance() {

        return instances;
    }

    private Map<Class<?>, Supplier<?>> registry = new HashMap<>();

    private BeanManager() {
        List<Class<?>> classes = List.of(H2ConnectionProvider.class);
        for (Class<?> aClass : classes) {
            for (Method aMethod : aClass.getDeclaredMethods()) {
                Provides aProvider = aMethod.getAnnotation(Provides.class);
                if (aProvider != null) {
                    Class<?> returnType = aMethod.getReturnType();
                    Supplier<?> supplier = () -> {
                        try {
                            if (Modifier.isStatic(aMethod.getModifiers())) {
                                Object object = aClass.getConstructor().newInstance();
                                return aMethod.invoke(object);
                            } else
                                return aMethod.invoke(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    };

                    registry.put(aClass, supplier);
                }
            }
        }
    }

    public <T, U> T getInstance(Class<T> aClass, Class<U> type) {

        try {
            T t = aClass.getConstructor().newInstance(type);
            for (Field field : aClass.getDeclaredFields()) {
                Inject inject = field.getAnnotation(Inject.class);
                if (inject != null) {
                    Object object = registry.get(field.getType()).get();
                    field.setAccessible(true);
                    field.set(t, object);
                }
            }
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
