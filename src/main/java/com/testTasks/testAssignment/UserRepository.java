package com.testTasks.testAssignment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   @Query("SELECT u FROM User as u WHERE u.birthday >= :from AND u.birthday <= :to")
   Page<User> findAllByParam(@Param("from") LocalDate from, @Param("to") LocalDate to, Pageable pageable);
}
