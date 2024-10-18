package com.example.CourseSellingWeb.respositories;

import com.example.CourseSellingWeb.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);

    User findByAccountId(int accountId);

    @Query("SELECT u FROM User u WHERE :keyword IS NULL OR u.name LIKE %:keyword% " +
            "OR (:keyword IS NULL OR u.email LIKE %:keyword%)")
    Page<User> searchUsers(@Param("keyword") String keyword,
                           Pageable pageable);
}
