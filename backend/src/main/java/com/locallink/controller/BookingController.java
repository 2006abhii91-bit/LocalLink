
package com.locallink.controller;

import com.locallink.dto.request.BookingRequest;
import com.locallink.dto.response.BookingResponse;
import com.locallink.entity.Booking;
import com.locallink.entity.User;
import com.locallink.service.impl.BookingService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @AuthenticationPrincipal User customer,
            @RequestBody BookingRequest request) {

        Booking booking = bookingService.createBooking(
                customer.getId(),
                request.getWorkerProfileId(),
                request.getServiceDescription(),
                request.getServiceAddress(),
                request.getScheduledAt()
        );

        BookingResponse response = new BookingResponse(
                booking.getId(),
                booking.getCustomer().getId(),
                booking.getWorkerProfile().getId(),
                booking.getServiceDescription(),
                booking.getServiceAddress(),
                booking.getScheduledAt(),
                booking.getStatus(),
                booking.getCreatedAt()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @GetMapping("/customer")
    public ResponseEntity<List<BookingResponse>> getCustomerBookings(
            @AuthenticationPrincipal User customer) {

        List<BookingResponse> bookings = bookingService
                .getCustomerBookings(customer.getId())
                .stream()
                .map(booking -> new BookingResponse(
                        booking.getId(),
                        booking.getCustomer().getId(),
                        booking.getWorkerProfile().getId(),
                        booking.getServiceDescription(),
                        booking.getServiceAddress(),
                        booking.getScheduledAt(),
                        booking.getStatus(),
                        booking.getCreatedAt()
                ))
                .toList();

        return ResponseEntity.ok(bookings);
    }


    @GetMapping("/worker")
    public ResponseEntity<List<BookingResponse>> getWorkerBookings(
            @AuthenticationPrincipal User worker) {

        List<BookingResponse> bookings = bookingService
                .getWorkerBookings(worker.getId())
                .stream()
                .map(booking -> new BookingResponse(
                        booking.getId(),
                        booking.getCustomer().getId(),
                        booking.getWorkerProfile().getId(),
                        booking.getServiceDescription(),
                        booking.getServiceAddress(),
                        booking.getScheduledAt(),
                        booking.getStatus(),
                        booking.getCreatedAt()
                ))
                .toList();

        return ResponseEntity.ok(bookings);
    }

}
