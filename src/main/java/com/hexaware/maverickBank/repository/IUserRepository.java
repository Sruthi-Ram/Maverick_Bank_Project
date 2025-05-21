package com.hexaware.maverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hexaware.maverickBank.entity.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);

    // Create operation is handled by JpaRepository's save() method

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password, u.email = :email, u.role = :role, u.updatedAt = CURRENT_TIMESTAMP, u.status = :status WHERE u.userId = :userId")
    void updateUser(@Param("userId") int userId, @Param("password") String password, @Param("email") String email, @Param("role") String role, @Param("status") String status);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.status = 'inactive', u.updatedAt = CURRENT_TIMESTAMP WHERE u.userId = :userId")
    void deactivateUser(@Param("userId") int userId);

    // Delete operation (if needed as a separate method)
    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.userId = :userId")
    void deleteUserById(@Param("userId") int userId);
}