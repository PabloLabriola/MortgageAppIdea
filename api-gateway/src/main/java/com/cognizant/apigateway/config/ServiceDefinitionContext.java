package com.cognizant.apigateway.config;

import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class ServiceDefinitionContext {

	private final ConcurrentHashMap<String, String> swaggerResources;
	
	public ServiceDefinitionContext() {
		this.swaggerResources = new ConcurrentHashMap<>();
	}
	public void addServiceDefinition(String name, String content) {
		swaggerResources.put(name, content);
	}

	public List<SwaggerResource> getSwaggerDefinitions() {
		
		return swaggerResources.entrySet().stream().map(service -> {
			SwaggerResource resource = new SwaggerResource();
				resource.setName(service.getKey());
				resource.setUrl("/service/"+service.getKey());
				resource.setSwaggerVersion("3.0.3");
				return resource;
		}).collect(Collectors.toList());
	}

	public String getServiceDefinition(String serviceName) {
		return swaggerResources.get(serviceName);
	}
}
