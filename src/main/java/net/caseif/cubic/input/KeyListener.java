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

import static net.caseif.cubic.gl.render.Camera.MOVE_DISTANCE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

import net.caseif.cubic.gl.GraphicsMain;
import net.caseif.cubic.util.helper.DeltaHelper;

public class KeyListener {

    private final long window;

    public KeyListener(long window) {
        this.window = window;
    }

    public void poll() {
        float dist = MOVE_DISTANCE * DeltaHelper.getDelta();
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            GraphicsMain.CAMERA.moveLeft(dist);
        }
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            GraphicsMain.CAMERA.moveRight(dist);
        }
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            GraphicsMain.CAMERA.moveForward(dist);
        }
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            GraphicsMain.CAMERA.moveBackward(dist);
        }
        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
            GraphicsMain.CAMERA.moveUp(dist);
        }
        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            GraphicsMain.CAMERA.moveDown(dist);
        }
    }

}
