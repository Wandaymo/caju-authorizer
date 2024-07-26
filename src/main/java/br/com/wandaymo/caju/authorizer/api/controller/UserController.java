package br.com.wandaymo.caju.authorizer.api.controller;

import br.com.wandaymo.caju.authorizer.api.dto.UserDTO;
import br.com.wandaymo.caju.authorizer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    public String create(@RequestBody UserDTO userDTO) {
        return userService.save(userDTO);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO) {
        return userService.getToken(userDTO);
    }
}
