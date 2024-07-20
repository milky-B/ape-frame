package com.airport.ape.user.test;

public class ImplClassC extends AbstractD implements InterfaceA,InterfaceB{
    @Override
    public void MethodA() {
        InterfaceA.MethodC();
        MethodD();
    }

    @Override
    public void MethodB() {
        /*InterfaceB.MethodC();
        InterfaceA.MethodC();
        AbstractD.MethodC();
        MethodC();*/
        
    }

}
