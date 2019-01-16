package com.grayash.auditactivity.model;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class ActivityData implements Serializable{
	
	private String customerId;
	private String requestUrl;
	private ActivityType activityType;
	private String activityData;
	private String responseCode;
	private String exception;
	private String os;
	private String osVersion;
	private String isp;
	private String ip;
	private String serviceName;
	private String spanId;
	private String traceId;
	

}
