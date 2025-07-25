precision highp float;

attribute vec4 vPosition;
uniform mat4 matrix;
varying vec2 texPos;
void main() {
    gl_Position = matrix * vPosition;
    texPos = vec2(vPosition.x, vPosition.y);
}