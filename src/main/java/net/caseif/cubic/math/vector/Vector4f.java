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

package net.caseif.cubic.math.vector;

import java.util.Objects;

public class Vector4f {

    private final float x;
    private final float y;
    private final float z;
    private final float w;

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getW() {
        return w;
    }

    public Vector4f add(float x, float y, float z, float w) {
        return new Vector4f(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ", " + getW() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector4f)) {
            return false;
        }
        Vector4f vec = (Vector4f) obj;
        return this.x == vec.x && this.y == vec.y && this.z == vec.z && this.w == vec.w;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }

}
