package com.airport.ape.user.test;

public interface InterfaceA {
    public static final int a = 0;
    public abstract void MethodA();
    public  default void MethodB(){
        System.out.println("default method B");
    }
    public  default void MethodD(){
        System.out.println("default method D");
    }
    public  default void MethodE(){
        System.out.println("default method E");
    }
    public static void MethodC(){
        System.out.println("public interface InterfaceA static method C");
    }

    public default void MethodQ(){
        System.out.println("public interface InterfaceA MethodQ");
    }
}
