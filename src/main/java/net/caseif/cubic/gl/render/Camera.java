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

import static net.caseif.cubic.util.helper.MatrixHelper.*;

import net.caseif.cubic.math.matrix.Matrix4f;
import net.caseif.cubic.math.vector.Vector3f;
import net.caseif.cubic.util.helper.MatrixHelper;

import java.nio.FloatBuffer;

public class Camera {

    public static final float MOVE_DISTANCE = 0.02f;

    private Matrix4f matrix;

    private Vector3f translation = new Vector3f(0f, 0f, -2f);
    private Vector3f rotation = new Vector3f(0f, 0f, 0f);

    public Camera() {
        //matrix = Matrix4f.orthographic(-10f, 10f, -10f * (9f / 16f), 10 * (9f / 16f), -10f, 10f);
        matrix = new Matrix4f(new float[Matrix4f.SIZE]).suppliment(MatrixHelper.IDENTITY);
    }

    public void moveForward(float units) {
        translation = translation.add(0, 0, units);
    }

    public void moveBackward(float units) {
        moveForward(-units);
    }

    public void moveLeft(float units) {
        translation = translation.add(units, 0, 0);
    }

    public void moveRight(float units) {
        moveLeft(-units);
    }

    public void moveDown(float units) {
        translation = translation.add(0, units, 0);
    }

    public void moveUp(float units) {
        moveDown(-units);
    }

    public void setPitch(float pitch) {
        rotation = new Vector3f(pitch, rotation.getY(), rotation.getZ());
    }

    public void setYaw(float yaw) {
        rotation = new Vector3f(rotation.getX(), yaw, rotation.getZ());
    }

    private void updateMatrix() {
        matrix =  getTranslationMatrix(translation)
                .multiply(getZRotationMatrix(rotation.getZ()))
                .multiply(getYRotationMatrix(rotation.getY()))
                .multiply(getXRotationMatrix(rotation.getX()));
    }

    public FloatBuffer getOrthoMatrix() {
        updateMatrix();
        return matrix.toBuffer();
    }

}
