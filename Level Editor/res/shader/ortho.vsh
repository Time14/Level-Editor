#version 400

uniform mat4 m_projection;
uniform mat4 m_transform;
uniform mat4 m_view;

uniform float f_scale;
uniform bool b_scale;

layout(location = 0) in vec2 in_position;
layout(location = 1) in vec2 in_texCoord;

out vec2 pass_texCoord;
void main() {

	vec2 scaledPosition = in_position;

	mat4 scaledTransform = m_transform;

	if(b_scale) {
		scaledPosition *= f_scale;
		scaledTransform[3][0] *= f_scale;
		scaledTransform[3][1] *= f_scale;
	}


	gl_Position = m_projection * scaledTransform * m_view * vec4(scaledPosition, 0, 1);
	pass_texCoord = in_texCoord;
}