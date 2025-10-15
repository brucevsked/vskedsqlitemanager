package com.vsked.system.domain;

import org.apache.commons.lang3.StringUtils;

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

    public boolean validAccountId(){
        return id != null && id.getId()!=null && id.getId()>=0;
    }

    public boolean validAccountName(){
        return name != null && name.getName()!=null && StringUtils.isNotBlank(name.getName());
    }

    public boolean validAccountPassword(){
        return password != null && password.getPassword()!=null && StringUtils.isNotBlank(password.getPassword());
    }

    public boolean validAccountType(){
        return type != null && type.getDescription()!=null && StringUtils.isNotBlank(type.getDescription());
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
