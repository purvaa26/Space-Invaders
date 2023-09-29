package invaders.entities;

import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * represents the implementation of an entity's view
 */
public class EntityViewImpl implements EntityView {
    private Renderable entity;
    private Vector2D position;
    private boolean delete = false;
    private ImageView node;

    public EntityViewImpl(Renderable entity) {
        this.entity = entity;
        this.position = entity.getPosition();
        node = new ImageView(entity.getImage());
        node.setViewOrder(getViewOrder(entity.getLayer()));
        update(0.0, 0.0);
    }

    private static double getViewOrder(Renderable.Layer layer) {
        switch (layer) {
            case BACKGROUND: return 100.0;
            case FOREGROUND: return 50.0;
            case EFFECT: return 25.0;
            default: throw new IllegalStateException("Javac doesn't understand how enums work so now I have to exist");
        }
    }

    @Override
    public void update(double xViewportOffset, double yViewportOffset) {
        Image currentNodeImage = node.getImage();
        Image entityImage = entity.getImage();

        if (currentNodeImage == null || !currentNodeImage.equals(entityImage)) {
            node.setImage(entityImage);
        }

        node.setX(position.getX() - xViewportOffset);
        node.setY(position.getY() - yViewportOffset);
        node.setFitHeight(entity.getHeight());
        node.setFitWidth(entity.getWidth());
        node.setPreserveRatio(true);
        delete = false;
    }

    @Override
    public boolean matchesEntity(Renderable entity) {
        return this.entity.equals(entity);
    }

    @Override
    public void markForDelete() {
        delete = true;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public boolean isMarkedForDelete() {
        return delete;
    }
}
