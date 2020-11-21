package com.rdesouter.service;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseInitService {

    private final HikariDataSource hikariDataSource;

    public DatabaseInitService(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
        init();
    }

    private void init(){
        try(
                Connection connection = hikariDataSource.getConnection();
                Statement statement = connection.createStatement()
                ){
                statement.execute("CREATE TABLE public.hogelalala ();");
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
}
