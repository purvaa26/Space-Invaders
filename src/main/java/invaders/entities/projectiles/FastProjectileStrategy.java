package invaders.entities.projectiles;

/**
 * represents the strategy for fast projectiles
 */
public class FastProjectileStrategy implements ProjectileStrategy {

    @Override
    public void move(Projectile projectile) {
        projectile.getPosition().setY(projectile.getPosition().getY() + 2 * projectile.getSpeed());
    }
}
