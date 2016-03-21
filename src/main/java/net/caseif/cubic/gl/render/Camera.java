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

package net.caseif.cubic.gl.render;

import net.caseif.cubic.math.vector.Vector3f;
import net.caseif.cubic.util.helper.MatrixHelper;
import net.caseif.cubic.util.helper.NumberHelper;

import java.nio.FloatBuffer;

public class Camera {

    public static final float MOVE_DISTANCE = 0.02f;

    private Vector3f translation = new Vector3f(0f, 0f, 2f);
    private Vector3f rotation = new Vector3f(0f, 0f, 0f);

    public Camera() {
    }

    public void moveBackward(float units) {
        float dx = units * (float) -Math.sin(rotation.getY());
        float dz = units * (float) Math.cos(rotation.getY());
        translation = translation.add(dx, 0, dz);
    }

    public void moveForward(float units) {
        moveBackward(-units);
    }

    public void moveRight(float units) {
        float dx = units * (float) Math.cos(rotation.getY());
        float dz = units * (float) Math.sin(rotation.getY());
        translation = translation.add(dx, 0, dz);
    }

    public void moveLeft(float units) {
        moveRight(-units);
    }

    public void moveUp(float units) {
        translation = translation.add(0, units, 0);
    }

    public void moveDown(float units) {
        moveUp(-units);
    }

    public void addPitch(float pitch) {
        float newPitch = (float) NumberHelper.clamp(rotation.getX() + pitch, -Math.PI / 2, Math.PI / 2);
        rotation = new Vector3f(newPitch, rotation.getY(), rotation.getZ());
    }

    public void addYaw(float yaw) {
        float newYaw = (rotation.getY() + yaw) % (2 * (float) Math.PI);
        if (newYaw < 0) {
            newYaw += 2 * (float) Math.PI;
        }
        rotation = new Vector3f(rotation.getX(), newYaw, rotation.getZ());
    }

    public FloatBuffer getTranslationMatrix() {
        return MatrixHelper.getTranslationMatrix(translation.multiply(-1)).toBuffer();
    }

    public FloatBuffer getXRotation() {
        return MatrixHelper.getXRotationMatrix(rotation.getX()).toBuffer();
    }

    public FloatBuffer getYRotation() {
        return MatrixHelper.getYRotationMatrix(rotation.getY()).toBuffer();
    }

    public FloatBuffer getZRotation() {
        return MatrixHelper.getZRotationMatrix(rotation.getZ()).toBuffer();
    }

}
