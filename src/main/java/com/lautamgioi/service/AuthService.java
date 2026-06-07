package com.lautamgioi.service;

import com.lautamgioi.dao.AccountDao;
import com.lautamgioi.model.Account;

import java.util.Optional;

public class AuthService {
    private final AccountDao accountDao = new AccountDao();

    public Optional<Account> login(String username, String password) {
        String normalizedUsername = Validators.username(username);
        String normalizedPassword = Validators.password(password);
        return accountDao.findByUsername(normalizedUsername)
                .filter(account -> account.getPassword().equals(normalizedPassword));
    }

    public int register(Account account) {
        account.setUsername(Validators.username(account.getUsername()));
        account.setPassword(Validators.password(account.getPassword()));
        account.setFullName(Validators.required(account.getFullName(), "Họ tên", 100));
        account.setEmail(Validators.email(account.getEmail()));
        String phone = Validators.optional(account.getPhone(), "Số điện thoại", 20);
        account.setPhone(phone == null ? null : Validators.phone(phone));
        return accountDao.createCustomer(account);
    }
}
