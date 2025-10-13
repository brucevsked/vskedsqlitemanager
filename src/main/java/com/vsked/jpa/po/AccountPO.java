package com.vsked.jpa.po;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;

@Table(name = "account")
@Entity
public class AccountPO implements Serializable {

    @Transient//此字段不需要持久化到数据库
    private static final long serialVersionUID = 2589051058614650816L;

    @Id
    private String accountName;
    private String password;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountPO() {
    }

    public AccountPO(String accountName, String password) {
        this.accountName = accountName;
        this.password = password;
    }

    @Override
    public String toString() {
        return "{" +
                "accountName='" + accountName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
