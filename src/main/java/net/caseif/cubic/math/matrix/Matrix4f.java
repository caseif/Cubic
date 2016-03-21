package net.caseif.cubic.math.matrix;

import net.caseif.cubic.math.vector.Vector3f;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

@SuppressWarnings("PointlessArithmeticExpression")
public class Matrix4f {

    public static final int SIZE = 4 * 4;

    private float[] elements = new float[SIZE];

    public Matrix4f(float[] values) {
        this.elements = values;
    }

    // magic, do not touch
    public Matrix4f multiply(Matrix4f matrix){
        float[] elements = new float[SIZE];
        for(int x = 0; x < 4; x++){
            for(int y = 0; y < 4; y++){
                float sum = 0.0f;
                for(int e = 0; e < 4; e++){
                    sum += this.elements[x + e * 4] * matrix.elements[e + y * 4];
                }
                elements[x + y * 4] = sum;
            }
        }
        return new Matrix4f(elements);
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
