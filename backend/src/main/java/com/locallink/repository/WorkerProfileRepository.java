package com.locallink.repository;

import com.locallink.entity.WorkerProfile;
import com.locallink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkerProfileRepository extends JpaRepository<WorkerProfile, Long> {

    Optional<WorkerProfile> findByUser(User user);

    boolean existsByUser(User user);
}