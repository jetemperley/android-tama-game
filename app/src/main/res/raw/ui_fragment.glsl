precision mediump float;
varying vec2 texPos;
uniform sampler2D u_Texture;
uniform vec3 tintColor;
void main() {
  vec4 col = texture2D(u_Texture, vec2(texPos.x, texPos.y));
  gl_FragColor = vec4(
    col.x*tintColor.x,
    col.y*tintColor.y,
    col.z*tintColor.z,
    1);
}