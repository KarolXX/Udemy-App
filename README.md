# Udemy
Application with courses - something similar to Udemy.

[Frontend for this app](https://github.com/KarolXX/Udemy-react)

## Techologies:
* Java v. 11
* Spring Boot v. 2.5.2
* JPA - Hibernate
* Maven
* Keycloak 15.0.2 (as an external authentication system but hashed sensitive user information are also stored in MySQL db in the event of opting out of the external system)
* Flyway
* MySQL as database for app, h2 for database in tests
* Model mapper v. 2.4.5

## Interesting concepts:
* JPQL
* events
* work with the `org.keycloak.admin.client.Keycloak`

# Brief app description
## Scheme operation:
![image](https://user-images.githubusercontent.com/71709330/172498398-c3f4eafc-cb24-4894-a6ec-297151317389.png)

## Start page
Login and registration requests are send to keycloak, from where we get TokenData (project class).
In addition to keeping users in LDAP built into Keycloak, they are also placed in the MySQL database
![image](https://user-images.githubusercontent.com/71709330/163377719-39ec7303-57eb-497d-89a8-9883098efe5f.png)

After registration you get roles specific to your account - user or author

## Menu
Menu is ... just menu - place where we can choose which course we want to see in detail.

The order of the courses is not random but depends on the course `sequence` property.

To avoid flow of unnecessary data I use CourseInMenu DTO when fetching courses.
Of course I use paging so as not to store all the data in main memory (easy and cheap memory allocation).
![image](https://user-images.githubusercontent.com/71709330/163381342-09b06988-a6ed-48e4-9fec-bf327323a7df.png)

## Course
The user who purchased course can rate, comment and watch the video instead of just seeing a picture of the course as a user who did not buy the course.
Everyone can add a course to favorites and view them later in favourite-courses tab.
![image](https://user-images.githubusercontent.com/71709330/163387792-a0254ec4-7099-4b87-ad52-1d00a071f5df.png)
![image](https://user-images.githubusercontent.com/71709330/163387462-9d20e5bc-70a0-49f5-b662-35fddf421f83.png)



# Lacks in app:
* temporary inability to log in as an author in the application
* no security by roles
* a few unresolved fixme statements
* no tests yet


