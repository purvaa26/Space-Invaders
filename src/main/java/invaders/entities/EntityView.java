package invaders.entities;

import javafx.scene.Node;
import invaders.rendering.Renderable;

/**
 * interface representing an entity's view in the game
 */
public interface EntityView {
    void update(double xViewportOffset, double yViewportOffset);

    boolean matchesEntity(Renderable entity);

    void markForDelete();

    Node getNode();

    boolean isMarkedForDelete();
}
