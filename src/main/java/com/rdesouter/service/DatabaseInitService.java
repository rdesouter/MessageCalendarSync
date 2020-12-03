//package com.rdesouter.service;
//
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//@Component
//public class DatabaseInitService {
//
//    private final HikariDataSource hikariDataSource;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    public DatabaseInitService(HikariDataSource hikariDataSource, BCryptPasswordEncoder bCryptPasswordEncoder) {
//        this.hikariDataSource = hikariDataSource;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//        init();
//    }
//
//    private void init(){
//        try(
//                Connection connection = hikariDataSource.getConnection();
//                Statement statement = connection.createStatement();
//                PreparedStatement preparedStatement = connection.prepareStatement("" +
//                        "INSERT INTO public.user (name, password) VALUES(?,?)")
//                ){
//                statement.execute("DROP TABLE IF EXISTS public.user, public.role, public.user_role; " +
//                        "CREATE TABLE public.user (" +
//                        "id serial PRIMARY KEY, " +
//                        "name VARCHAR (255)  NOT NULL, " +
//                        "password VARCHAR (255) NOT NULL); " +
//                        "CREATE TABLE public.role (" +
//                        "id serial PRIMARY KEY, " +
//                        "name VARCHAR (50) NOT NULL);" +
//                        "CREATE TABLE public.user_role (" +
//                        "user_id INTEGER," +
//                        "role_id INTEGER, " +
//                        "PRIMARY KEY(" +
//                        "user_id, role_id)); " +
//                        "INSERT INTO public.role (name) VALUES ('guest');" +
//                        "INSERT INTO user_role (user_id, role_id) VALUES (1, 1)");
//                preparedStatement.setString(1,"example");
//                preparedStatement.setString(2, bCryptPasswordEncoder.encode("lalala"));
//                preparedStatement.executeUpdate();
//        } catch (SQLException throwables) {
//            throw new RuntimeException(throwables);
//        }
//    }
//}
