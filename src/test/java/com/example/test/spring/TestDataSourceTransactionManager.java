package com.example.test.spring;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class TestDataSourceTransactionManager {
	
	public static void main(String[] args) throws SQLException {
		String url = "jdbc:mysql://23.83.247.18:3306/xcuber?autoCommit=true&useUnicode=true&autoReconnect=true&characterEncoding=utf-8";
		String user = "root";
		String password = "junyan2016";
		DataSource dataSource = new SimpleDriverDataSource(DriverManager.getDriver(url), url, user, password);
		JdbcTemplate template = new JdbcTemplate(dataSource);
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
		TransactionDefinition td = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(td);
		
		try {
			template.execute("insert into tb_student(id, name, phone) values ('4', 'test', '18012341236');");
			int i = 1 / 0;
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
		}
	}
}
