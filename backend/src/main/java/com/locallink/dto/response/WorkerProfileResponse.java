package com.locallink.dto.response;

public class WorkerProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String serviceCategory;
    private Integer experienceYears;
    private String bio;
    private String location;
    private String availability;

    public WorkerProfileResponse() {
    }

    public WorkerProfileResponse(Long id, String name, String email, String phone,
                                 String serviceCategory, Integer experienceYears,
                                 String bio, String location, String availability) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.serviceCategory = serviceCategory;
        this.experienceYears = experienceYears;
        this.bio = bio;
        this.location = location;
        this.availability = availability;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}