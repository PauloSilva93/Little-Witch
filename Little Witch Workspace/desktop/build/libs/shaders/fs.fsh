#version 330 core

in vec4 v_color;
in vec2 v_texCoord0;
out vec4 fragColor;

uniform sampler2D u_sampler2D;

void main()
{
	fragColor = texture2D(u_sampler2D, v_texCoord0) * v_color + vec4(0.0, 0.0, 0.2, 0.0);
}