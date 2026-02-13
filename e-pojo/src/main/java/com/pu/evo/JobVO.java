package com.pu.evo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobVO {
    Long id;
    String name;
    String description;
    String companyName;
    Integer salaryMin;
    Integer salaryMax;
    String location;
}
