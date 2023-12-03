package ru.practicum.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.user.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u")
    List<User> findAllUsers(Pageable page);

    @Query("SELECT u FROM User u WHERE u.id IN (:userIds)")
    List<User> findUsersByIds(List<Long> userIds, Pageable page);

}
