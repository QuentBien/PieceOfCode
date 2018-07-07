package com.ajd.pieceOfCode.util;

import com.ajd.pieceOfCode.user.User;
import com.ajd.pieceOfCode.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private static Optional<User> authenticatedUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof User)
                .map(principal -> (User) principal);
    }

    private final UserRepository userRepository;

    @Autowired
    public UserArgumentResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return User.class.equals(parameter.getParameterType());
    }

    @Override
    public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                WebDataBinderFactory binderFactory) {
        return getOptionalAuthenticatedUser().orElse(null);
    }

    private Optional<User> getOptionalAuthenticatedUser() {
        return authenticatedUser().map(User::getId).map(id -> userRepository.findById(id).orElseThrow(NotFoundException::new));
    }

}
