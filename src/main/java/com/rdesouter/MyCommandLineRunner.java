//package com.rdesouter;
//
//import com.rdesouter.config.AppConfiguration;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MyCommandLineRunner implements CommandLineRunner {
//
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//    private final AppConfiguration appConfiguration;
//
//
//
//    public MyCommandLineRunner(BCryptPasswordEncoder bCryptPasswordEncoder, AppConfiguration appConfiguration) {
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//        this.appConfiguration = appConfiguration;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("commandline runner...");
//        String hashedPassword = bCryptPasswordEncoder.encode("lalala");
//        System.out.println("hash of string 'lalala': " + hashedPassword);
//    }
//}
