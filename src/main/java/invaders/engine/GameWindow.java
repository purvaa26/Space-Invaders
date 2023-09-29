package invaders.engine;

import java.util.List;
import java.util.ArrayList;

import invaders.entities.EntityViewImpl;
import invaders.entities.Player;
import invaders.entities.SpaceBackground;
import javafx.util.Duration;

import javafx.scene.control.Label;
import invaders.entities.EntityView;
import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

//represents the main game window where the game is rendered and played
public class GameWindow {
	private final int width;
    private final int height;
	private Scene scene;
    private Pane pane;
    private GameEngine model;
    private final Player player;
    private List<EntityView> entityViews;
    private Renderable background;

    private double xViewportOffset = 0.0;
    private double yViewportOffset = 0.0;
    private static final double VIEWPORT_MARGIN = 280.0;

    private Timeline timeline;

    private Label livesLabel;
    private Text gameOverText;

    //construct game window
	public GameWindow(GameEngine model, int width, int height){
		this.width = width;
        this.height = height;
        this.model = model;
        this.player = model.getPlayer();
        pane = new Pane();
        scene = new Scene(pane, width, height);
        this.background = new SpaceBackground(model, pane);

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(this.model, this.player);

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

        entityViews = new ArrayList<EntityView>();

        //display lives on game screen
        livesLabel = new Label();
        livesLabel.setLayoutX(width - 100); 
        livesLabel.setLayoutY(10);
        pane.getChildren().add(livesLabel);

        //configure "Game Over" text node
        gameOverText = new Text("Game Over");
        gameOverText.setFont(Font.font("Arial", 48)); //set font and size
        gameOverText.setFill(Color.BLACK); //set the text color
        gameOverText.setX((width - gameOverText.getLayoutBounds().getWidth()) / 2); //centre horizontally
        gameOverText.setY((height + gameOverText.getLayoutBounds().getHeight()) / 2); //centre vertically
        gameOverText.setVisible(false); //initially hidden

        pane.getChildren().add(gameOverText); 

    }

    /*
     * start game loop and render game window
     */
	public void run() {
        timeline = new Timeline(new KeyFrame(Duration.millis(17), t -> {
            if (!model.isGameOver()) {
                this.draw();
            } else {
                //handle game over state
                updateGameOverState(true);
                //stop the game loop
                timeline.stop();
            }
        }));
    
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    

    private void draw(){
        model.update();
    
        livesLabel.setText("Lives: " + player.getLives());

        List<Renderable> renderables = model.getRenderables();
        
        //list of entities to remove
        List<EntityView> viewsToRemove = new ArrayList<>();
    
        for (EntityView view : entityViews) {
            boolean foundMatchingRenderable = false;
            for (Renderable r : renderables) {
                if (view.matchesEntity(r)) {
                    foundMatchingRenderable = true;
                    break;
                }
            }
            
            if (!foundMatchingRenderable) {
                viewsToRemove.add(view);
                pane.getChildren().remove(view.getNode());
            } else {
                view.update(xViewportOffset, yViewportOffset);
            }
        }
    
        entityViews.removeAll(viewsToRemove);
        
        for (Renderable entity : renderables) {
            boolean notFound = true;
            for (EntityView view : entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }
    }
    
    //updates the game over state and pauses or resumes the game loop
    public void updateGameOverState(boolean gameOver) {
        if (gameOver) {
            gameOverText.setVisible(true);
            //pause the game loop when game is over
            timeline.pause(); 
        } else {
            gameOverText.setVisible(false);
            //resume the game loop when not in game over state
            timeline.play(); 
        }
    }
    

	public Scene getScene() {
        return scene;
    }
}
