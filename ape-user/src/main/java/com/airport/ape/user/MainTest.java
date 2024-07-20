package com.airport.ape.user;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainTest {
    public static void main(String[] args) {
        AAA.BBB bbb = new AAA.BBB();
        AAA aaa = new AAA();
        AAA.CCC ccc = aaa.new CCC();
        try {
            throw new RuntimeException("aaaaa");
        }catch (RuntimeException e){
            log.info("log: {}","test",e.getMessage());
        }
    }
}
