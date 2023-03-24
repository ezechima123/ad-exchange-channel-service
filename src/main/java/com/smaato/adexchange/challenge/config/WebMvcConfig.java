package com.smaato.adexchange.challenge.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.smaato.adexchange.challenge.interceptor.MdcInterceptor;

@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    MdcInterceptor mdcInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mdcInterceptor);
    }

}
