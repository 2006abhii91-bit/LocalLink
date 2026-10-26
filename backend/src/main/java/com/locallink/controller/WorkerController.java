package com.locallink.controller;

import com.locallink.dto.request.WorkerOnboardingRequest;
import com.locallink.dto.request.WorkerProfileUpdateRequest;
import com.locallink.dto.response.WorkerProfileResponse;
import com.locallink.service.impl.WorkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/worker")
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @PostMapping("/onboard")
    public ResponseEntity<WorkerProfileResponse> onboardWorker(
            @RequestBody WorkerOnboardingRequest request) {

        WorkerProfileResponse response = workerService.onboardWorker(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<WorkerProfileResponse> getWorkerProfile() {

        WorkerProfileResponse response = workerService.getWorkerProfile();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<WorkerProfileResponse> updateWorkerProfile(
            @RequestBody WorkerProfileUpdateRequest request) {

        WorkerProfileResponse response = workerService.updateWorkerProfile(request);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/search")
    public ResponseEntity<List<WorkerProfileResponse>> searchWorkers(
            @RequestParam(required = false) String serviceCategory,
            @RequestParam(required = false) String location) {

        List<WorkerProfileResponse> workers =
                workerService.searchWorkers(serviceCategory, location);

        return ResponseEntity.ok(workers);
    }


}