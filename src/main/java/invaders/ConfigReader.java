package invaders;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * a class for reading and parsing configuration data from a JSON file
 */

public class ConfigReader {
    private PlayerConfig playerConfig;
    private GameConfig gameConfig;
    private List<BunkerConfig> bunkers;
    private List<EnemyConfig> enemies;

     /*
      * class to represent game configuration data
      */
    public class GameConfig {
        private long sizeX;
        private long sizeY;

        public GameConfig(long sizeX, long sizeY) {
            this.sizeX = sizeX;
            this.sizeY = sizeY;
        }

        //getter methods to access variables outside this class
        public int getSizeX() {    
            return (int) sizeX;
        }

        public int getSizeY() {
            return (int) sizeY;
        }
    }

     /*
      * class to represent player configuration data
      */
    public class PlayerConfig {
        private String colour;
        private int speed;
        private int lives;
        private long positionX;
        private long positionY;

        public PlayerConfig(String colour, int speed, int lives, long positionX, long positionY) {
            this.colour = colour;
            this.speed = speed;
            this.lives = lives;
            this.positionX = positionX;
            this.positionY = positionY;
        }

        //getter methods to access variables outside this class
        public String getColour() {
            return colour;
        }

        public int getSpeed() {
            return speed;
        }

        public int getLives() {
            return lives;
        }

        public long getPositionX() {
            return positionX;
        }

        public long getPositionY() {
            return positionY;
        }
    }

        /*
      * class to represent bunker configuration data
      */
    public class BunkerConfig {
        private long positionX;
        private long positionY;
        private long sizeX;
        private long sizeY;

        public BunkerConfig(long positionX, long positionY, long sizeX, long sizeY) {
            this.positionX = positionX;
            this.positionY = positionY;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
        }

        //getter methods to access variables outside this class
        public long getPositionX() {
            return positionX;
        }

        public long getPositionY() {
            return positionY;
        }

        public long getSizeX() {
            return sizeX;
        }

        public long getSizeY() {
            return sizeY;
        }
    }

     /*
      * class to represent enemies configuration data
      */
    public class EnemyConfig {
        private long positionX;
        private long positionY;
        private String projectile;

        public EnemyConfig(long positionX, long positionY, String projectile) {
            this.positionX = positionX;
            this.positionY = positionY;
            this.projectile = projectile;
        }

        //getter methods to access variables outside this class
        public double getPositionX() {
            return (double) positionX;
        }

        public double getPositionY() {
            return (double) positionY;
        }

        public String getProjectile() {
            return projectile;
        }
    }

    public void parse(String path) {
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader(path));
            JSONObject jsonObject = (JSONObject) object;

            //read and save game data
            JSONObject jsonGame = (JSONObject) jsonObject.get("Game");
            long gameX = (long) ((JSONObject) jsonGame.get("size")).get("x");
            long gameY = (long) ((JSONObject) jsonGame.get("size")).get("y");
            this.gameConfig = new GameConfig(gameX, gameY);

            //read and save player data
            JSONObject jsonPlayer = (JSONObject) jsonObject.get("Player");
            String playerColour = (String) jsonPlayer.get("colour");
            int playerSpeed = ((Long) jsonPlayer.get("speed")).intValue();
            int playerLives = ((Long) jsonPlayer.get("lives")).intValue();
            long playerPositionX = (long) ((JSONObject) jsonPlayer.get("position")).get("x");
            long playerPositionY = (long) ((JSONObject) jsonPlayer.get("position")).get("y");
            this.playerConfig = new PlayerConfig(playerColour, playerSpeed, playerLives, playerPositionX, playerPositionY);

            //read the bunker data and save to array
            JSONArray jsonBunkers = (JSONArray) jsonObject.get("Bunkers");
            bunkers = new ArrayList<>();

            for (Object obj : jsonBunkers) {
                JSONObject jsonBunker = (JSONObject) obj;
                
                long bunkerPositionX = (long) ((JSONObject) jsonBunker.get("position")).get("x");
                long bunkerPositionY = (long) ((JSONObject) jsonBunker.get("position")).get("y");
                long bunkerSizeX = (long) ((JSONObject) jsonBunker.get("size")).get("x");
                long bunkerSizeY = (long) ((JSONObject) jsonBunker.get("size")).get("y");

                BunkerConfig bunkerConfig = new BunkerConfig(bunkerPositionX, bunkerPositionY, bunkerSizeX, bunkerSizeY);
                bunkers.add(bunkerConfig);
            }

            //read the enemies data and save to array
            JSONArray jsonEnemies = (JSONArray) jsonObject.get("Enemies");
            enemies = new ArrayList<>();

            for (Object obj : jsonEnemies) {
                JSONObject jsonEnemy = (JSONObject) obj;
                
                long enemyPositionX = (long) ((JSONObject) jsonEnemy.get("position")).get("x");
                long enemyPositionY = (long) ((JSONObject) jsonEnemy.get("position")).get("y");
                String enemyProjectile = (String) jsonEnemy.get("projectile");

                EnemyConfig enemyConfig = new EnemyConfig(enemyPositionX, enemyPositionY, enemyProjectile);
                enemies.add(enemyConfig);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public PlayerConfig getPlayerConfig() {
        return playerConfig;
    }

    public GameConfig getGameConfig(){
        return gameConfig;
    }

    public List<BunkerConfig> getBunkerConfig() {
        return bunkers;
    }

    public List<EnemyConfig> getEnemyConfig() {
        return enemies;
    }

    public static void main(String[] args) {
        ConfigReader configReader = new ConfigReader();
        String configPath;
        if (args.length > 0) {
            configPath = args[0];
        } else {
            configPath = "src/main/resources/config.json";
        }
        configReader.parse(configPath);
    }
}
