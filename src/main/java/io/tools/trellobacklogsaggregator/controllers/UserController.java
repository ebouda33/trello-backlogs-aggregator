package io.tools.trellobacklogsaggregator.controllers;

import io.tools.trellobacklogsaggregator.model.UserModel;
import io.tools.trellobacklogsaggregator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public List<UserModel> getAll(){
        return userRepository.findAll();
    }

}
