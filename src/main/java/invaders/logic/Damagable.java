package invaders.logic;

/**
 * an interface representing entities that can take damage in the game
 */
public interface Damagable {

	public void takeDamage(double amount);

	public double getHealth();

	public boolean isAlive();

}
