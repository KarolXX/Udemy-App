# Udemy
Application with courses - something similar to Udemy.

I wrote UI for this app using react: [Frontend for this app](https://github.com/KarolXX/Udemy-react)

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
* "Composition over inheritance"
* work with the `org.keycloak.admin.client.Keycloak`

# Brief app description
## Scheme operation:
![image](https://user-images.githubusercontent.com/71709330/172498398-c3f4eafc-cb24-4894-a6ec-297151317389.png)

## Start page
Login and registration requests are send to keycloak, from where we get access token wrapped into TokenData (project class).
In addition to keeping users in LDAP built into Keycloak, they are also placed in the MySQL database with password hashed by one-way function
![image](https://user-images.githubusercontent.com/71709330/163377719-39ec7303-57eb-497d-89a8-9883098efe5f.png)

After registration you get roles specific to your account - user or author

## Menu
Menu is ... just menu - place where we can choose which course we want to see in detail.

The order of the courses is not random but depends on the course `sequence` property which reflects the algorithm describing how good the course is - The more users, good ratings and promotions or just a low price a course has, the faster it will be displayed to the user

To avoid flow of unnecessary data I use CourseInMenu DTO when fetching courses.
Of course I use paging so as not to store all the data in main memory (easy and cheap memory allocation).
![image](https://user-images.githubusercontent.com/71709330/183313405-ce13cf71-b7ec-492b-903e-70da2714a887.png)

## Course
The user who purchased course can rate, comment and watch the video instead of just seeing a picture of the course as a user who did not buy the course.
Everyone can add a course to favorites and view them later in favourite-courses tab.
![image](https://user-images.githubusercontent.com/71709330/183313457-4a8e72eb-8f2f-4bfd-b87e-fd5baf1684df.png)
![image](https://user-images.githubusercontent.com/71709330/183313660-e42b31c1-2df4-499f-ab7d-52dee27c7f64.png)

In the course view you can also check what courses other participants have
![image](https://user-images.githubusercontent.com/71709330/183313533-05c7d671-f184-437b-b8ce-3f9d4eb8c30d.png)

All the files like course image, comment image, course video, user or author profile image are stored in one table in DB "app_files"


# Lacks in app:
* temporary inability to log in as an author in the application
* no security by roles
* A few unresolved FIXME statements
* no lombock yet

