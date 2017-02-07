// NOTE - does not work properly. Gets color values between 0.0 and 1.0.
// Tries to look up corresponding color, but it's between 0 and 255.
// I'm leaving this shader in the project only as an example to look back on.
// Not to mention, it makes the game run incredibly slowly.

uniform sampler2D u_texture;
uniform sampler2D u_texture1;

varying vec4 v_color;
varying vec2 v_texCoords;

void main()
{
	vec4 thisColor = texture2D(u_texture, v_texCoords);
	vec4 swappedColor = texture2D(u_texture1, vec2(thisColor.r, 0.0));

	if(swappedColor.a == 0.0)
	{
		gl_FragColor = v_color * thisColor;
	}
	else
	{
		gl_FragColor = v_color * swappedColor;
	}
}
