
package com.locallink.service.impl;

import com.locallink.entity.Booking;
import com.locallink.entity.User;
import com.locallink.entity.WorkerProfile;
import com.locallink.enums.BookingStatus;
import com.locallink.repository.BookingRepository;
import com.locallink.repository.UserRepository;
import com.locallink.repository.WorkerProfileRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final WorkerProfileRepository workerProfileRepository;

    public BookingService(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            WorkerProfileRepository workerProfileRepository) {

        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.workerProfileRepository = workerProfileRepository;
    }

    // 1. CREATE BOOKING
    @Transactional
    public Booking createBooking(
            Long customerId,
            Long workerProfileId,
            String description,
            String address,
            LocalDateTime scheduledAt) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));

        WorkerProfile worker = workerProfileRepository
                .findById(workerProfileId)
                .orElseThrow(() ->
                        new RuntimeException("Worker not found"));

        if (description == null || description.isBlank()) {
            throw new RuntimeException("Service description is required");
        }

        if (address == null || address.isBlank()) {
            throw new RuntimeException("Service address is required");
        }

        if (scheduledAt == null ||
                !scheduledAt.isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Invalid booking date");
        }

        Booking booking = new Booking();

        booking.setCustomer(customer);
        booking.setWorkerProfile(worker);
        booking.setServiceDescription(description);
        booking.setServiceAddress(address);
        booking.setScheduledAt(scheduledAt);
        booking.setStatus(BookingStatus.PENDING);

        return bookingRepository.save(booking);
    }

    // 2. GET CUSTOMER BOOKINGS
    public List<Booking> getCustomerBookings(Long customerId) {

        return bookingRepository.findByCustomer_Id(customerId);
    }

    // 3. GET WORKER BOOKINGS
    public List<Booking> getWorkerBookings(Long userId) {

        WorkerProfile worker = workerProfileRepository
                .findByUser_Id(userId)
                .orElseThrow(() ->
                        new RuntimeException("Worker profile not found"));

        return bookingRepository.findByWorkerProfile_Id(worker.getId());
    }
}
