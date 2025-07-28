package com.vsked.system.domain;

public class Account {
    private AccountId id;
    private AccountName name;
    private AccountPassword password;
    private AccountType type;

    public Account(AccountId id, AccountName name, AccountPassword password, AccountType type) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.type = type;
    }

    public AccountId getId() {
        return id;
    }

    public AccountName getName() {
        return name;
    }

    public AccountPassword getPassword() {
        return password;
    }

    public AccountType getType() {
        return type;
    }
}
