package com.lautamgioi.dao;

import com.lautamgioi.model.Account;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findById(int id);
    Optional<Account> findByUsername(String username);
    int createCustomer(Account account);
    void updateCustomerContact(Account account);
}
