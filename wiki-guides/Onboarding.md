# **Reference Architecture Project - ONBOARDING**

##**0. 	Necessary Software:**
- IntelliJ - Latest version from OneIt
- Docker Desktop version 3.3.1 from OneIt
- Maven - Download the .zip binaries from Google
- Also, set the MAVEN_PATH in the Windows system variables and the path to the bin folder in the PATH variable.
- OpenJDK 11 from OneIt
- GIT - Latest version from OneIt
- Postman - Latest version from OneIt

##**1.a) Project configuration by script**
[Here](/Init-Script) the documentation to use a init script.

##**1.b) Project configuration manually**
1. 	Download all the repos https://github.com/PabloLabriola/MortgageAppIdea -> Import them into IntelliJ as modules 
2.  Set up maven configurations for all of them -> clean install (with -Dmaven.test.skip=true in VM Options in order to skip the tests **untill fixed)**
3.  Launch every clean install previously configured
4. 	Open a cmd prompt, and build the docker images one by one with this command: "docker image build -t local/[imageName]:latest ." *The imageName can be found in the "docker-compose.yml" file, in the "orchestrator" repository. You can identify them after the command "image: local/".

5. 	For creating each Docker image, you have to go to each module and then open console in the root path. Docker images are based on the Dockerfile that is located in each project path.

6.  Go to the "orchestrator" repository folder, and launch a cmd with the command "docker-compose up"
7.  After everything is up, you should be able to watch all the running images in Docker Desktop
8.  Links of interest in the localhost application:
	- http://localhost:8761 = EUREKA
	- http://localhost:9090 = PROMETHEUS
	- http://localhost:3000 = GRAFANA - User: admin, Pass: admin
	- http://localhost:16686 = JAEGER UI
	- http://localhost:8760/swagger-ui/index.html = SWAGGER


##**2. Test the business logic:**

1. Go to http://localhost:8760/swagger-ui/index.html
2. Under the companymanagement definition, deploy the auth-controller requests.
3. Open the POST auth/token request. In order to find a valid company, please copy one from the "companies.txt" file located under the resources folder in "companymanagement" repository.
4. Press "Try it out" and change the "string" parameter for the valid company name you copied from the previous file. Example:

```
{
    "companyName": "CHOAM"
}
```

5. Press execute and copy the token you just received for the next steps. Example:
	`Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDSE9BTSIsImlhdCI6MTYyMzc1Mjc0NSwiZXhwIjoxNjIzNzU2MzQ1fQ.k_iVPxgnel6OpKSXvOOwhJb0P2OUudpXJJO9T5g1vPjCRV2Hrp377pV-ieMD48wdcEmZmodoMT6qpOkZrXQdAw`
6. Click on the "Authorize" green button, paste the token in the "Value" input box and press Authorize. Now you are authorized.
7. Switch the definition (top right corner of the Swagger interface) to "mortgagefeasibility".
8. We are going to validate the mortgage. For that, chose the "/validate" endpoint, press "Try it out" and insert the following information in the body:


```
        {
		"city": "string",
		"company": "string",
		"country": "string",
		"downPayment": 810,
		"homePrice": 9999.9,
		"loanLength": 0,
		"monthlyIncomes": 100.1
	}
```

9. Press execute and copy one of the two mortgage ids from the response.
10. We are going to open the "/confirm" endpoint and paste the copied id.

##**Miro board and credentials (for scrum masters):**

https://miro.com/welcome/M2JxaFg1c085VjdzVjhlOWNjOFZnM0o3blN6VXg2a0t1ajcyMUpyWXphanlIbXVGTTBHeU5NVnY2eXFQME9GMHwzMDc0NDU3MzYwNzE4OTgyNTAz

Mail: referencearchitecturecog@gmail.com
Pass: refarchcog_1107
