package com.jdc.themis.dealer.app;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ReportDataGeneratorMain {

	public static void main(String[] args) {
		final ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:test-integration-context.xml");
		// final ApplicationContext context = new
		// ClassPathXmlApplicationContext("classpath:test-integration-context-qa.xml");
		final ReportDataGenerator client = (ReportDataGenerator) context
				.getBean("reportDataGenerator");

		client.generateRevenues();
		client.generateExpenses();

		testDealerEntryItemStatus(client);
		System.exit(0);
	}

	private static void testDealerEntryItemStatus(final ReportDataGenerator client) {
		final Executor executor1 = Executors.newSingleThreadExecutor();
		final Executor executor2 = Executors.newSingleThreadExecutor();

		final CountDownLatch latch = new CountDownLatch(2);
		final CountDownLatch latch2 = new CountDownLatch(1);

		executor1.execute(new Runnable() {

			@Override
			public void run() {
				try {
					try {
						latch2.await();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					for (int i = 0; i <= 10; i++) {
						try {

							client.generateEntryItemStatus();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} finally {

					latch.countDown();
				}
			}

		});

		executor2.execute(new Runnable() {

			@Override
			public void run() {
				try {
					try {
						latch2.await();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					for (int i = 0; i <= 10; i++) {
						
						try {

							client.generateEntryItemStatus();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} finally {

					latch.countDown();
				}
			}

		});
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		latch2.countDown();
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
