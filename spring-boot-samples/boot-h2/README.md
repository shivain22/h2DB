Spring Boot H2 Database 
------------

INTRODUCTION
------------
Sample Spring-boot with h2 Database

REQUIREMENTS
------------
JDK 1.8+, Maven 3.2+ 

INSTALLATION
------------
Download project import as a maven project 
Run spring tr.com.tr.com.jowl.App main class

H2
------------
#### Embedded
```spring.datasource.url=jdbc:h2:file:./yourdbname;DB_CLOSE_ON_EXIT=FALSE```
#### In-Memory
```spring.datasource.url=jdbc:h2:mem:yourdbname;DB_CLOSE_ON_EXIT=FALSE```
#### Enable h2 db console 
```spring.h2.console.enabled=true```

```spring.h2.console.path=/h2```

[http://localhost:8080/h2/](http://localhost:8080/h2/)

#### Automatic Mixed Mode
Multiple processes can access the same database without having to start the server manually. 
To do that, append ;AUTO_SERVER=TRUE to the database URL. 
You can use the same database URL independent of whether the database is already open or not. 
This feature doesn't work with in-memory databases.

Example database URL:

```spring.datasource.url=jdbc:h2:file:./yourdbname;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE;```

#### CREATE  your own SCHEMA

```spring.datasource.url=jdbc:h2:file:./yourdbname;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE;INIT=CREATE SCHEMA IF NOT EXISTS JOWL```

#### Watch Courses
[![spring-boot-h2](https://img.youtube.com/vi/xkGApfiqELE/0.jpg)](https://youtu.be/xkGApfiqELE)


[ibrahim karayel](https://www.linkedin.com/in/ibrahimkarayel/)
