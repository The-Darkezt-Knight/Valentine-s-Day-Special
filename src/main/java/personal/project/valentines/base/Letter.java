package personal.project.valentines.base;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "TEXT")
    String text;

    @ManyToOne
    Person recipient;

    LocalDateTime createdAt;

    public Letter(){}

    public Letter(String text, Person recipient) {
        this.text = Objects.requireNonNull(text);
        this.recipient = Objects.requireNonNull(recipient);
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
