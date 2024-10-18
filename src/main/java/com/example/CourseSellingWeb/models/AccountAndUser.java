package com.example.CourseSellingWeb.models;

public class AccountAndUser {
    private Account account;
    private User user;

    public AccountAndUser(Account account, User user) {
        this.user = user;
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public User getUser() {
        return user;
    }
}
