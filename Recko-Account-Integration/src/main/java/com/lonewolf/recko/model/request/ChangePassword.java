package com.lonewolf.recko.model.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChangePassword {

    private final String username;
    private final String oldPassword;
    private final String newPassword;
}
