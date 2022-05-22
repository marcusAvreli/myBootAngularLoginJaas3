
package myBootAngularLoginJaas3;

import java.util.Arrays;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


import myBootAngularLoginJaas3.config.DatabaseConfig;
import myBootAngularLoginJaas3.config.SecurityConfig;



/**
 * Hello world!
 *
 */
//@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "myBootAngularLoginJaas3" })
public class App extends SpringBootServletInitializer {

	protected static Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		logger.info("Hello World!");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		logger.info("***STARTING_APPLICATION_MY_BOOT_ANGULAR_LOGIN_JAAS_3***");
		System.setProperty("java.security.auth.login.config", "./src/main/resources/jaas.config");

		return application.sources(new Class[] {App.class, SecurityConfig.class, DatabaseConfig.class });
	}


	// Get all loaded beans with Class Type Information
	@Autowired
	private ApplicationContext appContext;

	public void run(String... args) throws Exception {

		String[] beans = appContext.getBeanDefinitionNames();
		Arrays.sort(beans);
		for (String bean : beans) {
			logger.info(bean + " of Type :: " + appContext.getBean(bean).getClass());
		}
	}
}
