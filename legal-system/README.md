# Introduction 
The legal-system microservice will receive a notification from the mortgage-feasibility-validator microservice when a mortgage is validated, and will persist this info in a database.

# Getting Started
Guide to get your code up and running on your system.
1.	Installation process:
    - Install Java JDK11, Maven
    - Clone repository: 

2.	Software dependencies:
    - RabbitMQ
    - H2 database
            spring.datasource.url=jdbc:h2:mem:legal_system_db;
            spring.datasource.username=sa
            spring.datasource.password=password
            spring.h2.console.path=/h2-console
    - Docker



# Build and Test
You can execute `mvn clean test`


## Deployment
Follow the deployment guide on the orchestrator readme.






