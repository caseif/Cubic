package net.caseif.cubic.math.matrix;

import net.caseif.cubic.math.vector.Vector3f;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.Arrays;

@SuppressWarnings("PointlessArithmeticExpression")
public class Matrix4f {

    private static final int SIZE = 4 * 4;

    private float[] elements = new float[SIZE];

    private Matrix4f() {
    }

    public static Matrix4f identity(){
        Matrix4f matrix = new Matrix4f();
        matrix.elements[0 + 0 * 4] = 1.0f; // | 1 0 0 0 |
        matrix.elements[1 + 1 * 4] = 1.0f; // | 0 1 0 0 |
        matrix.elements[2 + 2 * 4] = 1.0f; // | 0 0 1 0 |
        matrix.elements[3 + 3 * 4] = 1.0f; // | 0 0 0 1 |
        return matrix;
    }

    public static Matrix4f perspective(float left, float right, float bottom, float top, float near, float far,
            float FoV, float aspect){
        float y2 = near * (float)Math.tan(Math.toRadians(FoV));
        float y1 = -y2;
        float x1 = y1 * aspect;
        float x2 = y2 * aspect;
        return frustrum(x1, x2, y1, y2, near, far);
    }

    public static Matrix4f frustrum(float left, float right, float bottom, float top, float near, float far){
        Matrix4f matrix = new Matrix4f();

        matrix.elements[0 + 0 * 4] =  (2 * near) / (right - left);
        matrix.elements[1 + 1 * 4] =  (2 * near) / (top - bottom);
        matrix.elements[3 + 2 * 4] =  (2 * near * far) / (near - far);
        matrix.elements[2 + 2 * 4] =  (near + far) / ( near - far);
        matrix.elements[3 + 3 * 4] = 0;
        matrix.elements[2 + 3 * 4] = -1.0f;

        matrix.elements[0 + 2 * 4] = (right + left) / (right - left);
        matrix.elements[1 + 2 * 4] = (top + bottom) / (top - bottom);
        matrix.elements[2 + 2 * 4] = (near + far) / (near - far);

        return matrix;
    }

    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far){
        Matrix4f matrix = new Matrix4f();

        matrix.elements[0 + 0 * 4] = 2.0f / (right - left);
        matrix.elements[1 + 1 * 4] = 2.0f / (top - bottom);
        matrix.elements[2 + 2 * 4] = 2.0f / (near - far);

        matrix.elements[0 + 3 * 4] = (left + right) / (left - right);
        matrix.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
        matrix.elements[2 + 3 * 4] = (far + near) / (far - near);

        matrix.elements[3 + 3 * 4] = 1.0f;

        return matrix;
    }

    public void transform(Vector3f translation, Vector3f rotation) {
        this.translate(translation);
        this.rotate(rotation);
    }

    public void translate(Vector3f translation) {
        this.multiply(getTranslationMatrix(translation));
    }

    public void rotate(Vector3f rotation) {
        this.multiply(getRotationMatrix(rotation));
    }

    private Matrix4f getTranslationMatrix(Vector3f translation){
        Matrix4f matrix = identity();
        matrix.elements[0 + 3 * 4] = translation.getX();
        matrix.elements[1 + 3 * 4] = translation.getY();
        matrix.elements[2 + 3 * 4] = translation.getZ();
        return matrix;
    }

    private Matrix4f getRotationMatrix(Vector3f rotation){
        return identity()
                .multiply(Matrix4f.getXRotationMatrix(rotation.getX()))
                .multiply(Matrix4f.getYRotationMatrix(rotation.getY()))
                .multiply(Matrix4f.getZRotationMatrix(rotation.getZ()));
    }

    private static Matrix4f getXRotationMatrix(float angle){
        Matrix4f matrix = identity();
        float r = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(r);
        float sin = (float) Math.sin(r);

        matrix.elements[1 + 1 * 4] = cos;
        matrix.elements[1 + 2 * 4] = -sin;
        matrix.elements[2 + 1 * 4] = sin;
        matrix.elements[2 + 2 * 4] = cos;

        return matrix;
    }

    private static Matrix4f getYRotationMatrix(float angle){
        Matrix4f matrix = identity();
        float r = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(r);
        float sin = (float) Math.sin(r);

        matrix.elements[0 + 0 * 4] = cos;
        matrix.elements[2 + 0 * 4] = -sin;
        matrix.elements[0 + 2 * 4] = sin;
        matrix.elements[2 + 2 * 4] = cos;

        return matrix;
    }

    private static Matrix4f getZRotationMatrix(float angle){
        Matrix4f matrix = identity();
        float r = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(r);
        float sin = (float) Math.sin(r);

        matrix.elements[0 + 0 * 4] = cos;
        matrix.elements[1 + 0 * 4] = sin;
        matrix.elements[0 + 1 * 4] = -sin;
        matrix.elements[1 + 1 * 4] = cos;
        return matrix;
    }

    // magic, do not touch
    // this might actually be a cross product but I'm too tired to discern whether it actually is
    private Matrix4f multiply(Matrix4f matrix){
        Matrix4f result = new Matrix4f();
        for(int x = 0; x < 4; x++){
            for(int y = 0; y < 4; y++){
                float sum = 0.0f;
                for(int e = 0; e < 4; e++){
                    sum += this.elements[x + e * 4] * matrix.elements[e + y * 4];
                }
                result.elements[x + y * 4] = sum;
            }
        }
        this.elements = result.elements;
        return this;
    }

    public FloatBuffer toBuffer() {
        FloatBuffer fb = BufferUtils.createFloatBuffer(SIZE);
        fb.put(elements);
        fb.flip();
        return fb;
    }

}
