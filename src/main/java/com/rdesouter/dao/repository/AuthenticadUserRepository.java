package com.rdesouter.dao.repository;

import com.rdesouter.model.AuthenticatedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticadUserRepository extends JpaRepository<AuthenticatedUser, Integer> {

}
