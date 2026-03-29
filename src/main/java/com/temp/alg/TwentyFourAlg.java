package com.temp.alg;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class TwentyFourAlg implements Cloneable {

    private String name;

	public static void main(String[] args) {
        TwentyFourAlg twentyFourAlg = new TwentyFourAlg();
        twentyFourAlg.setName("1111");
        TwentyFourAlg clone = twentyFourAlg.clone();
        System.out.println(clone.getName());

    }
	
	private static void func(int... num) {
		if(num.length == 1) {
			if(num[0] != 24) {
				return;
			}
		}
		for(int i = 0; i < num.length; i++) {
			for(int j = 0; j < num.length; j++) {
				if(i != j) {
					
				}
			}
		}
	}
	
	public void say() {
		System.out.println(StringUtils.left("hello,world", 6));
	}


    @Override
    public TwentyFourAlg clone() {
        try {
            return (TwentyFourAlg) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
