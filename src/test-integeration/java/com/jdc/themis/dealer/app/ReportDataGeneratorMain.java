package com.jdc.themis.dealer.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ReportDataGeneratorMain {

	public static void main(String[] args) {
		//final ApplicationContext context = new ClassPathXmlApplicationContext("classpath:test-integration-context.xml");
        final ApplicationContext context = new ClassPathXmlApplicationContext("classpath:test-integration-context-qa.xml");
        final ReportDataGenerator client = (ReportDataGenerator) context.getBean("reportDataGenerator");
	
        client.generateRevenues();
        client.generateExpenses();
	    
	    System.exit(0);
	}

}
