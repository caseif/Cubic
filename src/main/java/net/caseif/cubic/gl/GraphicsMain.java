/*
 * GLExperiments
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
import net.caseif.cubic.gl.render.Camera;
import net.caseif.cubic.gl.render.ShaderHelper;
import net.caseif.cubic.gl.render.SimpleWorldRenderer;
import net.caseif.cubic.gl.texture.Texture;
import net.caseif.cubic.input.KeyListener;
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

        // reset the window hints
        glfwDefaultWindowHints();
        // make it unresizble
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        // hide it until we're done initializing it
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);

        // create the window
        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "GL Experiments", NULL, NULL);

        // whoops
        if (window == NULL) {
            throw new RuntimeException("Could not create GLFW window");
        }

        // assign the default key callback
        glfwSetKeyCallback(window, keyCallback);

        keyListener = new KeyListener(window);

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
        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_VERTEX_ATTRIB_ARRAY_POINTER);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        //glOrtho(0, WINDOW_WIDTH, WINDOW_HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

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

        applyFrustum(znear, zfar, fov);

        glUseProgram(ShaderHelper.cameraShader);
        Matrix4f pr_matrix = MatrixHelper.perspective(znear, zfar, fov, (float) WINDOW_WIDTH / WINDOW_HEIGHT);
        glUniformMatrix4fv(glGetUniformLocation(ShaderHelper.cameraShader, "pr_matrix"), false, pr_matrix.toBuffer());
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

            // clear the screen
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            glRotatef((float) org.lwjgl.glfw.GLFW.glfwGetTime() * 30f, 1f, 1f, 1f);

            SimpleWorldRenderer.render(Main.world);

            // swap the buffers
            glfwSwapBuffers(window);

            // poll for events (like key events)
            glfwPollEvents();
        }
    }

    private void applyFrustum(float znear, float zfar, float fov) {
        double ymax = znear * Math.tan(fov * Math.PI / 360f);
        double xmax = ymax * (float) WINDOW_WIDTH / (float) WINDOW_HEIGHT;
        glFrustum(-xmax, xmax, -ymax, ymax, znear, zfar);
    }

    private void applyTextures() {
        Arrays.stream(BlockType.values()).forEach(Texture::registerTexture);
        ImageHelper.createAtlas();
    }

}
