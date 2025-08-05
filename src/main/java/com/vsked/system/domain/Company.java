package com.vsked.system.domain;

public class Company {

    private CompanyId id;
    private CompanyName name;
    private ParentCompanyId parentId;

    public Company(CompanyId id, CompanyName name, ParentCompanyId parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public CompanyId getId() {
        return id;
    }

    public CompanyName getName() {
        return name;
    }

    public ParentCompanyId getParentId() {
        return parentId;
    }
}
