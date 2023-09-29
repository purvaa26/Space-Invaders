package invaders.entities;

import invaders.entities.projectiles.Projectile;
import invaders.entities.projectiles.ProjectileFactory;
import invaders.logic.Damagable;
import invaders.physics.Collider;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;
import java.io.File;

/**
 * represents an enemy entity in the game
 */
public class Player implements Moveable, Damagable, Renderable, Collider {

    private final Vector2D position;
    private double health = 100;

    private final double width = 25;
    private final double height = 30;
    private final Image image;
    private Projectile projectile;

    private int lives = 3;

    //creates a player entity with the given initial position
    public Player(Vector2D position){
        this.image = new Image(new File("src/main/resources/player.png").toURI().toString(), width, height, true, true);
        this.position = position;
        this.projectile = ProjectileFactory.createPlayerProjectile(new Vector2D(position.getX(), position.getY()));

    }

    @Override
    public double getHealth() {
        return this.health;
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

    @Override
    public void up() {
        return;
    }

    @Override
    public void down() {
        return;
    }

    @Override
    public void left() {
        this.position.setX(this.position.getX() - 1);
    }

    @Override
    public void right() {
        this.position.setX(this.position.getX() + 1);
    }

    //shoot player projectile if there inst an active projectile already
    public void shoot(){
        if (!projectile.isActive()) {  
            System.out.println("shooting projectiles");
            projectile.getPosition().setX(position.getX() + width / 2 - projectile.getWidth() / 2);  
            projectile.getPosition().setY(position.getY() - projectile.getHeight());  //position the projectile at the top of the player
            projectile.activate();
        }
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }
    
    //get player projectile
    public Projectile getPlayerProjectile() {
        return projectile;
    }

    //get number of lives player has
    public int getLives() {
        return lives;
    }

    //reduce player life count by 1
    public void decreaseLife() {
        this.lives = lives - 1;
    }

    @Override
    public void takeDamage(double amount) {
        throw new UnsupportedOperationException("Unimplemented method 'takeDamage'");
    }


}
