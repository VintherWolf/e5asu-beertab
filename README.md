# e5asu-beertab

Java Application "Beer Tab" is a demo application for keeping track of service per table and customer. The main asset to keep track of is  beverages per table 


The purpose of this application is to demonstrate the use of JavaFX Preloader and App., Database TCP/IP Connection with Postgresql in Docker Swarm, Java Multi Threading, using different design patterns. 


### Dependencies
- Docker (Docker swarm is used for this implementation)
- Postgresql JDBC driver


### Start Docker Swarm Postgresql Database Server

#### Preparation:

1) Generate environment file for the Database:

- filename:  .env.database
- location:  ./dbserver (local to stack.yml)
###### With content:
- *POSTGRES_USER=desired_username*
- *POSTGRES_PASSWORD=desired_password*

2) Generate Database Volume:

`# docker volume create db-storage`
#### Start Database Instance

`# docker stack deploy -c stack.yml postgres`

postgresql instance will then run at localhost:8181

#### Post-Start Alignments: Configure Database

1) Create database using psql

`# CREATE DATABASE beertab;`
Test: `# \l`'


2) Connect to beertab database:

`# \connect beertab`


2) Create Table if not exists in beertab

`# CREATE TABLE IF NOT EXISTS customertable(customerId serial PRIMARY KEY,
ctable INT,
Customer VARCHAR(10),
Beverage VARCHAR(10),
quantity INT,
cost INT
);`

3. Insert Data into Customertable

`# INSERT INTO customertable(
ctable, Customer, Beverage, quantity, cost)`

`# VALUES(1, 'John Doe', 'IPA', 3, 50);`

Test: Expected to return: **INSERT 0 1**

4: Check Data is Present in Table:


`# SELECT * FROM customertable;`

