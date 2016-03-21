#version 330

varying vec2 texCoord;

uniform sampler2D texture;

void main() {
    gl_FragColor = texture(texture, texCoord);
}
