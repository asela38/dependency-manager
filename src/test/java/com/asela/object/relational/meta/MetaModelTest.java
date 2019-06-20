package com.asela.object.relational.meta;

import com.asela.object.relational.entity.Person;
import org.junit.Test;

import java.util.List;

public class MetaModelTest {

    @Test
    public void createMetaModelForClass() {

        MetaModel model = MetaModel.of(Person.class);

        PrimaryKeyField primaryKeyField = model.getPrimaryKeysField();
        List<ColumnField> column = model.getColumnFields();


        System.out.println("primaryKeyField.getName() = " + primaryKeyField.getName());
        System.out.println("primaryKeyField.getType() = " + primaryKeyField.getType());

        for (ColumnField col : column) {

            System.out.println("col.getName() = " + col.getName());
            System.out.println("col.getType() = " + col.getType());
        }

    }
}
