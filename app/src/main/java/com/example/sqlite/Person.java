package com.example.sqlite;

public class Person {
    private int id;
    private String name;
    private int Age;

    public Person(int id, String name, int age) {
        this.id = id;
        this.name = name;
        Age = age;
    }

    public Person(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    @Override
    public String toString() {
        return "Name = " + name + " - " +
                " Age = " + Age;
    }
}
