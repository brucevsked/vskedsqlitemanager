package com.vsked.jpa.po;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Table(name = "user")
@Entity
public class UserPO implements Serializable {

    @Transient
    private static final long serialVersionUID = -5826993476244087741L;

    @Id
    private Long id;
    private String name;
    private Boolean isLogin;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "userAccount",joinColumns = {@JoinColumn(name = "userId")},inverseJoinColumns = {@JoinColumn(name = "accountId")})
    private AccountPO account;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "userCertificate",joinColumns = {@JoinColumn(name = "userId")},inverseJoinColumns = {@JoinColumn(name = "certificateId")})
    private List<CertificatePO> certificate;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "userRole",joinColumns = {@JoinColumn(name = "userId")},inverseJoinColumns = {@JoinColumn(name = "roleId")})
    private List<RolePO> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    public AccountPO getAccount() {
        return account;
    }

    public void setAccount(AccountPO account) {
        this.account = account;
    }

    public List<CertificatePO> getCertificate() {
        return certificate;
    }

    public void setCertificate(List<CertificatePO> certificate) {
        this.certificate = certificate;
    }

    public List<RolePO> getRoles() {
        return roles;
    }

    public void setRoles(List<RolePO> roles) {
        this.roles = roles;
    }

    public UserPO() {
    }

    public UserPO(Long id, String name, Boolean isLogin) {
        this.id = id;
        this.name = name;
        this.isLogin = isLogin;
    }

    public String toString() {
        return "{" +
                "id=" + id +
                ", name=" + name + "" +
                ", isLogin=" + isLogin +
                ", account=" + account +
                ", certificate=" + certificate +
                "}";
    }
}
