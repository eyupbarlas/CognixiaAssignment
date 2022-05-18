package assignment1.Assignment.repository;

import assignment1.Assignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmail(String email);

    @Query(value = "SELECT * FROM user WHERE user.email= :email", nativeQuery = true)
    List<User> emailCheck(String email);

    @Query(value = "SELECT * FROM user WHERE user.password= :password", nativeQuery = true)
    List<User> passwordCheck(String password);
}
