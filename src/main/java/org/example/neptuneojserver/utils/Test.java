package org.example.neptuneojserver.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;


class A {
    private int a;
    private int b;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
}

public class Test {
    public static void main(String[] args) {
        List<A> x = Arrays.asList(
                new A() {{ setA(1); setB(1); }},
                new A() {{ setA(2); setB(2); }}
        );
        List<String> names = Arrays.asList("a", "eddd");

        List<Map<String, Object>> y = x.stream().map(obj -> {
            return names.stream().collect(Collectors.toMap(
                    name -> name,
                    name -> {
                        try {
                            Field field = A.class.getDeclaredField(name);
                            field.setAccessible(true);
                            if(field.get(obj).equals(1)) {
                                return "One";
                            }
                            return field.get(obj);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            System.err.println("Error accessing field " + name + " for object " + obj.getClass().getSimpleName() + ": " + e.getMessage());
                            return "Error";
                        }
                    }
            ));
        }).collect(Collectors.toList());

        System.out.println(y);
    }
}

