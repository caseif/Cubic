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

package net.caseif.cubic.input;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

import net.caseif.cubic.gl.GraphicsMain;

import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

public class MouseListener {

    private static final float LOOK_RESOLUTION = 0.01f;

    private long window;

    private DoubleBuffer cursorX = (DoubleBuffer) BufferUtils.createDoubleBuffer(1).put(Double.NaN).rewind();
    private DoubleBuffer cursorY = (DoubleBuffer) BufferUtils.createDoubleBuffer(1).put(Double.NaN).rewind();

    public MouseListener(long window) {
        this.window = window;
    }

    public void poll() {
        double prevX = cursorX.get();
        double prevY = cursorY.get();
        rewindBuffers();
        if (prevX != prevX && prevY != prevY) {
            glfwGetCursorPos(window, cursorX, cursorY);
            return;
        }
        glfwGetCursorPos(window, cursorX, cursorY);
        int deltaX = (int) Math.floor(cursorX.get() - prevX);
        int deltaY = (int) Math.floor(cursorY.get() - prevY);
        rewindBuffers();

        if (deltaY != 0) {
            GraphicsMain.CAMERA.addPitch(deltaY * LOOK_RESOLUTION);
        }
        if (deltaX != 0) {
            GraphicsMain.CAMERA.addYaw(deltaX * LOOK_RESOLUTION);
        }
    }

    private void rewindBuffers() {
        cursorX.rewind();
        cursorY.rewind();
    }

}
