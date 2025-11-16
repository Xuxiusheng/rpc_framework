package com.cnblogs;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class HelloMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;

    private String description;
}
