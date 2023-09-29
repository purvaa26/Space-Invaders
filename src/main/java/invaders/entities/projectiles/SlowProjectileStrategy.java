package invaders.entities.projectiles;

/**
 * represents the strategy for slow projectiles
 */
public class SlowProjectileStrategy implements ProjectileStrategy {

    @Override
    public void move(Projectile projectile) {
        projectile.getPosition().setY(projectile.getPosition().getY() + projectile.getSpeed());
    }
}
