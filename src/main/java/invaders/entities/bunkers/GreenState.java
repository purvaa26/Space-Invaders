package invaders.entities.bunkers;

import javafx.scene.image.Image;
import java.io.File;

/**
 * represents the green state of a bunker.
 */
public class GreenState implements BunkerState {
    private Bunker bunker;

    /**
     * constructs a GreenState instance associated with a bunker
     * @param bunker The bunker associated with this state
     */

    public GreenState(Bunker bunker) {
        this.bunker = bunker;
    }

    @Override
    public void onHit() {
        bunker.changeState(new YellowState(bunker));
    }


    @Override
    public Image getImage() {
        return new Image(new File(bunker.getNormalBunkerImage()).toURI().toString(), bunker.getBunkerWidth(), bunker.getBunkerHeight(), true, true);
    }

}
