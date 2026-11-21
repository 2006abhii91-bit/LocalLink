
package com.locallink.dto.response;

import com.locallink.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookingResponse {

    private Long id;

    private Long customerId;

    private Long workerProfileId;

    private String serviceDescription;

    private String serviceAddress;

    private LocalDateTime scheduledAt;

    private BookingStatus status;

    private LocalDateTime createdAt;
}
