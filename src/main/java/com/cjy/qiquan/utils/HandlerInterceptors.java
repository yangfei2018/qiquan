package com.cjy.qiquan.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.servlet.HandlerInterceptor;

@Documented  
@Target({ElementType.TYPE})  
@Retention(RetentionPolicy.RUNTIME) 
public @interface HandlerInterceptors {
	Class<? extends HandlerInterceptor>[] value();  
}
