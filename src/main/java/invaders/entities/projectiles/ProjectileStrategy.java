package invaders.entities.projectiles;

/**
 * represents the strategy for projectiles
 */
public interface ProjectileStrategy {
    //moves the projectile according to the specific strategy
    void move(Projectile projectile);
}
