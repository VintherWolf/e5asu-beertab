# e5asu-beertab

Java Application "Beer Tab" is a demo application for keeping track of service per table and customer. The main asset to keep track of is  beverages per table 


The purpose of this application is to demonstrate the use of JavaFX Preloader and App., Database TCP/IP Connection with Postgresql in Docker Swarm, Java Multi Threading, using different design patterns. 


### Dependencies
- Docker (Docker swarm is used for this implementation)
- Postgresql JDBC driver


### Start Docker Swarm Postgresql Database Server

#### Preparation:
Generate environment file for the Database:

- filename:  .env.database
- location:  ./dbserver (local to stack.yml)
###### content:
POSTGRES_USER=desired_username

POSTGRES_PASSWORD=desired_password

#### Start Database Instance

''' # docker stack deploy -c stack.yml postgres
'''

postgresql instance will then run at localhost:8181
