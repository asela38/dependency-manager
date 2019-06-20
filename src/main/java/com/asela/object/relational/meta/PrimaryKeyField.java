package com.asela.object.relational.meta;

import com.asela.object.relational.anno.PrimaryKey;

import java.lang.reflect.Field;

public class PrimaryKeyField {

    private Field field;
    private  PrimaryKey primaryKey;

    public PrimaryKeyField(Field field) {
        
        this.field = field;
        primaryKey = this.field.getAnnotation(PrimaryKey.class);
    }

    public Class<?> getType() {
        return field.getType();
    }

    public String getName() {
      //  return field.getName();
        return primaryKey.name();
    }

    public Field getField() {
        return field;
    }
}
