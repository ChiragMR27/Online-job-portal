package com.jobportal.onlinejobportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlineJobPortalApplication {

	public static void main(String[] args) {
	    System.setProperty("jdk.tls.client.protocols", "TLSv1.2"); // force TLSv1.2 for MongoDB Atlas
	    SpringApplication.run(OnlineJobPortalApplication.class, args);
	}

}
