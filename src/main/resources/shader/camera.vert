#version 330

in vec4 position;

uniform mat4 orthoTransform;

void main() {
    gl_Position = position * orthoTransform;
}
