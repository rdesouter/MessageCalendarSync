package com.rdesouter.dao;

import com.rdesouter.model.Role;
import com.rdesouter.model.UserWithRoles;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class UserDao {

    private final HikariDataSource hikariDataSource;

    public UserDao(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    public UserWithRoles findByName(String userName){
        try(
                Connection connection = hikariDataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("" +
                        "SELECT u.id, u.name, u.password, r.id, r.name FROM " +
                        "public.user u " +
                        "LEFT JOIN user_role ur ON ur.user_id = u.id " +
                        "LEFT JOIN role r ON r.id = ur.role_id WHERE u.name = ?")
                ){
            preparedStatement.setString(1, userName);
            try(ResultSet rs = preparedStatement.executeQuery()){
                boolean isFirst = true;
                UserWithRoles userWithRoles = null;
                while (rs.next()){
                    if(isFirst){
                        userWithRoles = new UserWithRoles(
                                rs.getLong(1),
                                rs.getString(2),
                                new ArrayList<>(),
                                rs.getString(3)
                        );
                    }
                    if(rs.getObject(4) != null){
                        userWithRoles.roles.add(new Role(
                                rs.getLong(4),
                                rs.getString(5)
                        ));
                    }
                    isFirst = false;
                }
                return userWithRoles;
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
}
