attribute vec4 vPosition;
varying vec2 texPos;
void main() {
  gl_Position = vPosition;
  texPos = vec2(vPosition.x+0.5, vPosition.y+0.5);
}