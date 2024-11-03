package com.jetbrains.internship2024;

public class ClassA {
    public static void main(String[] args) throws Exception {
        while (System.currentTimeMillis() > 1) {
            System.out.println("Still waiting forever");
            Thread.sleep(1000L);
        }
        new InternalClassA().doInternal();
    }

    public void sayHello() {
        System.out.println("Hi");
    }
}
