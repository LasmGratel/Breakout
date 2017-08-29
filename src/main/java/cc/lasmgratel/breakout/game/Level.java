package cc.lasmgratel.breakout.game;

import cc.lasmgratel.breakout.render.Texture;
import cc.lasmgratel.breakout.render.sprite.SpriteRenderer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Level {
    private List<GameObject> bricks = new ArrayList<>();

    public void load(Path path, int levelWidth, int levelHeight) throws IOException {
        List<List<Integer>> intList = new ArrayList<>();
        for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
            List<Integer> temp = new ArrayList<>();
            Scanner scanner = new Scanner(line);
            boolean hasNext = scanner.hasNextInt();
            while (hasNext) {
                temp.add(scanner.nextInt());
                hasNext = scanner.hasNextInt();
            }
            intList.add(temp);
        }
        init(intList, levelWidth, levelHeight);
    }

    private void init(List<List<Integer>> intList, int levelWidth, int levelHeight) {
        // Calculate dimensions
        int height = intList.size();
        int width = intList.get(0).size(); // Note we can index vector at [0] since this function is only called if height > 0
        float unit_width = levelWidth / (float) width, unit_height = levelHeight / (float) height;
        // Initialize level tiles based on tileData
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                // Check block type from level data (2D level array)
                if (intList.get(y).get(x) == 1) { // Solid
                    Vector2f pos = new Vector2f(unit_width * x, unit_height * y);
                    Vector2f size = new Vector2f(unit_width, unit_height);
                    GameObject obj = new GameObject(pos, size, new Vector2f(0.0f), new Vector3f(0.8f, 0.8f, 0.7f), 0.0f, GameRegistry.INSTANCE.get("textureSolid"));
                    obj.setSolid(true);
                    bricks.add(obj);
                } else if (intList.get(y).get(x) > 1) {   // Non-solid; now determine its color based on level data
                    Vector3f color = new Vector3f(1.0f); // original: white
                    if (intList.get(y).get(x) == 2)
                        color = new Vector3f(0.2f, 0.6f, 1.0f);
                    else if (intList.get(y).get(x) == 3)
                        color = new Vector3f(0.0f, 0.7f, 0.0f);
                    else if (intList.get(y).get(x) == 4)
                        color = new Vector3f(0.8f, 0.8f, 0.4f);
                    else if (intList.get(y).get(x) == 5)
                        color = new Vector3f(1.0f, 0.5f, 0.0f);

                    Vector2f pos = new Vector2f(unit_width * x, unit_height * y);
                    Vector2f size = new Vector2f(unit_width, unit_height);
                    bricks.add(new GameObject(pos, size, new Vector2f(0.0f), color, 0.0f, GameRegistry.INSTANCE.get("textureBlock")));
                }
            }
        }
    }

    public void draw(SpriteRenderer renderer) {
        bricks.forEach(gameObject -> {
            if (!gameObject.isDestroyed())
                gameObject.draw(renderer);
        });
    }

    public boolean isCompleted() {
        for (GameObject gameObject : bricks)
            if (!gameObject.isDestroyed() && !gameObject.isSolid())
                return false;
        return true;
    }

    @Override
    public String toString() {
        return "Level{" +
                "bricks=" + bricks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Level level = (Level) o;

        return bricks != null ? bricks.equals(level.bricks) : level.bricks == null;
    }

    @Override
    public int hashCode() {
        return bricks != null ? bricks.hashCode() : 0;
    }
}
