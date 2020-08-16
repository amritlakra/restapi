package com.interview.template.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ExceptionResponse {
    private Date timestamp;
    private String message;
    private String details;
    List<String> errors;
}
