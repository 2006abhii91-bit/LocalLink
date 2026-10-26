package com.locallink.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WorkerProfileUpdateRequest {

    private String serviceCategory;
    private Integer experienceYears;
    private String bio;
    private String location;
    private String availability;

    public WorkerProfileUpdateRequest() {
    }

}