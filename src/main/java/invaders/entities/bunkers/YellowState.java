package invaders.entities.bunkers;

import javafx.scene.image.Image;
import java.io.File;

/**
 *represents the yellow state of a bunker.
 */

public class YellowState implements BunkerState {
    private Bunker bunker;

    /**
     * constructs a YellowState instance associated with a bunker
     * @param bunker The bunker associated with this state
     */
    public YellowState(Bunker bunker) {
        this.bunker = bunker;
    }

    @Override
    public void onHit() {
        bunker.changeState(new RedState(bunker));
    } 


    @Override
    public Image getImage() {
        return new Image(new File(bunker.getYellowBunkerImage()).toURI().toString(), bunker.getBunkerWidth(), bunker.getBunkerHeight(), true, true);
    }
}
