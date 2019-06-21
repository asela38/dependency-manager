package com.asela.object.relational.entity.manager;

import com.asela.object.relational.bean.BeanManager;
import com.asela.object.relational.entity.Person;
import com.asela.object.relational.entity.manager.EntityManger;
import org.junit.Test;

import java.sql.SQLException;

public class EntityManagerTest {

    @Test
    public void entityMangerCreate() throws SQLException, IllegalAccessException {

        BeanManager beanManager = BeanManager.getInstance();

        EntityManger<Person> entityManger = beanManager.getInstance(ManagedEntityManager.class, Person.class);

        Person saman = new Person("Saman", 30);
        Person kamal = new Person("Kamal", 24);
        Person nayana = new Person("Nayana", 28);
        Person nimal = new Person("Nimal", 34);

        entityManger.persist(saman);
        entityManger.persist(kamal);
        entityManger.persist(nayana);
        entityManger.persist(nimal);

        System.out.println("nimal = " + nimal);
        System.out.println("nayana = " + nayana);
        System.out.println("saman = " + saman);
        System.out.println("kamal = " + kamal);


    }

    @Test
    public void entityManagerFind() throws Exception {

        BeanManager beanManager = BeanManager.getInstance();
        EntityManger<Person> entityManger = beanManager.getInstance(ManagedEntityManager.class, Person.class);

        Person person = entityManger.find(20);

        System.out.println(person);


    }
}
