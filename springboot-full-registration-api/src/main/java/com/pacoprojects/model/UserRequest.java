package com.pacoprojects.model;

import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserRequest {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
}
