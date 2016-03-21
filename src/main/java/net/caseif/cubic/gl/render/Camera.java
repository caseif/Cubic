package net.caseif.cubic.gl.render;

import static net.caseif.cubic.util.MatrixHelper.*;

import net.caseif.cubic.math.matrix.Matrix4f;
import net.caseif.cubic.math.vector.Vector3f;
import net.caseif.cubic.util.MatrixHelper;

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
