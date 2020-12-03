package com.rdesouter.dao;

import com.rdesouter.model.CandidateAuthenticatedUser;
//import com.rdesouter.model.Role;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class CandidateAuthenticatedUserDao {

    private final HikariDataSource hikariDataSource;

    public CandidateAuthenticatedUserDao(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    public CandidateAuthenticatedUser findByName(String userName){
        try(
                Connection connection = hikariDataSource.getConnection();
                //Change the prepare statement
                PreparedStatement preparedStatement = connection.prepareStatement("" +
                        "SELECT u.id, u.name, u.password, r.id, r.name FROM " +
                        "public.user u " +
                        "LEFT JOIN user_role ur ON ur.user_id = u.id " +
                        "LEFT JOIN role r ON r.id = ur.role_id WHERE u.name = ?")
                ){
            preparedStatement.setString(1, userName);
            try(ResultSet rs = preparedStatement.executeQuery()){
                boolean isFirst = true;
                CandidateAuthenticatedUser candidateAuthenticatedUser = null;
                while (rs.next()){
                    if(isFirst){
                        candidateAuthenticatedUser = new CandidateAuthenticatedUser(
                                rs.getLong(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4)
                        );
                    }
                    isFirst = false;
                }
                return candidateAuthenticatedUser;
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
}
