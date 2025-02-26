package com.test.todoapp.repository;

import com.test.todoapp.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{'email': ?0, 'objStatus': 'ACTIVE'}")
    Optional<User> findActiveByEmail(String email);

    boolean existsByEmail(String email);

    @Query("{'_id': ?0, 'objStatus': 'ACTIVE'}")
    Optional<User> findActiveById(String id);

}
