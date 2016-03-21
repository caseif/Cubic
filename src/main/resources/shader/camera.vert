#version 330

in vec4 position;

varying vec2 texCoord;

uniform mat4 perspectiveMatrix; // perspective matrix (constant)
uniform mat4 translationMatrix; // translation matrix (varies with camera state)
uniform mat4 rotXMatrix; // x-rotation matrix (varies with camera state)
uniform mat4 rotYMatrix; // y-rotation matrix (varies with camera state)
uniform mat4 rotZMatrix; // z-rotation matrix (varies with camera state)

attribute vec2 in_texCoord;

void main() {
    gl_Position = perspectiveMatrix * rotXMatrix * rotYMatrix * rotZMatrix * translationMatrix * position;
    texCoord = in_texCoord;
}
