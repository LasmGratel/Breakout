package cc.lasmgratel.breakout.game;

import cc.lasmgratel.breakout.render.Texture;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GameRegistry {
    public static GameRegistry INSTANCE = new GameRegistry();
    private Map<String, Object> registryMap = new HashMap<>();

    public void registerTexture(String name, URL url) {
        try {
            Texture texture = new Texture(url);
            register(name, texture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(String name, Object obj) {
        registryMap.put(name, obj);
    }

    public <T> T get(String name) {
        return (T) registryMap.get(name);
    }
}
