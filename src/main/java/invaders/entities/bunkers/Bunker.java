package invaders.entities.bunkers;

import invaders.rendering.Renderable;
import invaders.physics.Vector2D;
import invaders.GameObject;
import invaders.physics.Collider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class Bunker implements GameObject, Renderable, Collider {

    private Vector2D position;
    private Image image;
    private final double width = 25;
    private final double height = 30;
    private BunkerState currentState;
    private int hits = 0;

    private final String BASE_PATH = "src/main/resources/";
    private final String RED_BUNKER_IMAGE = BASE_PATH + "bunker_red.png";
    private final String YELLOW_BUNKER_IMAGE = BASE_PATH + "bunker_yellow.png";
    private final String NORMAL_BUNKER_IMAGE = BASE_PATH + "bunker_green.png";
    private ImageView imageView;

    public Bunker(double x, double y, Image image) {
        this.position = new Vector2D(x, y);
        this.image = new Image(new File(NORMAL_BUNKER_IMAGE).toURI().toString(), width, height, true, true);
    }

    public Bunker(double x, double y) {
        this.position = new Vector2D(x, y);
        //set initial state to Green
        this.currentState = new GreenState(this);
        this.changeImage();
    }


    public void hit() {
        hits++; 
    
        //check hit count to decide what to do next
        if (hits == 1) {
            setState(new YellowState(this));
        } else if (hits == 2) {
            setState(new RedState(this));
        }
    }

    public void setState(BunkerState state) {
        this.currentState = state;
        changeImage();  //update bunker image to new state
    }

    private void changeImage() {
        this.image = currentState.getImage();
    }    

    public void changeState(BunkerState newState) {
        this.currentState = newState;
    }

    public ImageView getImageView() {
        return this.imageView;  
    }
    
    
    // GameObject interface methods
    @Override
    public void start() {
        
    }

    @Override
    public void update() {
        
    }

    // Renderable interface methods
    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public Renderable.Layer getLayer() {
        return Renderable.Layer.FOREGROUND;
    }

    @Override
    public boolean isColliding(Collider col) {
        return Collider.super.isColliding(col);
    }

    public void receiveDamage() {
        currentState.onHit();
    }

    public void remove() {
        System.out.println("lets remove the bunker ty");
    }

    public String getNormalBunkerImage() {
        return NORMAL_BUNKER_IMAGE;
    }
    
    public String getRedBunkerImage() {
        return RED_BUNKER_IMAGE;
    }
    
    public String getYellowBunkerImage() {
        return YELLOW_BUNKER_IMAGE;
    }
    
    public double getBunkerWidth() {
        return width;
    }
    
    public double getBunkerHeight() {
        return height;
    }

    public int getHits() {
        return hits;
    }

    public void setAppearance(String appearance) {
        switch (appearance) {
            case "green":
                changeState(new GreenState(this));
                break;
            case "yellow":
                changeState(new YellowState(this));
                break;
            case "red":
                changeState(new RedState(this));
                break;
            default:
                //throw an exception if the appearance string is unrecognized
                throw new IllegalArgumentException("Invalid appearance: " + appearance);
        }
    }

    //builder class for constructing Bunker objects
    public static class Builder {
        private double x;
        private double y;

        public Builder setX(double x) {
            this.x = x;
            return this;
        }

        public Builder setY(double y) {
            this.y = y;
            return this;
        }

        public Bunker build() {
            return new Bunker(x, y);
        }
    }

}
