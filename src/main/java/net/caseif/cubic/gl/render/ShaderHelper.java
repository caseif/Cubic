package net.caseif.cubic.gl.render;

import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShaderHelper {

    public static int cameraShader;

    public static void initCameraShader() throws IOException {
        cameraShader = createShaderProgram("/shader/camera.vert", "/shader/camera.frag");
    }

    private static int createShaderProgram(String vertShaderSource, String fragShaderSource) throws IOException {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        int fragShader = glCreateShader(GL_FRAGMENT_SHADER);

        compileShader(vertexShader, vertShaderSource);
        compileShader(fragShader, fragShaderSource);

        return linkProgram(vertexShader, fragShader);
    }

    private static void compileShader(int shaderHandle, String resource) throws IOException {
        InputStream is = ShaderHelper.class.getResourceAsStream(resource);
        char[] chars = new char[is.available()];
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        br.read(chars);
        glShaderSourceARB(shaderHandle, new String(chars));

        glCompileShaderARB(shaderHandle);
        int result = glGetObjectParameteriARB(shaderHandle, GL_COMPILE_STATUS);
        if (result == GL_FALSE) {
            System.err.println(glGetInfoLogARB(shaderHandle));
            throw new RuntimeException("Failed to compile GLSL shader from \"" + resource + "\"");
        }
    }

    private static int linkProgram(int vertShader, int fragShader) {
        int programHandle = glCreateProgram();
        glAttachShader(programHandle, vertShader);
        glAttachShader(programHandle, fragShader);
        glLinkProgram(programHandle);

        if (glGetProgrami(programHandle, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println(glGetInfoLogARB(programHandle));
            throw new RuntimeException("Failed to link shader program!");
        }

        return programHandle;
    }

}
