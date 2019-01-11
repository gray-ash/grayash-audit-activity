package com.grayash.auditactivity.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.commons.util.SpringFactoryImportSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.grayash.auditactivity.aspect.AuditActivityAspect;
import com.grayash.auditactivity.service.MessageSenderService;

import lombok.Data;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass=true)
@Data
public class AuditActivityAspectConfig extends SpringFactoryImportSelector<EnableAuditActivity>{
	
	private static final Logger Log = LoggerFactory.getLogger(AuditActivityAspectConfig.class);
	
	
	private String serviceName;
	
	@Bean
	AuditActivityAspect auditActivityAspect() {
		return new AuditActivityAspect(this);
	}

	
	@Bean
	MessageSenderService messageSenderService() {
		return new MessageSenderService();
	}


	@Override
	protected boolean isEnabled() {
		return true;
	}
	
	@Override
	public String[] selectImports(AnnotationMetadata metadata) {
		String[] imports = super.selectImports(metadata);

		AnnotationAttributes attributes = AnnotationAttributes.fromMap(
				metadata.getAnnotationAttributes(getAnnotationClass().getName(), true));
		serviceName = attributes.getString("serviceName");
		if(Log.isDebugEnabled())
			Log.debug("ServiceName:::"+serviceName);
		return imports;
	}
}
