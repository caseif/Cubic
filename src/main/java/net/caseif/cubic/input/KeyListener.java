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

package net.caseif.cubic.input;

import static org.lwjgl.glfw.GLFW.*;

import net.caseif.cubic.Main;
import net.caseif.cubic.gl.GraphicsMain;
import net.caseif.cubic.math.vector.Vector3f;

public class KeyListener {

    private final long window;

    public KeyListener(long window) {
        this.window = window;
    }

    public void poll() {
        float speed = Main.player.getSpeed();
        float vx = 0f;
        float vy = 0f;
        float vz = 0f;
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            vx -= speed * (float) Math.cos(GraphicsMain.CAMERA.getRotation().getY());
            vz -= speed * (float) Math.sin(GraphicsMain.CAMERA.getRotation().getY());
        }
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            vx += speed * (float) Math.cos(GraphicsMain.CAMERA.getRotation().getY());
            vz += speed * (float) Math.sin(GraphicsMain.CAMERA.getRotation().getY());
        }
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            vx -= speed * (float) -Math.sin(GraphicsMain.CAMERA.getRotation().getY());
            vz -= speed * (float) Math.cos(GraphicsMain.CAMERA.getRotation().getY());
        }
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            vx += speed * (float) -Math.sin(GraphicsMain.CAMERA.getRotation().getY());
            vz += speed * (float) Math.cos(GraphicsMain.CAMERA.getRotation().getY());
        }
        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            vy = -speed;
        }
        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
            vy = speed;
        }

        Main.player.setVelocity(new Vector3f(vx, vy, vz));
    }

}
