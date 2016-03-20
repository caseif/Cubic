package net.caseif.cubic.gl.render;

import net.caseif.cubic.math.matrix.Matrix4f;
import net.caseif.cubic.math.vector.Vector3f;

import java.nio.FloatBuffer;

public class Camera {

    private Matrix4f matrix;

    private Vector3f translation = new Vector3f(0f, 0f, 0f);
    private Vector3f rotation = new Vector3f(0f, 0f, 0f);

    public Camera() {
        matrix = Matrix4f.orthographic(-10f, 10f, -10f * (9f / 16f), 10 * (9f / 16f), -10f, 10f);
    }

    public void moveBackward(float units) {
        translation = translation.add(0, 0, units);
        matrix.translate(translation);
    }

    public void moveForward(float units) {
        moveBackward(-units);
    }

    public void moveRight(float units) {
        translation = translation.add(units, 0, 0);
        matrix.translate(translation);
    }

    public void moveLeft(float units) {
        moveRight(-units);
    }

    public void setPitch(float pitch) {
        rotation = new Vector3f(pitch, rotation.getY(), rotation.getZ());
        matrix.rotate(rotation);
    }

    public void setYaw(float yaw) {
        rotation = new Vector3f(rotation.getX(), yaw, rotation.getZ());
        matrix.rotate(rotation);
    }

    public FloatBuffer getLocationBuffer() {
        return matrix.toBuffer();
    }

}
