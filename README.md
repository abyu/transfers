Transfers
=========

 - API to transfer money between accounts
 

### Setup

 - Run `./gradlew build` to compile, run tests and builds a jar file at `build/libs/transfers.jar`
 - Run `./gradlew run` to run the application, it should be available at [localhost:8080](http://localhost:8080)

### Running API Tests
 - Running API tests needs the server to be running in the background, run the app at port 8080
 - Run `./gradlew end2endTest` to run all api tests
 
### Running the APP from an executable jar
 - Run `./gradlew clean build` to build the jar at `build/libs/transfers.jar`
 - Run `java -jar build/libs/transfers.jar` to run the app at port 8080
 - Use env variable `APP_PORT` to specify the port
    - `APP_PORT=8081 java -jar build/libs/transfers.jar` runs the app at port 8081