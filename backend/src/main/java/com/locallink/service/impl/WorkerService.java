
package com.locallink.service.impl;

import com.locallink.dto.request.WorkerOnboardingRequest;
import com.locallink.dto.request.WorkerProfileUpdateRequest;
import com.locallink.dto.response.WorkerProfileResponse;
import com.locallink.entity.User;
import com.locallink.entity.WorkerProfile;
import com.locallink.repository.UserRepository;
import com.locallink.repository.WorkerProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class WorkerService {

    private final UserRepository userRepository;
    private final WorkerProfileRepository workerProfileRepository;

    public WorkerService(UserRepository userRepository,
                         WorkerProfileRepository workerProfileRepository) {
        this.userRepository = userRepository;
        this.workerProfileRepository = workerProfileRepository;
    }

    public WorkerProfileResponse onboardWorker(
            WorkerOnboardingRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (workerProfileRepository.existsByUser(user)) {
            throw new RuntimeException("Worker profile already exists");
        }

        WorkerProfile workerProfile = new WorkerProfile();

        workerProfile.setUser(user);
        workerProfile.setServiceCategory(request.getServiceCategory());
        workerProfile.setExperienceYears(request.getExperienceYears());
        workerProfile.setBio(request.getBio());
        workerProfile.setLocation(request.getLocation());
        workerProfile.setAvailability(request.getAvailability());

        workerProfile = workerProfileRepository.save(workerProfile);

        user.setWorker(true);
        userRepository.save(user);

        return new WorkerProfileResponse(
                workerProfile.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                workerProfile.getServiceCategory(),
                workerProfile.getExperienceYears(),
                workerProfile.getBio(),
                workerProfile.getLocation(),
                workerProfile.getAvailability()
        );
    }

    public WorkerProfileResponse getWorkerProfile() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        WorkerProfile workerProfile = workerProfileRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Worker profile not found"));

        return new WorkerProfileResponse(
                workerProfile.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                workerProfile.getServiceCategory(),
                workerProfile.getExperienceYears(),
                workerProfile.getBio(),
                workerProfile.getLocation(),
                workerProfile.getAvailability()
        );
    }

    public WorkerProfileResponse updateWorkerProfile(
            WorkerProfileUpdateRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        WorkerProfile workerProfile = workerProfileRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Worker profile not found"));

        workerProfile.setServiceCategory(request.getServiceCategory());
        workerProfile.setExperienceYears(request.getExperienceYears());
        workerProfile.setBio(request.getBio());
        workerProfile.setLocation(request.getLocation());
        workerProfile.setAvailability(request.getAvailability());

        workerProfile = workerProfileRepository.save(workerProfile);

        return new WorkerProfileResponse(
                workerProfile.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                workerProfile.getServiceCategory(),
                workerProfile.getExperienceYears(),
                workerProfile.getBio(),
                workerProfile.getLocation(),
                workerProfile.getAvailability()
        );
    }

    public List<WorkerProfileResponse> searchWorkers(
            String serviceCategory, String location) {

        boolean hasCategory = serviceCategory != null
                && !serviceCategory.isBlank();

        boolean hasLocation = location != null
                && !location.isBlank();

        if (!hasCategory && !hasLocation) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Provide a service category or location to search"
            );
        }

        List<WorkerProfile> workers;

        if (hasCategory && hasLocation) {
            workers = workerProfileRepository
                    .findByServiceCategoryContainingIgnoreCaseAndLocationContainingIgnoreCase(
                            serviceCategory.trim(), location.trim());
        } else if (hasCategory) {
            workers = workerProfileRepository
                    .findByServiceCategoryContainingIgnoreCase(
                            serviceCategory.trim());
        } else {
            workers = workerProfileRepository
                    .findByLocationContainingIgnoreCase(location.trim());
        }

        return workers.stream()
                .map(worker -> new WorkerProfileResponse(
                        worker.getId(),
                        worker.getUser().getFullName(),
                        worker.getUser().getEmail(),
                        worker.getUser().getPhoneNumber(),
                        worker.getServiceCategory(),
                        worker.getExperienceYears(),
                        worker.getBio(),
                        worker.getLocation(),
                        worker.getAvailability()
                ))
                .toList();
    }
}
