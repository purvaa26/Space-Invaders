package invaders.entities.bunkers;

import javafx.scene.image.Image;
import java.io.File;

/**
 * represents the red state of a bunker.
 */
public class RedState implements BunkerState {
    private Bunker bunker;

    /**
     * constructs a RedState instance associated with a bunker
     * @param bunker The bunker associated with this state
     */
    public RedState(Bunker bunker) {
        this.bunker = bunker;
    }

    @Override
    public void onHit() {
        
    }


    @Override
    public Image getImage() {
        return new Image(new File(bunker.getRedBunkerImage()).toURI().toString(), bunker.getBunkerWidth(), bunker.getBunkerHeight(), true, true);
    }
}
