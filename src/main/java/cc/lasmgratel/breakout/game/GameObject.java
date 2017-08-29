package cc.lasmgratel.breakout.game;

import cc.lasmgratel.breakout.render.Texture;
import cc.lasmgratel.breakout.render.sprite.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GameObject {
    // Object state
    private Vector2f position, size, velocity;
    private Vector3f color;
    private float rotation;
    private boolean solid;
    private boolean destroyed;
    // Render state
    private Texture sprite;
    // Draw sprite
    public void draw(SpriteRenderer renderer) {
        renderer.draw(sprite, position, size, rotation, color);
    }

    public GameObject() {
        this.position = new Vector2f(0, 0);
        this.size = new Vector2f(1, 1);
        this.velocity = new Vector2f(0.0f);
        this.color = new Vector3f(1.0f);
        this.rotation = 0.0f;
        this.solid = false;
        this.destroyed = false;
    }

    public GameObject(Vector2f position, Vector2f size, Vector2f velocity, Vector3f color, float rotation, Texture sprite) {
        this.position = position;
        this.size = size;
        this.velocity = velocity;
        this.color = color;
        this.rotation = rotation;
        this.solid = false;
        this.destroyed = false;
        this.sprite = sprite;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public boolean isSolid() {
        return solid;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public Texture getSprite() {
        return sprite;
    }

    public void setSprite(Texture sprite) {
        this.sprite = sprite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameObject that = (GameObject) o;

        if (Float.compare(that.rotation, rotation) != 0) return false;
        if (solid != that.solid) return false;
        if (destroyed != that.destroyed) return false;
        if (position != null ? !position.equals(that.position) : that.position != null) return false;
        if (size != null ? !size.equals(that.size) : that.size != null) return false;
        if (velocity != null ? !velocity.equals(that.velocity) : that.velocity != null) return false;
        if (color != null ? !color.equals(that.color) : that.color != null) return false;
        return sprite != null ? sprite.equals(that.sprite) : that.sprite == null;
    }

    @Override
    public int hashCode() {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (velocity != null ? velocity.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (rotation != +0.0f ? Float.floatToIntBits(rotation) : 0);
        result = 31 * result + (solid ? 1 : 0);
        result = 31 * result + (destroyed ? 1 : 0);
        result = 31 * result + (sprite != null ? sprite.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GameObject{" +
                "position=" + position +
                ", size=" + size +
                ", velocity=" + velocity +
                ", color=" + color +
                ", rotation=" + rotation +
                ", solid=" + solid +
                ", destroyed=" + destroyed +
                ", sprite=" + sprite +
                '}';
    }
}
