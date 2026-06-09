package com.lautamgioi.service;

import com.lautamgioi.model.Account;

import java.util.Optional;

public interface AuthUseCase {
    Optional<Account> login(String username, String password);
    int register(Account account);
    Account account(int id);
    Account updateCustomerContact(int id, String email, String phone);
}
