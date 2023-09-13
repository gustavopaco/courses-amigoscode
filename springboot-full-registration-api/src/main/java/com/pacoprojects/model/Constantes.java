package com.pacoprojects.model;

import lombok.Getter;

@Getter
public enum Constantes {
    HTMLCONTENT("text/html; charset=utf-8"),

    SMTP("aws"),

    REMETENTE("gustavopacotest@gmail.com"),

    PASSWORD_REMETENTE("ddncmnmtfzfqtfnl"),

    FROM("email@gmail.com"),

    FROMNAME("JOverflow"),

    ASSUNTO("Password Recovery"),

    USERNAME("@gmail.com"),

    PASSWORD("password123"),

//    HOST("email-smtp.us-east-1.amazonaws.com");
    HOST("smtp.gmail.com");

    private String value;

    Constantes(String value) {
        this.value = value;
    }
}
