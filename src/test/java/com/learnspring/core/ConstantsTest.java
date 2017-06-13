package com.learnspring.core;

import org.springframework.core.Constants;
import org.springframework.transaction.TransactionDefinition;

public class ConstantsTest {

	public static void main(String[] args) {
		Constants constants = new Constants(TransactionDefinition.class);
		System.out.println(constants.getClassName());
		System.out.println(constants.getSize());
		System.out.println(constants.asString("TIMEOUT_DEFAULT"));
		System.out.println(constants.getNames("PROP"));
		System.out.println(constants.getNamesForProperty("timeoutDefault"));
		System.out.println(constants.getNamesForSuffix("DEFAULT"));
		System.out.println(constants.getValues("PROP"));
		System.out.println(constants.toCode(2, "PROP"));
	}
}
