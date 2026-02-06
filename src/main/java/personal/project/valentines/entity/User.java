package personal.project.valentines.entity;

import personal.project.valentines.base.Person;

public class User extends Person {
    public User(){}

    public User(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public User(String firstName, String lastName, int age) {
        super(firstName, lastName, age);
    }
}
