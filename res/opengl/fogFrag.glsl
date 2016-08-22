#version 400

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D colourTexture;
uniform sampler2D depthTexture;
//uniform sampler2D omitSkyTexture;

const float density = 0.07;
const float gradient = 1.5;
const float near = 0.1;
const float far = 1000.0;

void main() {
  //float depth = texture(depthTexture, textureCoords).r;
  //depth = (2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near))) + 0.2;
  //depth = clamp(depth, 0.0, 1.0);
  //float visibility = exp(-pow((depth * density), gradient));
  //visibility = clamp(visibility,0.0,1.0);
  out_Color = texture(colourTexture, textureCoords);
  out_Color = texture(depthTexture, textureCoords);
  //out_Color = texture(omitSkyTexture, textureCoords);
  //out_Color = mix(out_Color, vec4(0.01,0.01,0.01,1.0), visibility);
}
