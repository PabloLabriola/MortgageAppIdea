package com.cognizant.apigateway.controller;

import com.cognizant.apigateway.config.ServiceDefinitionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
public class ServiceDefinitionController {
	
	@Autowired
	ServiceDefinitionContext context;

	@GetMapping("/service/{serviceName}")
	public String getServiceDefinition(@PathVariable("serviceName") String serviceName) {
		return context.getServiceDefinition(serviceName);
	}
}
