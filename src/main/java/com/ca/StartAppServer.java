package com.ca;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author barna10
 * @description This is the main entry of the application used to start the application server
 * @HowTos
 * <ol>
 * 	<li>Changing the server port:<BR> 
 * 		The server start on port 8080; to run the application on different port use this JVM argument: server.port<BR>
 * 		e.g. <BR>
 * 		-Dserver.port=8888
 * 	</li>
 * 	<li>Changing the console/log banner:<BR> 
 * 		To change the banner put a banner.txt or banner.png/jpg in the source folder or on the classpath e.g. \src\main\java\banner.JPG
 * 	</li>
 * </ol>
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class StartAppServer {

	private static Log logger = LogFactory.getLog(StartAppServer.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(StartAppServer.class, args);
	}

	@Bean
	protected ServletContextListener listener() {
		return new ServletContextListener() {
			@Override
			public void contextInitialized(ServletContextEvent sce) {
				logger.info("ServletContext initialized");
			}

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				logger.info("ServletContext destroyed");
			}
		};
	}

}
