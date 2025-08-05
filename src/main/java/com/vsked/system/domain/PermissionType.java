package com.vsked.system.domain;

public class PermissionType {

    PermissionTypeEnum type;

    public PermissionType(Byte type) {
        if(type==null){
            throw new IllegalArgumentException("permission not be null！");
        }
        this.type = numberToType(type);
    }

    public PermissionTypeEnum getType() {
        return type;
    }

    public static PermissionTypeEnum numberToType(Byte b){
        return switch (b) {
            case 0 -> PermissionTypeEnum.action;
            case 1 -> PermissionTypeEnum.object;
            case 2 -> PermissionTypeEnum.attribute;
            case 3 -> PermissionTypeEnum.content;
            default -> throw new IllegalArgumentException("permission type error");
        };
    }

    public static Byte typeToNumber(PermissionTypeEnum tp){
        return switch (tp) {
            case action -> 0;
            case object -> 1;
            case attribute -> 2;
            case content -> 3;
        };
    }
}
