package com.vsked.system.domain;

import java.util.Set;

public class Department {

    private DepartmentId id;
    private DepartmentName name;
    private ParentDepartmentId parentId;
    private Set<Department> children;

    public Department(DepartmentId id, DepartmentName name, ParentDepartmentId parentId,  Set<Department> children) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.children = children;
    }
    public DepartmentId getId() {
        return id;
    }
    public DepartmentName getName() {
        return name;
    }
    public ParentDepartmentId getParentId() {
        return parentId;
    }
    public Set<Department> getChildren() {
        return children;
    }
    public boolean hasChild(Department department) {
        return children.contains(department);
    }
}
