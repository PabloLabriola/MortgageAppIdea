# Mortgage Feasibility Validator Service

Service that sends to a 3rd party the mortgage feasibility study result from its provided customer data.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

Software you will need to install.

```
1. Install Java JDK11

2. Clone repository: https://898832@dev.azure.com/898832/Reference%20Architecture/_git/mortgage-feasibility-validator

3. Apache Maven package and variable environment 

	- MAVEN_HOME=C:\{your_system_route}\apache-maven-x.x.x
	- %MAVEN_HOME%\bin

4. Check your Maven config: mvn --version

5. Install Docker

```

## Deployment

1. Follow the deployment guide on the orchestrator readme.

2. Check the Swagger on your browser:

http://localhost:8760/swagger-ui/index.html


## Prometheus metrics access

The URL were Prometheus metrics are shown is the following:

<http://localhost:8760/mortgagefeasibility/actuator/prometheus>


The metrics can also be viewed in Prometheus dashboard navigating to:

(Prometheus server must be up)

<http://localhost:9090>

To connect to Prometheus datasource:

1. Go to <http://localhost:3000/datasources>

2. Click on Add data source

3. Select Prometheus Data Source

4. Fill up the following data and save & test

```
URL: http://localhost:9090
Access: Browser
```


## Grafana

To visualize this metrics in custom graphs you can navigate to:

(Grafana server must be up)

<http://localhost:3000>

The login credentials are:
```
User: admin
Password: admin
```

## Database access

The H2 access url is the following:
<localhost:8760/mortgagefeasibility/h2-console/login.jsp>

The connection parameters are, as specified on the aplication.properties file, the following ones:
```
Datasource URL: jdbc:h2:mem:testdb
User: sa
Password: password
```

## Jaeger access (tracing)

The URL were services traces are shown is the following:

(Jaeger server must be up)

<http://localhost:16686/>


## Running tests

You can execute `mvn clean test -Dspring-boot.run.profiles=local`