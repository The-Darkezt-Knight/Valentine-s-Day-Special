package personal.project.valentines.base;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "message")
@Getter
@Setter
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String sender;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime createdAt;

    public Message(){}

    public Message(String sender, String message) {
        this.sender = Objects.requireNonNull(sender);
        this.text = Objects.requireNonNull(message);
    }

    public Message(String text){
        this.text = Objects.requireNonNull(text);
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
