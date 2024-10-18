package com.example.CourseSellingWeb.respositories;

import com.example.CourseSellingWeb.models.Account;
import com.example.CourseSellingWeb.models.Token;
import com.example.CourseSellingWeb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    List<Token> findByAccount(Account account);
}
