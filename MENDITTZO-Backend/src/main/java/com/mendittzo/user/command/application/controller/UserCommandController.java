package com.mendittzo.user.command.application.controller;

import com.mendittzo.user.command.application.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserCommandController {

    private final UserCommandService userCommandService;

//    @GetMapping("/info")
//    public Response<UserInfoResponseDTO> getUserInfo() {
//
//    }
}
