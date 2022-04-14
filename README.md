# Udemy
Application with courses - something similar to Udemy.
[Frontend for this app](https://github.com/KarolXX/Udemy-react)

## Techologies:
* Java v. 11
* Spring Boot v. 2.5.2
* Maven
* Keycloak 15.0.2
* Flyway
* MySQL as database for app, h2 for database it tests
* Model mapper v. 2.4.5

## Interesting concepts:
* JPQL
* events
* work with the `org.keycloak.admin.client.Keycloak`

### Start page
Login and registration requests are send to keycloak, from where we get TokenData (project class).
In addition to keeping users in LDAP built into Keycloak, they are also placed in the MySQL database
![image](https://user-images.githubusercontent.com/71709330/163377719-39ec7303-57eb-497d-89a8-9883098efe5f.png)

After registration you get roles specific to your account - user or author

### Menu
To avoid flow of unnecessary data I use CourseInMenu DTO when fetching courses.
Of course I use paging so as not to store all the data in main memory (easy and cheap memory allocation)
![image](https://user-images.githubusercontent.com/71709330/163381342-09b06988-a6ed-48e4-9fec-bf327323a7df.png)

### Course
The user who purchased course can rate, comment and watch the video instead of the image as a user who did not buy the course
![image](https://user-images.githubusercontent.com/71709330/163387427-413ce79a-f189-4009-92b3-1231ef04764f.png)
![image](https://user-images.githubusercontent.com/71709330/163387462-9d20e5bc-70a0-49f5-b662-35fddf421f83.png)



# Lacks in app:
* no secure saving of passwords in MySQL
* if a customer signs up as an author, he don't get the appropriate role - the logic responsible for this should work for both the user and the author, but actually only works for the user (OAuthService register method)
* no security by roles


