package com.springboot.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.Entity.User;

public interface UserRepository extends JpaRepository<User,Integer>{

    @Query(nativeQuery = true,value =" SELECT * FROM USERS")
    public List<User> findAllUserByNativQuery();

    @Query(nativeQuery = true,value = "SELECT * FROM USERS WHERE ID = :userId")
    public User findUserByNativQuery(@Param("userId") int userId);
} 
