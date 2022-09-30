package com.vorova.todo.service.util;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserUtil {

    public boolean isCorrectEmail(String email) {
        return Pattern.compile("\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*\\.\\w{2,6}")
                .matcher(email)
                .matches();
    }

    public boolean isCorrectPassword(String password) {
        return Pattern.compile("[a-zA-Z\\d._\\-]{6,20}")
                .matcher(password)
                .matches();
    }

}
