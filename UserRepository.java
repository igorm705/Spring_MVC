package main.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for saving in database data: name of user and his message
 */
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
}
