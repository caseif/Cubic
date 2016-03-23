#version 330 core

uniform sampler2DArray texArray;

in vec3 texCoord;

void main() {
    gl_FragColor = texture(texArray, texCoord);
}
