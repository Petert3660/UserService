package com.ptconsultancy;

import com.ptconsultancy.admin.adminsupport.BuildVersion;
import com.ptconsultancy.messages.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.Environment;

/**
 * Created by Peter Thomson on 13/04/2018.
 */
@SuppressWarnings("ALL")
@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static final String PROPS_FILENAME = "application";
    private static final String SERVER_HOST = "spring.data.rest.base-path";
    private static final String SERVER_PORT_PROPERTY = "server.port";

    private static final int EXIT_STATUS = 0;

    @Autowired
    private Environment env;

    @Autowired
    MessageHandler messageHandler;

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
            .headless(false)
            .run(args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println(messageHandler.getMessage("messages.ptconsultancy.messages"));
        outputMessage();
        populateDatabase();
    }

    private void outputMessage() {
        String hostname = env.getProperty(SERVER_HOST) + ":";
        String serverPort = env.getProperty(SERVER_PORT_PROPERTY);
        System.out.println("************************************************************************");
        if (BuildVersion.getProjectTitle() != null && BuildVersion.getBuildVersion() != null) {
            System.out.println("* " + BuildVersion.getProjectTitle() + ", Version: " + BuildVersion.getBuildVersion());
            System.out.println("************************************************************************");
        }
        if (BuildVersion.getProjectTitle() != null) {
            System.out.println("* " + BuildVersion.getProjectTitle() + " is now running on:- " + hostname + serverPort);
        } else {
            System.out.println("* This application is now running on:- " + hostname + serverPort);
        }
        System.out.println("************************************************************************");
    }

    // Use this method to set up test data from the application.properties file
    private void populateDatabase() {

//        if (customerRepository.findCustomerById((long) 11111).size() == 0) {
//            String prop;
//            int i = 1;
//            do {
//                String address = "customer" + String.valueOf(i++);
//                prop = env.getProperty(address);
//                if (!StringUtils.isEmpty(prop)) {
//                    String[] custDetails = prop.split(", ");
//                    customerRepository.save(new Customer(Long.parseLong(custDetails[0]), custDetails[1], custDetails[2], new Address()));
//                }
//            } while (!StringUtils.isEmpty(prop));
//        }
    }
}
