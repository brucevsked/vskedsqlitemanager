package com.vsked.system.domain;

public class AccountPassword {

    private String password;
    private String salt;

    public AccountPassword(String password, String salt) {
        this.password = password;
        this.salt = salt;
    }
    public String getPassword() {
        return password;
    }
    public String getSalt() {
        return salt;
    }
}
