#version 330

in vec4 position;

varying vec2 texCoord;

uniform mat4 perspectiveMatrix; // perspective matrix (constant)
uniform mat4 transformMatrix; // transformation matrix (varies with camera state)

attribute vec2 in_texCoord;

void main() {
    gl_Position = perspectiveMatrix * transformMatrix * position;
    texCoord = in_texCoord;
}
