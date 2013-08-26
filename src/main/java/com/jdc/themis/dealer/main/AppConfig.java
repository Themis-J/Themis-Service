package com.jdc.themis.dealer.main;

import java.util.Arrays;

import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.ImportResource;
import com.jdc.themis.dealer.service.rest.DealerIncomeSystemRestService;

@Configuration
@ImportResource({"classpath:META-INF/database-config.xml"})
public class AppConfig {
	@Bean(destroyMethod = "shutdown")
	public SpringBus cxf() {
		return new SpringBus();
	}

	@Bean @DependsOn("cxf")
	public Server jaxRsServer() {
		final JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance()
				.createEndpoint(jaxRsApiApplication(),
						JAXRSServerFactoryBean.class);
		factory.setServiceBeans(Arrays.<Object>asList(dealerRestService()));
		factory.setAddress(factory.getAddress());
		factory.setProviders( Arrays.<Object>asList( jsonProvider() ) );
		return factory.create();
	}

	@Bean
	public JaxRsApiApplication jaxRsApiApplication() {
		return new JaxRsApiApplication();
	}

	@Bean
	public DealerIncomeSystemRestService dealerRestService() {
		return new DealerIncomeSystemRestService();
	}
		
	@Bean
	public JacksonJsonProvider jsonProvider() {
		return new JacksonJsonProvider();
	}
	
}