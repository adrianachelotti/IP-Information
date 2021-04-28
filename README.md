# IP-Information
To run locally:

### Generate the jar file 
`mvn package`
### Build the image
`docker build -t api-docker-ip-information:latest .`
### Run with docker
`docker run -d --name ip-information-rest -p 9090:8080 api-docker-ip-information:latest`


## Script

`java -jar target/ip-informationXXXXXX.jar -Djdk.tls.client.protocols=TLSv1.2`

## APIs in Swagger url: 

- http://localhost:9090/ip-information/swagger-ui.html

