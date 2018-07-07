package com.ajd.pieceOfCode.user;

import com.ajd.pieceOfCode.util.ConflictException;
import com.ajd.pieceOfCode.util.View;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.NotFoundException;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@Transactional
@Validated
public class UserController {

    private final UserRepository userRepository;
    private final ObjectMapper mapper;

    @Autowired
    public UserController(UserRepository userRepository, ObjectMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    @JsonView(View.UserProfile.class)
    @RequestMapping(method = GET, value = "/user")
    @PreAuthorize("isAuthenticated()")
    public User getUser(User user) {
        if (user != null) {
            return user;
        }
        throw new NotFoundException();
    }

    @RequestMapping(method = POST, value = "/users/")
    @JsonView(View.UserProfile.class)
    @PreAuthorize("permitAll()")
    public User createUser(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ConflictException("There is already an account using this email address");
        }
        userRepository.save(user);
        return user;
    }

    @JsonView(View.UserProfile.class)
    @RequestMapping(method = PUT, value = "/user")
    @PreAuthorize("isAuthenticated()")
    public User updateUser(User principal, @RequestBody ObjectNode jsonUser) throws IOException {
        return mapper.readerForUpdating(principal).readValue(jsonUser);
    }
}