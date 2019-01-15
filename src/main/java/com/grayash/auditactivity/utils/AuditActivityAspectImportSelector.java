package com.grayash.auditactivity.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;

import com.grayash.auditactivity.aspect.AuditActivityAspect;
import com.grayash.auditactivity.config.EnableAuditActivity;

import lombok.Data;
import lombok.ToString;



@Data
@ToString
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class AuditActivityAspectImportSelector implements ImportSelector {

	private static final Logger Log = LoggerFactory.getLogger(AuditActivityAspectImportSelector.class);

	public String serviceName;


	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		AnnotationAttributes attributes = AnnotationAttributes
				.fromMap(importingClassMetadata.getAnnotationAttributes(EnableAuditActivity.class.getName(), false));
		String serviceName = attributes.getString("serviceName");
		Log.debug("ServiceName::"+serviceName);
		BaseProfile.getInstance().setProperty("grayash-aspect-serviceName", serviceName);
		return new String[] { "com.grayash.auditactivity.aspect.AuditActivityAspect" };
	}
	
	
	
}


