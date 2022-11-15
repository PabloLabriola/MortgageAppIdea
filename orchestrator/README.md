# Introduction
This repository contains the docker-composer file with the docker container configuration for the Reference Architecture Project. Also, here you will find the necessary software for this project's onboarding, and instructions on how to test a use case scenario from the business logic.

# Necessary Software:
- IntelliJ - Last version from OneIt
- Docker Desktop version 3.3.1 from OneIt
- Maven - Download the .zip binaries from Google. Also, set the MAVEN_PATH in the Windows system variables and the path to the bin folder in the PATH variable.
- OpenJDK 11 from OneIt
- GIT - Last version from OneIt
- Postman - Last version from OneIt

# IntelliJ Configuration:


### 1. 	Download all the repositories
You can find them at https://github.com/PabloLabriola/MortgageAppIdea -> Import them into IntelliJ as modules
### 2.  Run the services on local
You can run any service on local adding this parameter to the VM options:

Run configuration -> Modify options -> Add VM Options
Add:
```-Dspring.profiles.active=local```

### 3.  Generate the target code to be used by docker:
clean install (with an optional -Dmaven.test.skip=true)

# Setting up docker on your device.
Once you have Docker Desktop up and running, these are the steps to create (and refresh) the images and pack them into a container:

### 1. Git pull the repositories
I would suggest pulling them from the IDE, so they end up stored on the IDE folder.

### 2. Generate the sources
Note: From now on, all the steps are necessary every time we want to refresh the docker images.
In order to generate the sources you have to **maven clean install** all the projects.

### 3. Rebuild the docker images
Execute the docker image build command on the root folder on the project specifying the corresponding image name (you can find them in the docker-compose.yml file).
Example commands:

```
cd C:\Users\932938\IdeaProjects\service-discovery
docker image build -t local/discovery .

cd C:\Users\932938\IdeaProjects\api-gateway
docker image build -t local/gateway .

cd C:\Users\932938\IdeaProjects\mortgage-feasibility-validator
docker image build -t local/feasibility .

cd C:\Users\932938\IdeaProjects\company-management
docker image build -t local/company .
```
The previously existing images with the same name will now be listed on Docker Desktop as "no name". There can be deleted.

### 4. Compose the container

Remove the existing container from Docker Desktop if existing and build another one executing the docker compose-up command on the orchestrator root folder:

```
cd C:\Users\932938\IdeaProjects\orchestrator
docker-compose up -d
```

After everything is up, you should be able to watch all the running images in Docker Desktop

#Links of interest
Links of interest in the localhost application:
- http://localhost:8761 = EUREKA
- http://localhost:9090 = PROMETHEUS
- http://localhost:3000 = GRAFANA - User: admin, Pass: admin
- http://localhost:16686 = JAEGER UI
- http://localhost:8760/swagger-ui/index.html = SWAGGER

 #Test the business logic

1. Go to http://localhost:8760/swagger-ui/index.html
2. Under the "companymanagement" definition, deploy the "auth-controller" requests.
3. Open the POST "auth/token" request. In order to find a valid company, please copy one from the "companies.txt" file located under the resources folder in "companymanagement" repository.
4. Press "Try it out" and change the "string" parameter for the valid company name you copied from the previous file. Example:
   ```
   {
   "companyName": "CHOAM"
   }
   ```
5. Press execute and copy the token you just received for the next steps. Example:
    ```   
    Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDSE9BTSIsImlhdCI6MTYyMzc1Mjc0NSwiZXhwIjoxNjIzNzU2MzQ1fQ.k_iVPxgnel6OpKSXvOOwhJb0P2OUudpXJJO9T5g1vPjCRV2Hrp377pV-ieMD48wdcEmZmodoMT6qpOkZrXQdAw
    ```
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
