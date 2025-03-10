precision mediump float;
varying vec2 texPos;
uniform sampler2D u_Texture;
void main() {
  vec4 col = texture2D(u_Texture, vec2(texPos.x, texPos.y));
  if(col.w < 0.01)
    discard;
  gl_FragColor = col;
}