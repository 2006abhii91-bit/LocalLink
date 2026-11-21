
package com.locallink.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingRequest {

    private Long workerProfileId;

    private String serviceDescription;

    private String serviceAddress;

    private LocalDateTime scheduledAt;
}
