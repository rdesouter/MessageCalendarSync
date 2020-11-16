package com.rdesouter.dao;

import com.rdesouter.model.Person;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class PersonDao {

    private final HikariDataSource hikariDataSource;


    public PersonDao(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    public void insert(Person person){
        try(
                Connection connection = hikariDataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("" +
                        "INSERT INTO public.person (name, email, refresh_token) VALUES(?,?,?)")
                ){
            preparedStatement.setString(1, person.name);
            preparedStatement.setString(2, person.email);
            preparedStatement.setString(3, person.refreshToken);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
}
