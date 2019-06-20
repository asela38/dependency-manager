package com.asela.object.relational.meta;

import com.asela.object.relational.anno.Column;

import java.lang.reflect.Field;

public class ColumnField {
    private Field field;
    private  Column column;

    public ColumnField(Field field) {

        this.field = field;
        column = field.getAnnotation(Column.class);
    }


    public Class<?> getType() {
        return field.getType();
    }

    public String getName() {
        return column.name();
        //return field.getName();
    }

    public Field getField() {
        return field;
    }
}
