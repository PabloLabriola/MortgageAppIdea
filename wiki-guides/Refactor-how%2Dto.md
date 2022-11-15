**Microservices structure**

- src/main/java
	- exceptions
	- controllers
	- services
		- impl
	- repositories
		- impl
	- infrastructure
		- rabbitmq
			- config
			- “classes”
	- model
		- dtos
		- entities
		- enums
		- mappers
	- security	
		- config
		- “classes”
	- swagger
		- config
		- “classes”
- src/test       
	- unit
	- acceptance
	- integration

Notes:

- Inside services top level there will be interfaces. In impl there will be classes implementing each service interface. Same for controllers and repositories.

- Inside enum files will end with Type.
	
**Java Code**	

- Use logs in controllers and exceptions. Use logs instead of system.out.print
- Make sure we are logging exceptions.
- Correct code indentation
- Descriptive and concrete errors/exceptions descriptions with all information needed.
- Use Lombok
- Split large methods
- Combine constants (ex: Constants.java FEASIBILTY_URI use VALIDATE)
- Review unused imports
- Use build annotation
- Check sonarlint
- Avoid * in imports
- Restrict and specify methods and fields visibility
- Update readme
- Only document code logic not clear (before documenting try to avoid doing it doing clearer code)
- Split services logic (ex: bdd, rabbit, response generation) (ex: ConfirmationServiceImpl generateconfirmresponse )
- Use camelCase (not in constants)
- Constants in capitals.
- Move common configs to config-data (ej: config-server port) 
- Delete commented code / properties (ej: application.yml config-server)
- Delete unused variables
- More descriptive variable names, classes and methods
- Move string literals to properties
- Avoid general names on classes (ex: FeasiblityService)
- Split long lines, methods and classes
- Use SOLID PRINCIPLES
- Use CLEAN CODE
- Check swagger exists for all endpoints and they are documented

**Config server**
- Split profiles
- Split files for topic/microservice

**Test**
- Check Test without nothing up
- Coverage
- More descriptive test method naming
- Acceptance TEST split?
- Local properties for integration test:
       Use this parameter with maven: clean install -Dspring.cloud.config.enabled=false

**Maven**
- POM check UNUSED DEPENDECIES


**DOCKERFILE**

**git ignore (iml files, target.., mvn files)**

