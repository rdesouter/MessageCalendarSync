package com.rdesouter.dao;

import com.rdesouter.model.AuthenticatedUser;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class AuthenticatedUserDao {

    private HikariDataSource hikariDataSource;

    public AuthenticatedUserDao(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    public void insert(com.rdesouter.model.AuthenticatedUser authenticatedUser){
        try(
                Connection connection = hikariDataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("" +
                        "INSERT INTO public.person (name, email, refresh_token) VALUES(?,?,?)")
                ){
            preparedStatement.setString(1, authenticatedUser.name);
            preparedStatement.setString(2, authenticatedUser.email);
            preparedStatement.setString(3, authenticatedUser.refreshToken);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
}
