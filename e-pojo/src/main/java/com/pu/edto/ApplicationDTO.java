package com.pu.edto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApplicationDTO {
    private Long applicationId;
    private Long jobId;
    private String jobName;
    private Long companyId;
    private String companyName;
    private String applicationStatus;
    private LocalDateTime applicationTime;
}
