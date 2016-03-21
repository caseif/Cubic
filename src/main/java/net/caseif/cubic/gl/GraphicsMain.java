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

package net.caseif.cubic.gl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import net.caseif.cubic.Main;
import net.caseif.cubic.gl.callback.KeyCallback;
import net.caseif.cubic.gl.render.BlockRenderer;
import net.caseif.cubic.gl.render.Camera;
import net.caseif.cubic.gl.render.ShaderHelper;
import net.caseif.cubic.gl.texture.Texture;
import net.caseif.cubic.input.KeyListener;
import net.caseif.cubic.input.MouseListener;
import net.caseif.cubic.math.matrix.Matrix4f;
import net.caseif.cubic.util.helper.ImageHelper;
import net.caseif.cubic.util.helper.MatrixHelper;
import net.caseif.cubic.world.block.BlockType;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.io.IOException;
import java.util.Arrays;

public class GraphicsMain implements Runnable {

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 600;
    public static final Camera CAMERA = new Camera();

    private GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
    private GLFWKeyCallback keyCallback = new KeyCallback();
    private KeyListener keyListener;
    private MouseListener mouseListener;

    private long window;

    public void run() {
        try {
            init();
            mainLoop();

            glfwDestroyWindow(window);
            keyCallback.release();
        } finally {
            deinit();
        }
    }

    private void init() {
        // set the error callback
        glfwSetErrorCallback(errorCallback);

        // whoops
        if (glfwInit() != GL_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // reset the window hints
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE); // make it unresizble
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // hide it until we're done initializing it
        //glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3); // using OpenGL 3.3
        //glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        //glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); // use core 3.3 profile

        // create the window
        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Cubic", NULL, NULL);

        // whoops
        if (window == NULL) {
            throw new RuntimeException("Could not create GLFW window");
        }

        glfwSetKeyCallback(window, keyCallback);
        keyListener = new KeyListener(window);
        mouseListener = new MouseListener(window);
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        // get the video mode of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // center the window
        glfwSetWindowPos(window, (vidmode.width() - WINDOW_WIDTH) / 2, (vidmode.height() - WINDOW_HEIGHT) / 2);

        // set the current context
        glfwMakeContextCurrent(window);
        // enable vsync
        glfwSwapInterval(1);

        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glViewport(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        applyTextures();

        try {
            ShaderHelper.initCameraShader();
        } catch (IOException ex) {
            System.err.println("Failed to initialize shaders!");
            ex.printStackTrace();
        }

        float fov = 15f;
        float znear = 1f;
        float zfar = 10f;

        glUseProgram(ShaderHelper.cameraShader);
        Matrix4f prMatrix = MatrixHelper.perspective(znear, zfar, fov, (float) WINDOW_WIDTH / WINDOW_HEIGHT);
        glUniformMatrix4fv(glGetUniformLocation(ShaderHelper.cameraShader, "perspectiveMatrix"), false,
                prMatrix.toBuffer());
        glUseProgram(0);

        // show the window
        glfwShowWindow(window);
    }

    private void deinit() {
        glfwTerminate();
        errorCallback.release();
    }

    private void mainLoop() {

        GL.createCapabilities();

        glClearColor(1f, 1f, 0f, 1f);

        while (glfwWindowShouldClose(window) == GL_FALSE) {
            keyListener.poll();
            mouseListener.poll();

            // clear the screen
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            BlockRenderer.render(Main.world);

            // swap the buffers
            glfwSwapBuffers(window);

            // poll for events (like key events)
            glfwPollEvents();
        }
    }

    private void applyTextures() {
        Arrays.stream(BlockType.values()).forEach(Texture::registerTexture);
        ImageHelper.createAtlas();
    }

}
