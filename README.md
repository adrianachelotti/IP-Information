# IP-Information

## To run locally:

 - Generate the jar file:
` mvn package`
 -  Build the image
`docker build -t api-docker-ip-information:latest .`
- Run with docker compose
`docker-compose up`

## To run with the IDE:

 - Run Mongo db with docker :
`docker run --name api-database -p 27018:27017 mongo:latest`
 - Run jar
 ` java -jar target/ip-informationXXXXXX.jar -Dspring.data.mongodb.host=localhost -Dspring.data.mongodb.port=27018`

## APIs Documentation in Swagger 

http://localhost:9090/ip-information/swagger-ui.html
