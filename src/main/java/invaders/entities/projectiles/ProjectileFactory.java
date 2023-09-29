package invaders.entities.projectiles;

import invaders.physics.Vector2D;

/**
 * factory class for creating projectiles
 */
public class ProjectileFactory {
    private static final String slowProjectile = "src/main/resources/slow_projectile.png";
    private static final String fastProjectile = "src/main/resources/fast_projectile.png";

    /**
     * creates a fast projectile at the specified position
     * @param position the initial position of the fast projectile
     * @return a new fast projectile
     */
    public static Projectile createFastProjectile(Vector2D position) {
        return new Projectile(position, fastProjectile, new FastProjectileStrategy());
    }

    /**
     * creates a slow projectile at the specified position
     * @param position the initial position of the slow projectile
     * @return a new slow projectile
     */
    public static Projectile createSlowProjectile(Vector2D position) {
        return new Projectile(position, slowProjectile, new SlowProjectileStrategy());
    }

    /**
     * creates a player projectile at the specified position
     * @param position the initial position of the player projectile
     * @return a new player projectile
     */
    public static Projectile createPlayerProjectile(Vector2D position) {
        return new Projectile(position, slowProjectile, new PlayerProjectileStrategy());
    }
}
