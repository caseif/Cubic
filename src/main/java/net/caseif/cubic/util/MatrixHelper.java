package net.caseif.cubic.util;

import static net.caseif.cubic.math.matrix.Matrix4f.SIZE;

import net.caseif.cubic.math.matrix.Matrix4f;
import net.caseif.cubic.math.vector.Vector3f;

@SuppressWarnings("PointlessArithmeticExpression")
public class MatrixHelper {

    public static final Matrix4f IDENTITY = new Matrix4f(new float[] {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
    });

    public static Matrix4f perspective(float near, float far,
                                       float FoV, float aspect){
        float y2 = near * (float)Math.tan(Math.toRadians(FoV));
        float y1 = -y2;
        float x1 = y1 * aspect;
        float x2 = y2 * aspect;
        return frustum(x1, x2, y1, y2, near, far);
    }

    public static Matrix4f frustum(float left, float right, float bottom, float top, float near, float far){
        float[] elements = new float[SIZE];

        elements[0 + 0 * 4] =  (2 * near) / (right - left);
        elements[1 + 1 * 4] =  (2 * near) / (top - bottom);
        elements[3 + 2 * 4] =  (2 * near * far) / (near - far);
        elements[2 + 2 * 4] =  (near + far) / ( near - far);
        elements[3 + 3 * 4] = 0;
        elements[2 + 3 * 4] = -1.0f;

        elements[0 + 2 * 4] = (right + left) / (right - left);
        elements[1 + 2 * 4] = (top + bottom) / (top - bottom);
        elements[2 + 2 * 4] = (near + far) / (near - far);

        return new Matrix4f(elements);
    }

    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far){
        float[] elements = new float[SIZE];

        elements[0 + 0 * 4] = 2.0f / (right - left);
        elements[1 + 1 * 4] = 2.0f / (top - bottom);
        elements[2 + 2 * 4] = 2.0f / (near - far);

        elements[0 + 3 * 4] = (left + right) / (left - right);
        elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
        elements[2 + 3 * 4] = (far + near) / (far - near);

        elements[3 + 3 * 4] = 1.0f;

        return new Matrix4f(elements);
    }

    public static Matrix4f getTranslationMatrix(Vector3f translation) {
        float[] elements = new float[16];
        elements[0 + 3 * 4] = translation.getX();
        elements[1 + 3 * 4] = translation.getY();
        elements[2 + 3 * 4] = translation.getZ();
        return new Matrix4f(elements).suppliment(IDENTITY);
    }

    public static Matrix4f getXRotationMatrix(float rotationRadians) {
        float[] elements = new float[16];
        float cos = (float) Math.cos(rotationRadians);
        float sin = (float) Math.sin(rotationRadians);
        elements[1 + 1 * 4] = cos;
        elements[2 + 1 * 4] = sin;
        elements[1 + 2 * 4] = -sin;
        elements[2 + 2 * 4] = cos;
        return new Matrix4f(elements).suppliment(IDENTITY);
    }

    public static Matrix4f getYRotationMatrix(float rotationRadians) {
        float[] elements = new float[16];
        float cos = (float) Math.cos(rotationRadians);
        float sin = (float) Math.sin(rotationRadians);
        elements[0 + 0 * 4] = cos;
        elements[2 + 0 * 4] = -sin;
        elements[0 + 2 * 4] = sin;
        elements[2 + 2 * 4] = cos;
        return new Matrix4f(elements).suppliment(IDENTITY);
    }

    public static Matrix4f getZRotationMatrix(float rotationRadians) {
        float[] elements = new float[16];
        float cos = (float) Math.cos(rotationRadians);
        float sin = (float) Math.sin(rotationRadians);
        elements[0 + 0 * 4] = cos;
        elements[1 + 0 * 4] = -sin;
        elements[0 + 1 * 4] = sin;
        elements[1 + 1 * 4] = cos;
        return new Matrix4f(elements).suppliment(IDENTITY);
    }

}
