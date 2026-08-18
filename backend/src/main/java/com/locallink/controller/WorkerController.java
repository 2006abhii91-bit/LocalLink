package com.locallink.controller;

import com.locallink.dto.request.WorkerOnboardingRequest;
import com.locallink.dto.response.WorkerProfileResponse;
import com.locallink.service.impl.WorkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}