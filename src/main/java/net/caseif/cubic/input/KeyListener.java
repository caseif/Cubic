package net.caseif.cubic.input;

import static java.awt.SystemColor.window;
import static net.caseif.cubic.gl.render.Camera.MOVE_DISTANCE;
import static org.lwjgl.glfw.GLFW.*;

import net.caseif.cubic.gl.GraphicsMain;

public class KeyListener {

    private final long window;

    public KeyListener(long window) {
        this.window = window;
    }

    public void poll() {
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            GraphicsMain.CAMERA.moveLeft(MOVE_DISTANCE);
        } else if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            GraphicsMain.CAMERA.moveRight(MOVE_DISTANCE);
        } else if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            GraphicsMain.CAMERA.moveForward(MOVE_DISTANCE);
        } else if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            GraphicsMain.CAMERA.moveBackward(MOVE_DISTANCE);
        } else if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
            GraphicsMain.CAMERA.moveUp(MOVE_DISTANCE);
        } else if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            GraphicsMain.CAMERA.moveDown(MOVE_DISTANCE);
        }
    }

}
