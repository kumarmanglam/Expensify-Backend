package com.expensia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Generates all the boilerplate code for getter, setter, toString(), equals(), and hashCode() methods.
@AllArgsConstructor
@NoArgsConstructor
public class ApiResonseDto {
    int recordCount;  // The total number of records available (e.g., total records in a database table)
    Object response;
}
