package com.asela.object.relational.entity.manager;

import com.asela.object.relational.anno.Inject;
import com.asela.object.relational.meta.ColumnField;
import com.asela.object.relational.meta.MetaModel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ManagedEntityManager<T> implements EntityManger<T> {


    private AtomicInteger idGen = new AtomicInteger(20);

    private Class<T> clss;

    @Inject
    private Connection connection;

     ManagedEntityManager(Class<T> clss) {
        this.clss = clss;
    }

    @Override
    public void persist(T entity) throws SQLException, IllegalAccessException {

        MetaModel model = MetaModel.of(entity.getClass());
        String sql = model.buildInsertRequest();

        System.out.println("sql = " + sql);

        try (PreparedStatement statement = prepareStatement(sql).addParameters(entity)) {
            statement.executeUpdate();
        }


    }

    @Override
    public T find(Object id  ) throws Exception {

        MetaModel model = MetaModel.of(clss);
        String sql = model.buildSelectRequest();
        System.out.println("sql = " + sql);

        try (PreparedStatement statement = prepareStatement(sql).addPrimaryKey(id);
             ResultSet resultSet = statement.executeQuery()) {

            return  buildInstanceFrom(clss, resultSet);
        }


    }

    private T buildInstanceFrom(Class<T> clss, ResultSet resultSet) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException, SQLException {

        MetaModel model = MetaModel.of(clss);

        T t = clss.getConstructor().newInstance();

        Class<?> type = model.getPrimaryKeysField().getType();

        resultSet.next();
        if(type == int.class) {

            String name = model.getPrimaryKeysField().getName();
            Field declaredField = clss.getDeclaredField(name);
            declaredField.setAccessible(true);
            declaredField.set(t, resultSet.getInt(name));
        }


        List<ColumnField> columnFields = model.getColumnFields();
        for (ColumnField columnField : columnFields) {
            if(columnField.getType() == int.class) {
                String name = columnField.getName();
                Field declaredField = clss.getDeclaredField(name);
                declaredField.setAccessible(true);
                declaredField.set(t, resultSet.getInt(name));
            }
            if(columnField.getType()  == String.class) {
                String name = columnField.getName();
                Field declaredField = clss.getDeclaredField(name);
                declaredField.setAccessible(true);
                declaredField.set(t, resultSet.getString(name));
            }
        }

        return t;
    }

    private PreparedStatementWrapper prepareStatement(String sql) throws SQLException {

        PreparedStatement statement = connection.prepareStatement(sql);
        return  new PreparedStatementWrapper(statement);
    }

    private class PreparedStatementWrapper {
        PreparedStatement statement;

        public PreparedStatementWrapper(PreparedStatement statement) {
            this.statement = statement;
        }


        public PreparedStatement addParameters(T entity) throws SQLException, IllegalAccessException {
            MetaModel model = MetaModel.of(entity.getClass());
            Class<?> type = model.getPrimaryKeysField().getType();

            if(type == int.class) {
                int  id= idGen.getAndDecrement();
                statement.setInt(1, id);

                Field field = model.getPrimaryKeysField().getField();
                field.setAccessible(true);
                field.set(entity, id);
            }

            List<ColumnField> columnFields = model.getColumnFields();
            for (int i = 0; i < columnFields.size(); i++) {

                ColumnField columnField = columnFields.get(i);
                Class<?> fieldType = columnField.getType();
                Field field = columnField.getField();
                field.setAccessible(true);
                Object object = field.get(entity);

                if(fieldType == int.class) {
                    statement.setInt(i + 2, (int)object);
                }
                if(fieldType == String.class) {
                    statement.setString(i + 2, (String) object);
                }
            }
            return statement;
        }

        public PreparedStatement addPrimaryKey(Object id) throws SQLException {
            MetaModel model = MetaModel.of(clss);
            Class<?> type = model.getPrimaryKeysField().getType();

            if(type == int.class) {
                statement.setInt(1, (int)id);

            }
            return statement;
        }
    }
}
