#version 330 core

uniform mat4 perspectiveMatrix; // perspective matrix (constant)
uniform mat4 translationMatrix; // translation matrix (varies with camera state)
uniform mat4 rotXMatrix; // x-rotation matrix (varies with camera state)
uniform mat4 rotYMatrix; // y-rotation matrix (varies with camera state)
uniform mat4 rotZMatrix; // z-rotation matrix (varies with camera state)

out vec3 texCoord;

attribute vec4 in_position;
attribute vec3 in_texCoord;

void main() {
    gl_Position = perspectiveMatrix * rotXMatrix * rotYMatrix * rotZMatrix * translationMatrix * in_position;
    texCoord = in_texCoord;
}
