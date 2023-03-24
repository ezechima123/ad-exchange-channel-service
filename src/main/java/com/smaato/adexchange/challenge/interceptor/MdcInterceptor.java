package com.smaato.adexchange.challenge.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class MdcInterceptor implements HandlerInterceptor {

    private static final Logger log = LogManager.getLogger(MdcInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        MDC.put("transactionId", getCorrelationId(request));
        log.info("transactionId =>" + MDC.get("transactionId"));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        MDC.remove("transactionId");
    }

    private String getCorrelationId(HttpServletRequest request) {
        String id = request.getParameter("id");
        if (id != null && !id.isEmpty()) {
            return id;
        }
        return UUID.randomUUID().toString();
    }
}