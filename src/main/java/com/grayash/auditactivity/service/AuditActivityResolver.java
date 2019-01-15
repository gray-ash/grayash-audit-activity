package com.grayash.auditactivity.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.grayash.auditactivity.config.EnableAuditActivity;

public class AuditActivityResolver implements HandlerMethodArgumentResolver {
	
	private static final Logger Log = LoggerFactory.getLogger(AuditActivityResolver.class);

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		EnableAuditActivity attr = parameter.getParameterAnnotation(EnableAuditActivity.class);
        if(Log.isDebugEnabled()) {
        	Log.debug("Service Name::"+attr.serviceName());
        }
		return attr != null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		EnableAuditActivity attr = parameter.getParameterAnnotation(EnableAuditActivity.class);
        if(Log.isDebugEnabled()) {
        	Log.debug("Service Name::"+attr.serviceName());
        }
		return null;
	}

}
