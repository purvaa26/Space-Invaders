package invaders.entities.bunkers;

import javafx.scene.image.Image;

/**
 * represents the state of a bunker entity.
 */

public interface BunkerState {

    //execute actions when the bunker is hit
    void onHit();

    //get image representing the bunker state
    Image getImage();
}
