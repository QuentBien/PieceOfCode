package com.ajd.pieceOfCode.configuration;

import com.ajd.pieceOfCode.util.PieceOfCodeArgumentResolver;
import com.ajd.pieceOfCode.util.UserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final PieceOfCodeArgumentResolver pieceOfCodeArgumentResolver;
    private final UserArgumentResolver userArgumentResolver;

    @Autowired
    public WebMvcConfiguration(PieceOfCodeArgumentResolver pieceOfCodeArgumentResolver, UserArgumentResolver userArgumentResolver) {
        this.pieceOfCodeArgumentResolver = pieceOfCodeArgumentResolver;
        this.userArgumentResolver = userArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(pieceOfCodeArgumentResolver);
        argumentResolvers.add(userArgumentResolver);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }
}
