package com.grayash.auditactivity.config;



import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.grayash.auditactivity.utils.AuditActivityAspectImportSelector;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AuditActivityAspectImportSelector.class)
public @interface EnableAuditActivity {
	
	String serviceName() default "";

}
