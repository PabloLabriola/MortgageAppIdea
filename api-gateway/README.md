# Api Gateway Microservice

Microservice that sits between the client and the collection of the rest of backend microservices of the project, adding a layer of security.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

Software you will need to install.

```
1. Install Java JDK11

2. Clone repository: https://github.com/PabloLabriola/MortgageAppIdea

3. Apache Maven package and variable environment

	- MAVEN_HOME=C:\{your_system_route}\apache-maven-x.x.x
	- %MAVEN_HOME%\bin

4. Check your Maven config: mvn --version

5. Install Docker

```

### Installation

```
1. Go inside the api-gateway folder in your system.

2. Open CMD window.

3. Type: mvn clean install

4. If everything is ok you will get a .jar file inside target folder.
```

## Prometheus metrics access

The URL were Prometheus metrics are shown is the following:

<http://localhost:8760/mortgagefeasibility/actuator/prometheus>




The metrics can also be viewed in Prometheus dashboard navigating to:

(Prometheus server must be up)

<http://localhost:9090>


## Grafana

To visualize this metrics in custom graphs you can navigate to:

(Grafana server must be up)

<http://localhost:3000>

The login credentials are:
```
User: admin
Password: admin
```

To connect to Prometheus datasource:

1. Go to <http://localhost:3000/datasources>

2. Click on Add data source

3. Select Prometheus Data Source

4. Fill up the following data and save & test

```
URL: http://localhost:9090
Access: Browser
```

## Jaeger access (tracing)

The URL were services traces are shown is the following:

(Jaeger server must be up)

<http://localhost:16686/>



## Deployment

Open CMD window and type:

1. Generate docker image: docker image build -t local/gateway:latest .

2. Get your [image_id]: docker images

3. Deploy the container image based with the rest of microservices: docker-compose up

4. Check in your browser if the instance is registered with Eureka: http://localhost:8761/


## H2 database notes

To see the h2 console go to http://localhost:8760/mortgagefeasibility/h2-console/


## Versioning
TODO
We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags).
