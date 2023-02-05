# gli-technical-test

## General Info, this project using :
1. Java JDK 11,
2. Java Spring Boot 2.7.9-SNAPSHOT version,
3. Maven.

## Library :
1. Spring Web,
2. Spring Boot DevTools,
3. Lombok,
4. Spring Data JPA,
5. H2 Database,
6. Webflux / Webclient,
7. Gson,
8. JUnit 5.

## Rules :
1. Import the postman collection from postman-collection folder in this project to your postman.

2. mvn spring-boot:run : Use this script for running this project using CMD / Terminal.

3. mvn clean test : Use this script for running unit testing this project using CMD / Terminal.

4. Type this URL in your brower http://localhost:8080/h2-console/login.jsp , for check the H2 database.
   I don't provide the script, because ddl-auto, I setting create-drop, so when the service running, it Will create table automaticlly.


## I provide some services:
1. Create Data Dog (localhost:8080/api/createDogData)
   - This API I provide for create data and fetching data from dog.ceo and than save into H2 db,
   
2. Get All Data (localhost:8080/api/getDogDataListFromDb)
   - This API I provide for fetching all data from the H2 database

3. Get Data By Dog Name (localhost:8080/api/getDogDataByName?dogName=terrier)
   - This API I provide for fetching spesific data by dog name.
  
4. Delete By Dog Name (localhost:8080/api/deleteDogDataByName?dogName=sheepdog)
   - This API I provide for delete data from the database.
   
 5. Update Data by DogName(localhost:8080/api/updateDogImage?oldDogName=terrier&newDogName=shiba)
   - This API I provide for update data in H2 DB.  
 
  
