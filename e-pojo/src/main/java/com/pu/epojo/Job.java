package com.pu.epojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Job {
    Long id;
    Long companyId;
    String name;
    String description;
    String companyEmail;
    Integer salaryMin;
    Integer salaryMax;
    Integer status;
    @DateTimeFormat
    LocalDateTime createTime;
    @DateTimeFormat
    LocalDateTime updateTime;
}
