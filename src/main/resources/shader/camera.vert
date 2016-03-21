#version 330

in vec4 position;

uniform mat4 pr_matrix; // perspective matrix (constant)

uniform mat4 orthoTransform; // orthographic matrix (varies with camera state)

void main() {
    gl_Position = pr_matrix * orthoTransform * position;
}
