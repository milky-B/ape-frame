package com.airport.ape.user.test;

public class Test {
    public static void main(String[] args) {
        SubClassB subClassB = new SubClassB();
        subClassB.setAaa("aaa");
        System.out.println(subClassB.getAaa());
        ImplClassC implClassC = new ImplClassC();
        implClassC.MethodD();
        implClassC.MethodE();
        implClassC.MethodQ();

        System.out.println("**************");
        implClassC.MethodB();
    }
}
