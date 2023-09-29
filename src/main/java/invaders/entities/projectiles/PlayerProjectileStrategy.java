package invaders.entities.projectiles;

/**
 * represents the strategy for player projectiles
 */
public class PlayerProjectileStrategy implements ProjectileStrategy {

    @Override
    public void move(Projectile projectile) {
        projectile.getPosition().setY(projectile.getPosition().getY() - projectile.getSpeed());
    }
}
