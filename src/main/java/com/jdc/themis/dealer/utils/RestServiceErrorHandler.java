package com.jdc.themis.dealer.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Method marked by this annotation would check whether there is expected exception thrown from the method. 
 * If there is, it will send the alert message. 
 * 
 * @author chen386_2000
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RestServiceErrorHandler {

}
