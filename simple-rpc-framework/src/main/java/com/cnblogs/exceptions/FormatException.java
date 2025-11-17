package com.cnblogs.exceptions;

import lombok.Getter;

@Getter
public class FormatException extends RuntimeException{
    String msg;

    public FormatException(String msg) {
        super(msg);
    }
}
