package com.airport.ape.user.test;

public class SubClassB extends SuperClassA{
    private String bbb;

    public String getBbb() {
        return bbb;
    }

    public void setBbb(String bbb) {
        this.bbb = bbb;
    }

    public void doSome(){
        super.doSome();
    }

    public String doSome(String a) {
        return "aaa";
    }


    public Integer doSome(int a){
        return 1;
    }
}
