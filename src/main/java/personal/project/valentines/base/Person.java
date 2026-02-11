package personal.project.valentines.base;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "people")
@Getter
@Setter
public class Person {

    public enum Category {
        PARENTS,
        SPECIAL_SOMEONE,
        PEERS
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = true)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = true)
    private String secretQuestion;

    @Column(nullable = true)
    private String secretAnswer;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Person(){}

    public Person(String firstName, String lastName, Category category) {
        this.firstName = Objects.requireNonNull(firstName, "firstName must not be null");
        this.lastName = Objects.requireNonNull(lastName, "lastName must not be null");
        this.category = Objects.requireNonNull(category, "category must not be null");
    }

    public Person(String firstName, String lastName, int age, Category category) {
        this.firstName = Objects.requireNonNull(firstName, "firstName must not be null");
        this.lastName = Objects.requireNonNull(lastName, "lastName must not be null");
        this.category = Objects.requireNonNull(category, "category must not be null");
        this.age = age;
    }

    public Person(String firstName, String lastName, int age, Category category,
                  String secretQuestion, String secretAnswer) {
        this.firstName = Objects.requireNonNull(firstName, "firstName must not be null");
        this.lastName = Objects.requireNonNull(lastName, "lastName must not be null");
        this.category = Objects.requireNonNull(category, "category must not be null");
        this.age = age;
        this.secretQuestion = secretQuestion;
        this.secretAnswer = secretAnswer;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
