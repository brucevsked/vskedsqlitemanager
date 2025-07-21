package com.vsked.system.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public enum DataType {

    BOOLEAN("boolean", boolean.class),
    BYTE("byte", byte.class),
    SHORT("short", short.class),
    INT("int", int.class),
    LONG("long", long.class),
    FLOAT("float", float.class),
    DOUBLE("double", double.class),
    CHAR("char", char.class),

    BOOLEAN_OBJECT("Boolean", Boolean.class),
    BYTE_OBJECT("Byte", Byte.class),
    SHORT_OBJECT("Short", Short.class),
    INTEGER("Integer", Integer.class),
    LONG_OBJECT("Long", Long.class),
    FLOAT_OBJECT("Float", Float.class),
    DOUBLE_OBJECT("Double", Double.class),
    CHARACTER("Character", Character.class),
    STRING("String", String.class),

    // 可选：集合类型
    LIST("List", List.class),
    SET("Set", Set.class),
    MAP("Map", Map.class),
    COLLECTION("Collection", Collection.class),

    // 可选：数组类型
    OBJECT_ARRAY("Object[]", Object[].class),
    STRING_ARRAY("String[]", String[].class),
    INT_ARRAY("int[]", int[].class),
    LONG_ARRAY("long[]", long[].class),
    BOOLEAN_ARRAY("boolean[]", boolean[].class);

    // 枚举属性
    private final String typeName;
    private final Class<?> typeClass;

    DataType(String typeName, Class<?> typeClass) {
        this.typeName = typeName;
        this.typeClass = typeClass;
    }

    public String getTypeName() {
        return typeName;
    }

    public Class<?> getTypeClass() {
        return typeClass;
    }

    // 通过类型名称获取枚举（忽略大小写）
    public static Optional<DataType> fromTypeName(String typeName) {
        if (typeName == null || typeName.isEmpty()) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(t -> t.typeName.equalsIgnoreCase(typeName))
                .findFirst();
    }

    // 获取所有类型名称
    public static Set<String> getAllTypeNames() {
        return Arrays.stream(values())
                .map(DataType::getTypeName)
                .sorted()
                .collect(Collectors.toSet());
    }
}

