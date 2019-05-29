package com.acme.ex3.controller;


import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import com.acme.ex3.exception.ApplicationException;

@ControllerAdvice
public class MyControllerAdvice {


	@ExceptionHandler(ApplicationException.class)
	public String onApplicationException(ApplicationException e, Model model) {
		model.addAttribute("exception", e);
		return "_errors/application-exception";
	}
/*	
	@ExceptionHandler(AccessDeniedException.class)
	public String onApplicationException() {
		return "_errors/security-exception";
	}
*/
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
