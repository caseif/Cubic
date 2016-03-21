#version 330

varying vec2 texCoord;

uniform sampler2D texture;

void main() {
    if (texCoord[0] == 0.0) {
        gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
    } else {
        gl_FragColor = texture(texture, texCoord);
    }
}
