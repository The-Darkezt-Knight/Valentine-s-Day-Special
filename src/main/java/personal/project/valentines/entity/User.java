package personal.project.valentines.entity;

import personal.project.valentines.base.Person;

public class User extends Person {
    public User(){}

    public User(String firstName, String lastName, Category category) {
        super(firstName, lastName, category);
    }

    public User(String firstName, String lastName, int age, Category category) {
        super(firstName, lastName, age, category);
    }

    public User(String firstName, String lastName, int age, Category category,
                String secretQuestion, String secretAnswer) {
        super(firstName, lastName, age, category, secretQuestion, secretAnswer);
    }
}
