/*
 * Cubic
 * Copyright (c) 2016, Maxim Roncace <me@caseif.net>
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.caseif.cubic.gl.render;

import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
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
