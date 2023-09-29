package invaders.engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.text.Position;

import invaders.GameObject;
import invaders.entities.Enemy;
import invaders.entities.Player;
import invaders.entities.bunkers.Bunker;
import invaders.entities.projectiles.Projectile;
import invaders.ConfigReader;
import invaders.physics.Collider;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.layout.Pane;
import invaders.ConfigReader.BunkerConfig;

import java.util.Random;


/**
 * This class manages the main loop and logic of the game
 */
public class GameEngine {

	private List<GameObject> gameobjects;
	private Pane gamePane;
	private List<Renderable> renderables;
	private Player player;
    private ConfigReader configReader;
    private List<Enemy> enemies = new ArrayList<>();
	private List<Bunker> bunkers = new ArrayList<>();
	private List<Projectile> projectiles = new ArrayList<>();
	private int screenHeight;
	private int screenWidth;

	private boolean left;
	private boolean right;

	private boolean movingRight = true;
	private final int maxEnemyProj = 3;
	private Random random = new Random();

	private boolean gameOver = false;

	/**
     * constructs a GameEngine instance.
     * @param config the configuration file path
     */

	public GameEngine(String config){
		this.gamePane = gamePane;

		configReader = new ConfigReader();
		configReader.parse("src/main/resources/config.json");
	
		//initialize the gameobjects and renderables lists first
		gameobjects = new ArrayList<GameObject>();
		renderables = new ArrayList<Renderable>();
	
		//get the playerconfig object
		ConfigReader.PlayerConfig playerConfig = configReader.getPlayerConfig();
		
		//get game config
		ConfigReader.GameConfig gameConfig = configReader.getGameConfig();
		screenWidth = (int) gameConfig.getSizeX();
		screenHeight = (int) gameConfig.getSizeY();
	
		List<ConfigReader.EnemyConfig> enemyConfigs = configReader.getEnemyConfig();
		for (ConfigReader.EnemyConfig enemyConfig : enemyConfigs) {
			Enemy enemy = new Enemy.Builder().setPosition(enemyConfig.getPositionX(), enemyConfig.getPositionY()).setProjectile(enemyConfig.getProjectile()).build();
			enemies.add(enemy);
			renderables.add(enemy);
		}
	
		//access player data using configReader
		player = new Player(new Vector2D(playerConfig.getPositionX(), playerConfig.getPositionY()));
		System.out.println(playerConfig.getPositionX());
		System.out.println(playerConfig.getPositionY());
	
		renderables.add(player);

		List<BunkerConfig> bunkerConfigs = configReader.getBunkerConfig();
		for (BunkerConfig bunkerConfig : bunkerConfigs) {
			Bunker bunker = new Bunker.Builder()
							.setX(bunkerConfig.getPositionX())
							.setY(bunkerConfig.getPositionY())
							.build();

			bunkers.add(bunker);
			gameobjects.add(bunker);
			renderables.add(bunker);
		}
	}
	

	/**
	 * Updates the game/simulation
	 */

	public void update(){
		movePlayer();

		if (movingRight) {
			if (rightmostEnemy(enemies).getPosition().getX() + rightmostEnemy(enemies).getWidth() >= screenWidth) {
				movingRight = false; //move left
				for (Enemy enemy : enemies) {
					enemy.down(); //move all enemies down
				}
			} else {
				for (Enemy enemy : enemies) {
					enemy.right(); //move right
				}
			}
		} else {
			if (leftmostEnemy(enemies).getPosition().getX() <= 0) {
				movingRight = true; //moev right
				for (Enemy enemy : enemies) {
					enemy.down(); //move all enemies down
				}
			} else {
				for (Enemy enemy : enemies) {
					enemy.left(); //move left
				}
			}
		}

		//ppdate the player's projectile
		if (player.getPlayerProjectile().isActive()) {
			player.getPlayerProjectile().update();
			

			//check bunker and projectile collision
			for (Bunker bunker : bunkers) {
				if (checkCollision(player.getPlayerProjectile(), bunker)) {
					System.out.println("Bunker hit!");
					bunker.hit();
					player.getPlayerProjectile().deactivate();
					
					//if bunker has 3 hits, remove it
					if (bunker.getHits() == 3) {
						destroyBunker(bunker);
					}
					break;
				}
			}
			

			//check if projectile hits any enemies
			for (Enemy enemy : new ArrayList<>(enemies)) {
				if (checkCollision(player.getPlayerProjectile(), enemy)) {
					System.out.println("Collision detected!");
					enemy.increaseSpeed(); //increase speed when projectile collides with enemy
			
					player.getPlayerProjectile().deactivate();
					
					if (enemies.remove(enemy)) {
						System.out.println("Enemy removed from enemies list");
					}
					
					if (gameobjects.remove(enemy)) {
						System.out.println("Enemy removed from gameobjects list");
					}
			
					if (renderables.remove(enemy)) {
						System.out.println("Enemy removed from renderables list");
					}
			
					break;
				}
			}

			//add the projectile to the renderables list to be rendered
			if(!renderables.contains(player.getPlayerProjectile())) {
				renderables.add(player.getPlayerProjectile());
			}
		} else {
			//remove the projectile from the renderables list if it is not active
			renderables.remove(player.getPlayerProjectile());
		}

		activateRandomEnemyProjectiles(3);

        //update enemy projectiles
		List<Projectile> projectilesToRemove = new ArrayList<>();
		for (Projectile enemyProjectile : projectiles) {
			if (enemyProjectile.isActive()) {
				enemyProjectile.update();
				
				//check if the projectile has crossed the screen's height
				if (enemyProjectile.getPosition().getY() > screenHeight) {
					enemyProjectile.deactivate();
					projectilesToRemove.add(enemyProjectile);
					renderables.remove(enemyProjectile);
					continue;
				}
				
				//add the projectile to the renderables list to be rendered
				if (!renderables.contains(enemyProjectile)) {
					renderables.add(enemyProjectile);
				}

				for (Bunker bunker : bunkers) {
					if (checkCollision(enemyProjectile, bunker)) {
						System.out.println("Bunker hit by enemy projectile!");
						bunker.hit();  //register a hit on the bunker
						enemyProjectile.deactivate();  //deactivate the enemy projectile
						projectilesToRemove.add(enemyProjectile);
						renderables.remove(enemyProjectile);
						
						//if bunker has 3 hits, remove it
						if (bunker.getHits() == 3) {
							destroyBunker(bunker);
						}
						break;  //exit the inner loop if collision found
					}
				}

				//check if the projectile has hit the player
				if (checkCollision(enemyProjectile, player)) {
					System.out.println("Player hit!");
					player.decreaseLife(); 
					enemyProjectile.deactivate(); // deactivate the enemy projectile
					System.out.println("player has lives: " + player.getLives());
					enemyProjectile.deactivate();
					projectilesToRemove.add(enemyProjectile);
					renderables.remove(enemyProjectile);
					
					if (player.getLives() <= 0) {
						System.out.println("Game Over!");
						gameOver = true;
					}
					break;
				}
			} else {
				renderables.remove(enemyProjectile);
			}
		}
		//remove projectiles that are deactivated
		projectiles.removeAll(projectilesToRemove);


		for(Renderable ro: renderables){
			if (ro instanceof Enemy) {
				continue;  //skip the boundary check for enemies
			}

			if (ro instanceof Projectile) {
				continue;  //skip the boundary check for projectiles
			}

			if(ro.getPosition().getX() + ro.getWidth() >= screenWidth) {
				ro.getPosition().setX(screenWidth-ro.getWidth());
			}

			if(ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(1);
			}

			if(ro.getPosition().getY() + ro.getHeight() >= screenHeight) {
				ro.getPosition().setY(screenHeight-ro.getHeight());
			}

			if(ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(1);
			}
		}
		
		if (checkEnemyReachedBottom() || checkEnemyReachedPlayer()) {
			//set gameover flag
			gameOver = true;
		}
	}

	public List<Renderable> getRenderables(){
		return renderables;
	}


	public void leftReleased() {
		this.left = false;
	}

	public void rightReleased(){
		this.right = false;
	}

	public void leftPressed() {
		this.left = true;
	}
	public void rightPressed(){
		this.right = true;
	}

	//check is player shoots are triggered
	public boolean shootPressed(){
		player.shoot();
		return true;
		
	}

	//player movement
	private void movePlayer(){
		if(left){
			player.left();
		}

		if(right){
			player.right();
		}
	}

	//get player data
	public Player getPlayer(){
		return player;
	}

	//find rightmost enemy
	private Enemy rightmostEnemy(List<Enemy> enemies) {
		return enemies.stream().max(Comparator.comparingDouble(e -> e.getPosition().getX())).orElse(null);
	}

	//find leftmost enemy
	private Enemy leftmostEnemy(List<Enemy> enemies) {
		return enemies.stream().min(Comparator.comparingDouble(e -> e.getPosition().getX())).orElse(null);
	}

	//check whether two objects are colliding
	public boolean checkCollision(Collider a, Collider b){
		return a.isColliding(b);
	}
	

	//remoev active projectile and enemy from screen upon collision
	public void handleCollision(Enemy enemy, Projectile projectile) {
		enemies.remove(enemy);
		projectiles.remove(projectile);
	
		gamePane.getChildren().remove(enemy.getImage());
		gamePane.getChildren().remove(projectile.getImage());
	}

	//handles removing bunkers from screen
	private void destroyBunker(Bunker bunker) {
		bunkers.remove(bunker);
		gameobjects.remove(bunker);
		renderables.remove(bunker);
	}

	//activate random enemy projectiles and adds them to the list of active projectiles

	private void activateRandomEnemyProjectiles(int count) {
		int attempts = 0;
		
		while (attempts < count) {
			//check if there are already 3 active projectiles - if yes then break loop
			if (projectiles.size() >= 3) {
				break;
			}
			
			int randomIndex = random.nextInt(enemies.size());
			Enemy randomEnemy = enemies.get(randomIndex);
			Projectile projectile = randomEnemy.shoot();
	
			if (projectile != null) {
				projectiles.add(projectile);
				attempts++; //increase the attempts only when a projectile is successfully activated.
			}
		}
	}


	//check if any enemies reached the bottom of screen
	private boolean checkEnemyReachedBottom() {
		for (Enemy enemy : enemies) {
			if (enemy.getPosition().getY() + enemy.getHeight() >= screenHeight) {
				return true; //an enemy reached the bottom
			}
		}
		return false;
	}
	
	//checks if any enemy has collided with the player
	private boolean checkEnemyReachedPlayer() {
		for (Enemy enemy : enemies) {
			if (enemy.isColliding(player)) {
				return true; //an enemy reached the player
			}
		}
		return false;
	}

	//pause game since its over
	public boolean isGameOver() {
		return gameOver;
	}
	
}
