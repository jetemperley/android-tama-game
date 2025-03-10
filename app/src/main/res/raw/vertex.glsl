attribute vec4 vPosition;
uniform mat4 matrix;
varying vec2 texPos;
void main() {
  gl_Position = matrix * vPosition;
  texPos = vec2(vPosition.x+0.5, vPosition.y+0.5);
}