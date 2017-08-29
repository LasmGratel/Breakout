package cc.lasmgratel.breakout.game;

import cc.lasmgratel.breakout.render.Texture;
import org.joml.Vector2f;

public class Ball extends GameObject {
    private float radius;
    private boolean stuck;

    public Ball() {
    }

    public Ball(Vector2f pos, float radius, boolean stuck, Texture sprite) {
        this.radius = radius;
        this.stuck = stuck;
        setPosition(pos);
        setSprite(sprite);
    }

    public Vector2f move(float dt, int windowWidth) {
        // 如果没有被固定在挡板上
        if (!stuck) {
            // 移动球
            getPosition().add(getVelocity().mul(dt));
            // 检查是否在窗口边界以外，如果是的话反转速度并恢复到正确的位置
            if (getPosition().x <= 0.0f)
            {
                getVelocity().x = -getVelocity().x;
                getPosition().x = 0.0f;
            }
        else if (getPosition().x + getSize().x >= windowWidth)
            {
                getVelocity().x = -getVelocity().x;
                getPosition().x = windowWidth - getSize().x;
            }
            if (getPosition().y <= 0.0f)
            {
                getVelocity().y = -getVelocity().y;
                getPosition().y = 0.0f;
            }

        }
        return getPosition();
    }

    public void reset(Vector2f position, Vector2f velocity) {
        setPosition(position);
        setVelocity(velocity);
        stuck = true;
    }
}
