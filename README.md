# web-challenge
All the challenge requirements have been implemented,the user can sign up & sign in using email and password (spring security), 
thanks to gustavoponce7 :https://github.com/gustavoponce7/spring-login it was really a time saving 

Configuration : edit 'application.properties'

Optional to read :

- there is a many2many relationship between the user and the shop with extra attributes like 'like,dislike' so i had to create
  a new entity Shop_User

- the distance depends on both the user and the shop ,for each user we have to provide new distance from the shop 
(Geolocation), it's too troublesome ^^ ,to keep it simple i choose the distance to be fixed and depends only on the shop 
(when creating a shop we assign to it a distance in miles, it means that all the users are at the same distance from the shop)
 
- used technologies : 
    - Spring Boot v2.0.4.RELEASE
    - Thymeleaf 
    - Spring security

Screenhsots :
![Settings Window](https://github.com/fou65ad/web-coding-challenge/blob/master/screenshots/Screenshot%20from%202018-10-10%2023-42-22.png)

![Settings Window](https://github.com/fou65ad/web-coding-challenge/blob/master/screenshots/Screenshot_2018-10-11%20Registration%20Form.png)

![Settings Window](https://github.com/fou65ad/web-coding-challenge/blob/master/screenshots/Screenshot_2018-10-10%20Admin%20Page.png)

![Settings Window](https://github.com/fou65ad/web-coding-challenge/blob/master/screenshots/Screenshot_2018-10-10%20Admin%20Page(1).png)

![Settings Window](https://github.com/fou65ad/web-coding-challenge/blob/master/screenshots/Screenshot%20from%202018-10-10%2023-51-15.png)

