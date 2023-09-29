package invaders.entities;

import java.io.File;

import invaders.entities.projectiles.Projectile;
import invaders.entities.projectiles.ProjectileFactory;
import invaders.physics.Collider;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

/**
 * represents an enemy entity in the game
 */
public class Enemy implements Renderable, Moveable, Collider {
    private Vector2D position;
    private String projectile;
    private boolean active = true;
    private final Image image;
    private final double width = 25;
    private final double height = 30;
    
    private static double speed = 1.0; //enemy movement speed
    private static double moveDown = 10.0;
    private static final double maxSpeed = 3;
    private static final double incrementValue = 0.05;

    private boolean canShoot = true;
    


    private Enemy() {
        this.image = new Image(new File("src/main/resources/enemy.png").toURI().toString(), width, height, true, true);
    }

    /**
     * builder class for creating enemy instances
     */
    public static class Builder {
        private Vector2D position = new Vector2D(0, 0);
        private String projectile;

        /**
         * sets the position of the enemy
         * @param positionX The X-coordinate of the position
         * @param positionY The Y-coordinate of the position
         * @return The builder instance
         */
        public Builder setPosition(double positionX, double positionY) {
            this.position = new Vector2D(positionX, positionY);
            return this;
        }

        /**
         * sets the projectile type for the enemy
         * @param projectile The type of projectile the enemy uses
         * @return The builder instance
         */
        public Builder setProjectile(String projectile) {
            this.projectile = projectile;
            return this;
        }

        /**
         * builds the enemy instance
         * @return The created enemy
         */
        public Enemy build() {
            Enemy enemy = new Enemy();
            enemy.position = this.position;
            enemy.projectile = this.projectile;
            return enemy;
        }
    }
    
    @Override
    public void up() {
        this.position.setY(this.position.getY() - moveDown);
    }

    @Override
    public void down() {
        this.position.setY(this.position.getY() + moveDown);
    }

    @Override
    public void left() {
        this.position.setX(this.position.getX() - speed);
    }

    @Override
    public void right() {
        this.position.setX(this.position.getX() + speed);
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public double getWidth() {
        return image.getWidth();
    }

    @Override
    public double getHeight() {
        return image.getHeight();
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    //gets the projectile type used by the enemy
    public String getProjectile() {
        return projectile;
    }

    //mark enemy for deletion
    public void markForDeletion() {
        this.active = false;
    }
    
    //check if enemy is currently active
    public boolean isActive() {
        return active;
    }

    //increase enemy movement speed
    public void increaseSpeed() {
        speed += incrementValue;
        if (speed >= maxSpeed) {
            speed = maxSpeed;
        }
    }

    //create and activate a projectile fired by enemy
    public Projectile shoot() {
        Vector2D position = new Vector2D(getPosition().getX() + getWidth() / 2, getPosition().getY() + getHeight());
        Projectile enemyProjectile;
        
        if ("fast_straight".equalsIgnoreCase(getProjectile())) {
            enemyProjectile = ProjectileFactory.createFastProjectile(position);
        } else {
            enemyProjectile = ProjectileFactory.createSlowProjectile(position);
        }
        
        enemyProjectile.activate();
        return enemyProjectile;
    
    }
}
