
package com.locallink.repository;

import com.locallink.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Get all bookings made by a customer
    List<Booking> findByCustomer_Id(Long customerId);

    // Get all bookings received by a worker
    List<Booking> findByWorkerProfile_Id(Long workerProfileId);
}
