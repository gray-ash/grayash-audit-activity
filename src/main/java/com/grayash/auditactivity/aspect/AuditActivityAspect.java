package com.grayash.auditactivity.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.grayash.auditactivity.exception.PCRuntimeException;
import com.grayash.auditactivity.model.ActivityData;
import com.grayash.auditactivity.model.ActivityType;
import com.grayash.auditactivity.service.MessageSenderService;
import com.grayash.auditactivity.utils.BaseProfile;
import com.grayash.auditactivity.utils.CommonUtils;

@SuppressWarnings("Duplicates")
@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Component
public class AuditActivityAspect {

	private static final Logger Log = LoggerFactory.getLogger(AuditActivityAspect.class);
	
	
	@Bean
	public MessageSenderService messageSenderService() {
		return new MessageSenderService();
	}

	@AfterReturning(pointcut = "execution(* com.grayash..controller..handleGlobalException(..))", returning = "result")
	public void auditException(JoinPoint joinPoint, Object result) {
		try {
			String serviceName = (String) BaseProfile.getInstance().getProperty("grayash-aspect-serviceName");
			Object[] arr = joinPoint.getArgs();
			PCRuntimeException exception = null;
			ServletWebRequest str = null;
			for (Object obj : arr) {
				if (obj instanceof PCRuntimeException)
					exception = (PCRuntimeException) obj;
				else if (obj instanceof ServletWebRequest)
					str = (ServletWebRequest) obj;
			}
			ResponseEntity entity = (ResponseEntity) result;
			String requestUrl = str.getRequest().getRequestURI();
			ActivityData logData = new ActivityData();
			logData.setActivityData(CommonUtils.constructJsonResponse(entity.getBody()));
			logData.setActivityType(ActivityType.EXCEPTION);
			logData.setRequestUrl(requestUrl);
			logData.setServiceName(serviceName);
			logData.setSpanId(MDC.get("spanId"));
			logData.setTraceId(MDC.get("spanId"));
			logData.setException(exception.toString());
			logData.setResponseCode(entity.getStatusCode().toString());

			if (Log.isDebugEnabled())
				Log.debug("Activity Log for EXCEPTION::" + logData);
			messageSenderService().send(logData);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@AfterReturning(pointcut = "execution(* com.grayash..controller..*(..)) && !execution(* com.grayash..controller..handleGlobalException(..))", returning = "result")
	public void auditResponse(JoinPoint joinPoint, Object result)  {
		try {
			String serviceName = (String) BaseProfile.getInstance().getProperty("grayash-aspect-serviceName");
			Object[] arr = joinPoint.getArgs();
			HttpServletRequest servletRequest = null;
		
			for (Object obj : arr) {
				if (obj instanceof HttpServletRequest)
					servletRequest = (HttpServletRequest) obj;
			}
			ResponseEntity entity = (ResponseEntity) result;
			String requestUrl = servletRequest.getRequestURI().toString();
			String csid = servletRequest.getHeader("csid");
			ActivityData logData = new ActivityData();
			logData.setActivityData(CommonUtils.constructJsonResponse(entity.getBody()));
			logData.setActivityType(ActivityType.RESPONSE);
			logData.setCustomerId(csid);
			logData.setRequestUrl(requestUrl);
			logData.setServiceName(serviceName);
			logData.setSpanId(MDC.get("spanId"));
			logData.setTraceId(MDC.get("traceId"));
			logData.setResponseCode(entity.getStatusCode().toString());
			if (Log.isDebugEnabled())
				Log.debug("Activity Log for RESPONSE::" + logData);
			messageSenderService().send(logData);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Before("execution(* com.grayash..controller..*(..)) && !execution(* com.grayash..controller..handleGlobalException(..))")
	public void auditRequest(JoinPoint joinPoint)  {
		try {
			String serviceName = (String) BaseProfile.getInstance().getProperty("grayash-aspect-serviceName");
			Object[] arr = joinPoint.getArgs();
			String request = null;
			JSONObject cmnReq=null;
			HttpServletRequest servletRequest = null;
			HttpHeaders headers = null;
			for (Object obj : arr) {
				if (obj instanceof HttpServletRequest)
					servletRequest = (HttpServletRequest) obj;
				else if (obj instanceof HttpHeaders)
					headers = (HttpHeaders) obj;
				else {
					try {
						cmnReq = new JSONObject(obj);
						cmnReq = cmnReq.getJSONObject("appData");
						if(Log.isDebugEnabled())
							Log.debug("appData values are:::"+cmnReq);
					} catch (Exception e) {
						if(Log.isErrorEnabled())
							Log.error("Can not convert to CommonRequest"+e.getMessage());
					}
					request = CommonUtils.constructJsonResponse(obj);
				}
			}
			String requestUrl = servletRequest.getRequestURI().toString();
			String csid = servletRequest.getHeader("csid");
			ActivityData logData = new ActivityData();
			logData.setActivityData(request);
			logData.setActivityType(ActivityType.REQUEST);
			logData.setCustomerId(csid);
			logData.setRequestUrl(requestUrl);
			logData.setServiceName(serviceName);
			logData.setSpanId(MDC.get("spanId"));
			logData.setTraceId(MDC.get("traceId"));
			if(cmnReq!=null) {
				logData.setOs(cmnReq.get("os").toString());
				logData.setIsp(cmnReq.get("isp").toString());
				logData.setIp(cmnReq.get("ip").toString());
				logData.setOsVersion(cmnReq.get("osVersion").toString());
			}
			if (Log.isDebugEnabled())
				Log.debug("Activity Log for REQUEST::" + logData);
			messageSenderService().send(logData);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
