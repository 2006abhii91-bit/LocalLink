
package com.locallink.repository;

import com.locallink.entity.WorkerProfile;
import com.locallink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import java.util.List;
import java.util.Optional;

public interface WorkerProfileRepository extends JpaRepository<WorkerProfile, Long> {

    Optional<WorkerProfile> findByUser(User user);

    boolean existsByUser(User user);

    List<WorkerProfile> findByServiceCategoryContainingIgnoreCase(String serviceCategory);

    List<WorkerProfile> findByLocationContainingIgnoreCase(String location);

    List<WorkerProfile> findByServiceCategoryContainingIgnoreCaseAndLocationContainingIgnoreCase(
            String serviceCategory, String location);

    Optional<WorkerProfile> findByUser_Id(Long userId);
}
