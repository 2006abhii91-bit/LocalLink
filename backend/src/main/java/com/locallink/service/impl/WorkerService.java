package com.locallink.service.impl;

import com.locallink.dto.request.WorkerOnboardingRequest;
import com.locallink.dto.response.WorkerProfileResponse;
import com.locallink.entity.User;
import com.locallink.entity.WorkerProfile;
import com.locallink.repository.UserRepository;
import com.locallink.repository.WorkerProfileRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static java.lang.System.in;

@Service
public class WorkerService {

    private final UserRepository userRepository;
    private final WorkerProfileRepository workerProfileRepository;

    public WorkerService(UserRepository userRepository,
                         WorkerProfileRepository workerProfileRepository) {
        this.userRepository = userRepository;
        this.workerProfileRepository = workerProfileRepository;
    }

    public WorkerProfileResponse onboardWorker(WorkerOnboardingRequest request) {

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

}