package com.javakc.servicebase.handler;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HctfException extends RuntimeException {

    private Integer code;

    private String msg;
}
