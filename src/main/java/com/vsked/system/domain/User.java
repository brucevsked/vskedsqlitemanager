package com.vsked.system.domain;

import java.util.Set;

public class User {

    private UserId id;
    private UserName name;
    private Set<Account> accounts;
    private Set<Role> roles;
    private Department department;
    private Certificate certificate;


    public User(UserId id, UserName name,Set<Account> accounts, Set<Role> roles, Department  department, Certificate certificate) {
        this.id = id;
        this.name = name;
        this.accounts = accounts;
        this.roles = roles;
        this.department = department;
        this.certificate = certificate;
    }
    public UserId getId() {
        return id;
    }
    public UserName getName() {
        return name;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Department getDepartment() {
        return department;
    }

    public Certificate getCertificate() {
        return certificate;
    }
    public boolean hasAccount(Account account) {
        return accounts.contains(account);
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public boolean hasPermission(Permission permission) {
        return roles.stream().anyMatch(role -> role.hasPermission(permission));
    }

    public boolean hasCertificate(Certificate certificate) {
        return this.certificate.equals(certificate);
    }
}
