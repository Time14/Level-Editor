#version 400
#extension GL_ARB_texture_rectangle : enable
#extension GL_ARB_texture_non_power_of_two : enable

uniform mat4 m_transform;
uniform mat4 m_view;

uniform sampler2D t_sampler;
uniform sampler2DRect tr_sampler;

uniform bool b_rect;

in vec2 pass_texCoord;

layout(location = 0) out vec4 out_color;
void main() {
	if(b_rect) {
		out_color = texture2DRect(tr_sampler, pass_texCoord);
	} else {
		out_color = texture2D(t_sampler, pass_texCoord);
	}
}