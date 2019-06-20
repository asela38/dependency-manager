package com.asela.object.relational.meta;

import com.asela.object.relational.anno.Column;
import com.asela.object.relational.anno.PrimaryKey;
import com.asela.object.relational.entity.Person;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MetaModel<T> {

    private Class<T> clss;

    public MetaModel(Class<T> clss) {
        this.clss = clss;
    }

    public static <T> MetaModel of(Class<T> clss) {
        return new MetaModel(clss);
    }

    public PrimaryKeyField getPrimaryKeysField() {

        Field[] fields = clss.getDeclaredFields();
        for (Field field : fields) {
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            if (primaryKey != null) {
                return new PrimaryKeyField(field);
            }
        }

        throw new IllegalStateException("Entity has no private keys : " + clss.getSimpleName());
    }

    public List<ColumnField> getColumnFields() {

        List<ColumnField> fields = new ArrayList<>();

        for (Field field : clss.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                fields.add(new ColumnField(field));
            }
        }

        return fields;
    }

    public String buildInsertRequest() {

        List<String> fields = getFields();

        String columns = fields.stream().collect(Collectors.joining(", ", "(", ")"));


        return String.format("insert into %s %s values %s", clss.getSimpleName(), columns, columns.replaceAll("\\w+", "?"));
    }

    private List<String> getFields() {
        List<String> fields = new LinkedList<>();
        fields.add(getPrimaryKeysField().getName());
        getColumnFields().stream().map(ColumnField::getName).forEach(fields::add);
        return fields;
    }

    public String buildSelectRequest() {
        PrimaryKeyField primaryKeysField = getPrimaryKeysField();
        return String.format("Select %s From %s Where %s = ? ", String.join(", ", getFields()), clss.getSimpleName(),
                primaryKeysField.getName());
    }
}
