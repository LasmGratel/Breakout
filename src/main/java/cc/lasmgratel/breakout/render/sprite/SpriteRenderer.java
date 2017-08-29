package cc.lasmgratel.breakout.render.sprite;

import cc.lasmgratel.breakout.render.Shader;
import cc.lasmgratel.breakout.render.Texture;
import org.apache.commons.io.IOUtils;
import org.joml.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class SpriteRenderer {
    private Shader shader;
    private int quadVAO;

    public SpriteRenderer() {
        try {
            shader = new Shader(
                    IOUtils.toString(SpriteRenderer.class.getResourceAsStream("/assets/shaders/sprite.fgs"), StandardCharsets.UTF_8),
                    IOUtils.toString(SpriteRenderer.class.getResourceAsStream("/assets/shaders/sprite.vxs"), StandardCharsets.UTF_8),
                    "");

            int vbo;
            float vertices[] = {
                    // 位置     // 纹理
                    0.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 0.0f,

                    0.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 0.0f
            };

            quadVAO = glGenVertexArrays();
            vbo = glGenBuffers();

            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

            glBindVertexArray(quadVAO);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 4, GL_FLOAT, false, 4 * Float.BYTES, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Texture texture, Vector2fc position, Vector2fc size, float rotate, Vector3fc color) {
        shader.use();
        Matrix4f model = new Matrix4f();
        model = model.translate(new Vector3f(position, 0.0f));
        model = model.translate(0.5f * size.x(), 0.5f * size.y(), 0.0f);
        model = model.rotate(rotate, new Vector3f(0.0f, 0.0f, 1.0f));
        model = model.translate(-0.5f * size.x(), -0.5f * size.y(), 0.0f);
        model = model.scale(new Vector3f(size, 1.0f));

        shader.setMatrix4("model", model);
        shader.setVector3f("spriteColor", color);

        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        glBindVertexArray(quadVAO);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);
    }
}
