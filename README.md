# Asset Manager

### Spring boot application to:
 - Upload assets
 - List uploaded assets
 - Archive assets 

### Requirements
- JDK 1.8
- Maven 3
- Mongo 3.6


### Testing the application:
At the project root level, run:
```mvn test```

### Configurations:


### To run tests and build:
At the project root level, run:
```mvn clean install```

### To run locally:
1. Install mongo 3.6
2. Specify the following properties in *src/main/resources/application.properties*
    - aws.access.key.id={AWS_ACCESS_KEY}
    - aws.access.key.secret={AWS_SECRET_KEY}
    - aws.s3.bucket.name={AWS_BUCKET_NAME}
    - aws.region={AWS_REGION}
3. 
    ##### At the project root level run:
    ```mvn spring-boot:run```

    OR

   ##### At the project root level:
    build: ```mvn clean install```  
    run: ```./target/assetmanager.jar``` OR ```java -jar target/assetmanager.jar```


### To run on container:
##### Ensure **docker** and **docker-compose** is installed on the system
- Add AWS details in *docker-compose.yml* file with
  - AWS_ACCESS_KEY
  - AWS_SECRET_KEY
  - AWS_BUCKET
  - AWS_REGION
##### At the project root level, run command:
```docker-compose up```

### To deploy on an instance
1. Ensure mongo service running and java 8 installed
2. Copy the assetmanager.jar from target to the directory where you want to deploy
3. run ```java -jar -Dspring.data.mongodb.database={db_name} -Dspring.data.mongodb.host={mongo_host} -Dspring.data.momgodb.port={mongo_port} -DAWS_ACCESS_KEY={your_access_key} -DAWS_SECRET_KEY={your_secret_key} -DAWS_BUCKET={bucket_name} -DAWS_REGION={aws_region_name} assetmanager.jar >> assetmanager.log &```


### api to run health check for the application
```http://host:port/actuator/health```

## To test api-s, swagger is integrated which is accessible on
```http://host:port/swagger-ui.html```