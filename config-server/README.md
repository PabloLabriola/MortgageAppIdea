# Config Server Service

Service that share the GIT repository configuration with the service clients.

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
1. Inside the config-server folder

2. open CMD window

3. mvn clean install

4. If evething is ok you will get a .jar file inside target folder.
```

## Deployment

Open CMD window and type:

1. Generate docker image: docker image build -t local/config:latest .

2. Get your [image_id]: docker images

3. Deploy the container image based: docker run -p 8888:8888 --name config [image_id]

4. Check in your browser:
http://localhost:8888/actuator/health
