# Space-Invaders
This Java project, built with JavaFX, is an engaging space invaders game that challenges players to defend against alien invaders and protect their spaceship. It should be executed using the coding environment stated below:  
- Gradle 7.4.2
- JDK 17
- Unix-based System

# How to run the code?  
To run the code, download the zip file and open it in your preferred IDE. Then open your terminal and build the project using 'gradle clean build
' and then run the application using the command 'gradle run'. This will compile and execute the Java application. You should see the game window appear on your screen.  


  

# Which files and classes are involved in each design pattern implementation?  
## Factory Method  
The factory method was used to create the projectiles. The files involved are: the `Projectile.java` and `ProjectileFactory.java` files which consist of the `Projectile` and `ProjectileFactory` class respectively.  

## State Pattern  
The state pattern was used to control and change the colour of each bunker when it was hit. The files involved are: `BunkerState.java` interface and `GreenState.java`, `RedState.java` and `YellowState.java` which implemented the interface for specific handling of features.  

## Builder Pattern
The builder pattern was used to create the enemies and the bunkers. The files involved are: `Bunker.java` and `Enemy.java`. Both these files have a `public static class Builder` which is used to construct their respective objects.  

## Strategy Pattern  
The strategy patternw as used to control the projectile behaviour since we had two different behaviours. The files involved are: `ProjectileStrategy.java` which has a `ProjectileStrategy` interface. This interface is then implemented by `FastProjectileStrategy.java`, `SlowProjectileStrategy.java` and `PlayerProjectileStrategy.java` files and their respective classes.   


# Additional Information  
The extent of how much of the game we could change was not specified to us. Therefore, this game has two additional features:
1. Displayes player lives in the top-right corner
2. Game Over: is shown across the screen and all gameplay is stopped once the game is over. The game will pause and end if:
     - Player is shot by a projectile
     - Enemy reaches bottom of screen
     - Enemy collides with player
