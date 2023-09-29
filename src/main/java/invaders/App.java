package invaders;

import javafx.application.Application;
import javafx.stage.Stage;
import invaders.ConfigReader;
import invaders.engine.GameEngine;
import invaders.engine.GameWindow;

import java.util.Map;

/**
 * the main application class for the Space Invaders game
 */
public class App extends Application {

    //main method to launch applocation
    public static void main(String[] args) {
        launch(args);
    }

    //main entry point for JavaFX app
    @Override
    public void start(Stage primaryStage) {
        Map<String, String> params = getParameters().getNamed();
        
        ConfigReader configReader = new ConfigReader();
		configReader.parse("src/main/resources/config.json");

        ConfigReader.GameConfig gameConfig = configReader.getGameConfig();


        GameEngine model = new GameEngine("/resources/config.json");
        GameWindow window = new GameWindow(model, gameConfig.getSizeX(), gameConfig.getSizeY());
        window.run();

        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(window.getScene());
        primaryStage.show();

        window.run();
    }
}
