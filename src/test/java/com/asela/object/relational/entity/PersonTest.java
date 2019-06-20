package com.asela.object.relational.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.logging.Logger;

public class PersonTest {


    @Test
    public void personShouldBeAbleToCreateWithNameAndAge() {
        Person person = new Person("Asela", 34);
        Logger.getAnonymousLogger().info(person.toString());
        Assert.assertNotNull(person);

    }
}
