# Company Management Service

Service that ensure only registered companies can use the platform, to avoid access to any non subscribed company.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

Software you will need to install.

```
1. Install Java JDK11

2. Clone repository: https://github.com/PabloLabriola/MortgageAppIdea

3. Apache Mavem package and variable environment

	- MAVEN_HOME=C:\{your_system_route}\apache-maven-x.x.x
	- %MAVEN_HOME%\bin

4. Check your Maven config: mvn --version

5. Install Docker

```

### Installation

```
1. Inside the company-management folder

2. open CMD window

3. mvn clean install

4. If evething is ok you will get a .jar file inside target folder.
```

## Prometheus metrics access

The URL were Prometheus metrics are shown is the following:

<http://localhost:8760/companymanagement/actuator/prometheus>




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


## Running tests

You can execute `mvn clean test`


## Deployment

Open CMD window and type:

1. Generate docker image: docker image build -t local/company:latest .

2. Get your [image_id]: docker images

3. Deploy the container image based: docker run -p 8080:8080 --name companymanagement [image_id]

4. Check in your browser:

http://127.0.0.1:8081/companymanagement/1.0.0/companies/Virtucon

```
{"code":"200","description":"COMPANY SUBSCRIBE"}
```


## Versioning
TODO
We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags).
