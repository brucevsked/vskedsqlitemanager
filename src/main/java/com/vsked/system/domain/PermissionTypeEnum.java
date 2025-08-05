package com.vsked.system.domain;

public enum PermissionTypeEnum {

    /**
     * 0 role has permission for object method
     */
    action,

    /**
     * 1 role has permission for Object
     */
    object,

    /**
     * 2 role has permission for object attribute
     */
    attribute,

    /**
     * 3 role has permission for data
     */
    content

}
