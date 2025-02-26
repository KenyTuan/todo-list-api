package com.test.todoapp.repository;

import com.test.todoapp.model.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {

    @Query("{'objStatus': 'ACTIVE'}")
    List<Task> findAllActive();

    @Query("{'_id': ?0, 'objStatus': 'ACTIVE'}")
    Optional<Task> findTaskActiveById(String id);

    @Query("{'userId': ?0,'objStatus': 'ACTIVE'}")
    List<Task> findAllActiveByUserId(String userId);

    @Query("{'_id': ?0, 'userId': ?1, 'objStatus': 'ACTIVE'}")
    Optional<Task> findTaskActiveByIdAndUserId(String id, String userId);

    @Query("{ 'title': { $regex: ?0, $options: 'i' },'objStatus': 'ACTIVE' }")
    Page<Task> findActiveByTitleContaining(String title, Pageable pageable);

    @Query("{ 'title': { $regex: ?0, $options: 'i' }," +
            "'objStatus': 'ACTIVE'," +
            "'userId': ?1 }")
    Page<Task> findActiveByTitleContainingAndUserId(String title, String userId, PageRequest pageRequest);
}
