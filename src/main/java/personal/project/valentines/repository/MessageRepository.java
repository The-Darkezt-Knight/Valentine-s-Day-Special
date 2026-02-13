package personal.project.valentines.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import personal.project.valentines.base.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{
    
}
