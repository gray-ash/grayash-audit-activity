package com.grayash.auditactivity.model;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class AppData implements Serializable{
	
	private String os;
	private String osVersion;
	private String isp;
	private String ip;
	private String customerId;
	

}
