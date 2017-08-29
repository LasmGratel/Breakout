package cc.lasmgratel.breakout.render;

import cc.lasmgratel.breakout.game.GameObject;
import cc.lasmgratel.breakout.game.GameRegistry;
import cc.lasmgratel.breakout.game.Level;
import cc.lasmgratel.breakout.render.sprite.SpriteRenderer;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.IntBuffer;
import java.nio.file.Paths;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class RenderThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger("Render Thread");
    private long window;
    private GameObject player;
    private SpriteRenderer sprite;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private void init() {
        LOGGER.info("Using LWJGL {}", Version.getVersion());

        GLFWErrorCallback.create((error, description) -> LOGGER.error("GLFW rendering error {}: {}", error, description)).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "Breakout", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, this::handleKeyCallback);

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void handleKeyCallback(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
            glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
    }

    private void loadLevels() {
        Level level = new Level();
        try {
            level.load(Paths.get(RenderThread.class.getResource("/assets/levels/1.txt").toURI()), WIDTH, (int)(HEIGHT * 0.5));
            GameRegistry.INSTANCE.register("level1", level);
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Cannot load level!", e);
        }
    }

    private void loadTextures() {
        GameRegistry.INSTANCE.registerTexture("textureBlock", GameRegistry.class.getResource("/assets/textures/block.png"));
        GameRegistry.INSTANCE.registerTexture("textureSolid", GameRegistry.class.getResource("/assets/textures/block_solid.png"));
        GameRegistry.INSTANCE.registerTexture("texturePaddle", GameRegistry.class.getResource("/assets/textures/paddle.png"));
    }

    @Override
    public void run() {
        init();

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        loadTextures();
        loadLevels();

        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        sprite = new SpriteRenderer();

        while (!glfwWindowShouldClose(window)) {
            loop();
        }

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    private void loop() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        GameRegistry.INSTANCE.<Level>get("level1").draw(sprite);

        glfwSwapBuffers(window); // swap the color buffers

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
    }
}
