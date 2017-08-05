package com.example.test.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.temp.service.TestService;

public class TestSpringBean {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		PlatformTransactionManager transactionManager = (PlatformTransactionManager) applicationContext.getBean("transactionManager");
		System.out.println(transactionManager.hashCode());
		TransactionDefinition td = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(td);
		TestService service = (TestService) applicationContext.getBean("testServiceImpl");
		try {
			service.insertDB();
			int i = 1 / 0;
			System.out.println(i);
		}catch (Exception e) {
			transactionManager.rollback(status);
			throw e;
		}
		transactionManager.commit(status);
	}
}
