package cc.lasmgratel.breakout.render;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3fc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class Shader {
    private static final Logger LOGGER = LoggerFactory.getLogger("Shader");
    private int programId;

    public Shader(String vertexShader, String fragmentShader, String geometryShader) {
        int sVertex, sFragment, gShader = 0;
        // Vertex Shader
        sVertex = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(sVertex, vertexShader);
        glCompileShader(sVertex);
        checkErrors(sVertex, "VERTEX");
        // Fragment Shader
        sFragment = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(sFragment, fragmentShader);
        glCompileShader(sFragment);
        checkErrors(sFragment, "FRAGMENT");
        // If geometry shader source code is given, also compile geometry shader
        if (geometryShader != null && !geometryShader.isEmpty()) {
            gShader = glCreateShader(GL_GEOMETRY_SHADER);
            glShaderSource(gShader, geometryShader);
            glCompileShader(gShader);
            checkErrors(gShader, "GEOMETRY");
        }
        // Shader Program
        programId = glCreateProgram();
        glAttachShader(programId, sVertex);
        glAttachShader(programId, sFragment);
        if (geometryShader != null && !geometryShader.isEmpty())
            glAttachShader(programId, gShader);
        glLinkProgram(programId);
        checkErrors(programId, "PROGRAM");
        // Delete the shaders as they're linked into our program now and no longer necessery
        glDeleteShader(sVertex);
        glDeleteShader(sFragment);
        if (geometryShader != null && !geometryShader.isEmpty())
            glDeleteShader(gShader);
    }

    public void use() {
        glUseProgram(programId);
    }

    private void checkErrors(int object, String type) {
        IntBuffer success = IntBuffer.allocate(1);
        if (type.equals("PROGRAM")) {
            glGetShaderiv(object, GL_COMPILE_STATUS, success);
            if (success.get() == 0)
                LOGGER.error("Shader compile time error with type {}: {}", type, glGetShaderInfoLog(object));
        } else {
            glGetShaderiv(object, GL_LINK_STATUS, success);
            if (success.get() == 0)
                LOGGER.error("Shader link time error with type {}: {}", type, glGetShaderInfoLog(object));
        }
    }

    public void setMatrix4(String name, Matrix4fc matrix) {
        glUniformMatrix4fv(glGetUniformLocation(programId, name), false, matrix.get(new float[4*4]));
    }

    public void setVector3f(String name, Vector3fc vector) {
        glUniform3fv(glGetUniformLocation(programId, name), vector.get(ByteBuffer.allocateDirect(3 * Float.BYTES).asFloatBuffer()));
    }

    public void setInteger(String name, int i) {
        glUniform1i(glGetUniformLocation(programId, name), i);
    }
}