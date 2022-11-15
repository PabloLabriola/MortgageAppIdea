# Introduction
Performance project to performe load testing.

# Getting Started
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

6. Download Apache JMeter version 5.4.1

```


# Launch JMX script

To run the load tests, the services need to be deployed first.
Once the system is up and running test can be run:

```
1. Inside the JMETER directory, open \apache-jmeter-5.4.1\bin\jmeter

2. File --> open --> performance-tests/performance-tests.jmx

3. Click the button start to launch load tests.

4. If everything is ok, in the node "View Results Tree", you will see the results of the requests launched, and in the node "Summary report" you will see response times.
```
