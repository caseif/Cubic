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
import static org.lwjgl.system.MemoryUtil.NULL;

import net.caseif.cubic.gl.callback.KeyCallback;
import net.caseif.cubic.gl.render.SimpleWorldRenderer;
import net.caseif.cubic.math.Vector2f;
import net.caseif.cubic.math.Vector3f;
import net.caseif.cubic.world.Chunk;
import net.caseif.cubic.world.World;
import net.caseif.cubic.world.block.Block;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class GraphicsMain implements Runnable {

    private static final int WINDOW_WIDTH = 640;
    private static final int WINDOW_HEIGHT = 480;

    private GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
    private GLFWKeyCallback keyCallback = new KeyCallback();

    private long window;

    private World world;

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
        window = glfwCreateWindow(640, 480, "GL Experiments", NULL, NULL);

        // whoops
        if (window == NULL) {
            throw new RuntimeException("Could not create GLFW window");
        }

        // assign the default key callback
        glfwSetKeyCallback(window, keyCallback);

        // get the video mode of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // center the window
        glfwSetWindowPos(window, (vidmode.width() - WINDOW_WIDTH) / 2, (vidmode.height() - WINDOW_HEIGHT) / 2);

        // set the current context
        glfwMakeContextCurrent(window);
        // enable vsync
        glfwSwapInterval(1);

        //GLContext.createFromCurrent();

        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        // show the window
        glfwShowWindow(window);

        createDummyWorld();
    }

    private void deinit() {
        glfwTerminate();
        errorCallback.release();
    }

    private void mainLoop() {

        GL.createCapabilities();

        glClearColor(0f, 0f, 0f, 0f);

        while (glfwWindowShouldClose(window) == GL_FALSE) {
            glViewport(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

            // clear the screen
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            float ratio = WINDOW_WIDTH / WINDOW_HEIGHT;
            glOrtho(-ratio, ratio, -1f, 1f, 50f, -50f);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            glRotatef((float) GLFW.glfwGetTime() * 30f, 0f, 1f, 1f);

            SimpleWorldRenderer.render(world);

            // swap the buffers
            glfwSwapBuffers(window);

            // poll for events (like key events)
            glfwPollEvents();
        }
    }

    public void createDummyWorld() {
        world = new World("world");
        Chunk chunk = new Chunk(world, new Vector2f(0, 0));
        world.addChunk(chunk);
        chunk.getBlocks()[0][0][0] = new Block(chunk, new Vector3f(0, 0, 0));
    }

}
