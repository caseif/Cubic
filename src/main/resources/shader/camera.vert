#version 330

in vec4 position;

varying vec2 texCoord;

uniform mat4 pr_matrix; // perspective matrix (constant)
uniform mat4 orthoTransform; // orthographic matrix (varies with camera state)

attribute vec2 in_texCoord;

void main() {
    gl_Position = pr_matrix * orthoTransform * position;
    texCoord = in_texCoord;
}
