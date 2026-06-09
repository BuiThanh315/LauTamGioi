package com.lautamgioi.service;

import com.lautamgioi.dao.AccountDao;
import com.lautamgioi.dao.AccountRepository;
import com.lautamgioi.model.Account;
import com.lautamgioi.model.Role;

import java.util.Optional;

public class AuthService implements AuthUseCase {
    private final AccountRepository accountRepository;

    public AuthService() {
        this(new AccountDao());
    }

    public AuthService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> login(String username, String password) {
        String normalizedUsername = Validators.username(username);
        String normalizedPassword = Validators.password(password);
        return accountRepository.findByUsername(normalizedUsername)
                .filter(account -> account.getPassword().equals(normalizedPassword));
    }

    @Override
    public int register(Account account) {
        account.setUsername(Validators.username(account.getUsername()));
        account.setPassword(Validators.password(account.getPassword()));
        account.setFullName(Validators.required(account.getFullName(), "Họ tên", 100));
        account.setEmail(Validators.email(account.getEmail()));
        String phone = Validators.optional(account.getPhone(), "Số điện thoại", 20);
        account.setPhone(phone == null ? null : Validators.phone(phone));
        return accountRepository.createCustomer(account);
    }

    @Override
    public Account account(int id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản."));
    }

    @Override
    public Account updateCustomerContact(int id, String email, String phone) {
        Account account = account(id);
        if (account.getRole() != Role.CUSTOMER) {
            throw new ValidationException("Chỉ khách hàng mới có thể cập nhật thông tin cá nhân.");
        }
        account.setEmail(Validators.email(email));
        account.setPhone(Validators.phone(phone));
        accountRepository.updateCustomerContact(account);
        return account(id);
    }
}
