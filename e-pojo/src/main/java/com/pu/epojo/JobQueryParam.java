package com.pu.epojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class JobQueryParam {
    private Integer page = 1;
    private Integer rows = 10;
    private String name;
}
