package personal.project.valentines.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "letters")
@Setter
@Getter
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long letter_id;

    @Column(columnDefinition = "TEXT")
    String text;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
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
