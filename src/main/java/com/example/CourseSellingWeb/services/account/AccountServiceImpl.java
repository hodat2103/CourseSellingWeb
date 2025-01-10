package com.example.CourseSellingWeb.services.account;

import com.example.CourseSellingWeb.dtos.AccountAndUserDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.exceptions.InvalidPasswordException;
import com.example.CourseSellingWeb.models.Account;
import com.example.CourseSellingWeb.models.AccountAndUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountServiceImpl {
    AccountAndUser register(AccountAndUserDTO accountAndUserDTO) throws Exception;
    String login(String phoneNumber, String password) throws DataNotFoundException, InvalidParamException;
    String resetPassword(String email, String newPassword)
            throws InvalidPasswordException, DataNotFoundException;


    void updatePassword(String newPassword, Integer accountId) throws DataNotFoundException;
    void blockAccount(int accountId) throws DataNotFoundException;
    void unblockAccount(int accountId) throws DataNotFoundException;

    List<Account> getAll();
    Account getAccountDetailsFromToken(String token) throws Exception;

}
