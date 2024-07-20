package com.airport.ape.user.test;

public interface InterfaceB {
    public static final int a = 0;
    public abstract void MethodA();
    public  default void MethodB(){
        System.out.println("default method B");
    }
    public static void MethodC(){
        System.out.println("public interface InterfaceB static method C");
    }
}
