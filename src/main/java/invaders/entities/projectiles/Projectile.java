package invaders.entities.projectiles;

import java.io.File;

import invaders.GameObject;
import invaders.physics.Collider;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

/**
 * represents a projectile entity in the game
 */
public class Projectile implements Renderable, Collider, Moveable {
    private Vector2D position;
    private double speed;
    private boolean active;

    //settings for projectile image
    private final double width = 12;
    private final double height = 19;
    private final Image image;

    private ProjectileStrategy strategy;

    /**
     * constructs a new Projectile instance
     * @param position   The initial position of the projectile
     * @param imagePath  The file path to the image representing the projectile
     * @param strategy   The strategy used for projectile movement
     */    
    public Projectile(Vector2D position, String imagePath, ProjectileStrategy strategy) {
        this.position = position;
        this.speed = 1;
        this.active = false;
        this.image = new Image(new File(imagePath).toURI().toString(), width, height, true, true);
        this.strategy = strategy;
    }
    

    @Override
    public void up() {
    }

    @Override
    public void down() {
    }

    @Override
    public void left() {
    }

    @Override
    public void right() {
    }
    
    //update state of projectile, including position and activation status
    public void update() {
        if (active) {
            strategy.move(this); 
            if (position.getY() + height < 0) {
                deactivate(); 
            }
        }
    }

    //gets the speed of the projectile
    public double getSpeed() {
        return speed;
    }    
    
    //gets the image representing the projectile
    public Image getImage() {
        return image;
    }

    //gets the width of the projectile
    public double getWidth() {
        return image.getWidth();
    }

    //gets the height of the projectile
    public double getHeight() {
        return image.getHeight(); 
    }

    //gets the position of the projectile
    public Vector2D getPosition() {
        return position;
    }

    public Renderable.Layer getLayer() {
        return Renderable.Layer.FOREGROUND; 
    }

    //checks if the projectile is currently active
    public boolean isActive() {
        return active;
    }

    //activate projectile to move and show on screen
    public void activate() {
        active = true;
    }

    //deactivate projectile
    public void deactivate() {
        active = false;
    }

    //flag for deletion
    public void markForDeletion() {
        this.active = false;
    }
    
}
