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

package net.caseif.cubic.math.matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

@SuppressWarnings("PointlessArithmeticExpression")
public class Matrix4f {

    public static final int SIZE = 4 * 4;

    private float[] elements = new float[SIZE];

    public Matrix4f(float[] values) {
        this.elements = values;
    }

    public Matrix4f suppliment(Matrix4f matrix) {
        float[] newElements = new float[SIZE];
        for (int i = 0; i < SIZE; i++) {
            newElements[i] = elements[i] == 0 ? matrix.elements[i] : elements[i];
        }
        return new Matrix4f(newElements);
    }

    public FloatBuffer toBuffer() {
        FloatBuffer fb = ByteBuffer.allocateDirect(elements.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fb.put(elements).flip();
        return fb;
    }

}
