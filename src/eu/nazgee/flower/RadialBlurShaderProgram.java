package eu.nazgee.flower;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramException;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

public class RadialBlurShaderProgram extends ShaderProgram {
	// ===========================================================
	// Constants
	// ===========================================================

	private static RadialBlurShaderProgram INSTANCE;

	public static final String VERTEXSHADER = 
	"uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
	"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION  + ";\n" +
	"attribute vec2 " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
	"varying vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
	"void main() {\n" +
	"	" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " = "+ ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" + 
	"	" + "gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
	"}";

	private static final String UNIFORM_FX_CENTER = "u_fx_center";
	private static final String UNIFORM_REGION_CENTER = "u_region_center";
	private static final String UNIFORM_RADIALBLUR_STRENGTH = "u_radialblur_strength";

	public static final String FRAGMENTSHADER =
	"precision lowp float;\n" +

	"uniform sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_0	+ ";\n"  +
	"varying mediump vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +

	"uniform vec2 " + RadialBlurShaderProgram.UNIFORM_FX_CENTER + ";\n" +
	"uniform vec2 " + RadialBlurShaderProgram.UNIFORM_REGION_CENTER + ";\n" +
	"uniform float " + RadialBlurShaderProgram.UNIFORM_RADIALBLUR_STRENGTH + ";\n" 	+

			"const float sampleDist = 48.0 / 1024.0;\n" +
			"const float sampleStrength = 1.0;\n" +

			"void main() {\n" +
			/* The actual (unburred) sample. */
			"	vec4 color = texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ");\n" +

			/* fragment => radial direction */
			"	vec2 direction = " + RadialBlurShaderProgram.UNIFORM_FX_CENTER + " - " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
			/* radial => center direction */
			"	vec2 dir_rad2cen = " + RadialBlurShaderProgram.UNIFORM_REGION_CENTER + " - " + RadialBlurShaderProgram.UNIFORM_FX_CENTER + ";\n" +

			/* Calculate the distance to the center of the blur. */
			"	float distance = sqrt(direction.x * direction.x + direction.y * direction.y);\n" +

			/* Normalize the direction (reuse the distance). */
			"	direction = direction / distance;\n" +

			//"	float t = sqrt(distance) * sampleStrength;\n" +
			//"	t = clamp(t, 0.0, 1.0);\n" + // 0 <= t >= 1

//			"	vec4 sum = color * sampleShare;\n" +
			"	vec2 directionSampleDist = direction * sampleDist;\n" +
			"	vec4 sum = texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " + "+ RadialBlurShaderProgram.UNIFORM_RADIALBLUR_STRENGTH +" * directionSampleDist);\n" +

			"	gl_FragColor = sum; \n" +

//			/* Weighten the blur effect with the distance to the center of the blur (further out is blurred more). */
//			"	float t = sqrt(distance) * sampleStrength;\n" +
//			"	t = clamp(t, 0.0, 1.0);\n" + // 0 <= t >= 1
//
//			/* Blend the original color with the averaged pixels. */
//			"	gl_FragColor = mix(color, sum, t);\n" +
			"}";

	// ===========================================================
	// Fields
	// ===========================================================

	public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexture0Location = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformFXCenterLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformFXStrength = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformRegionCenterLocation = ShaderProgramConstants.LOCATION_INVALID;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RadialBlurShaderProgram() {
		super(RadialBlurShaderProgram.VERTEXSHADER,
				RadialBlurShaderProgram.FRAGMENTSHADER);
	}

	public static RadialBlurShaderProgram getInstance() {
		if (RadialBlurShaderProgram.INSTANCE == null) {
			RadialBlurShaderProgram.INSTANCE = new RadialBlurShaderProgram();
		}
		return RadialBlurShaderProgram.INSTANCE;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void link(final GLState pGLState)
			throws ShaderProgramLinkException {
		GLES20.glBindAttribLocation(this.mProgramID,
				ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION,
				ShaderProgramConstants.ATTRIBUTE_POSITION);
		GLES20.glBindAttribLocation(this.mProgramID,
				ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION,
				ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES);

		super.link(pGLState);

		RadialBlurShaderProgram.sUniformModelViewPositionMatrixLocation = getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
		RadialBlurShaderProgram.sUniformTexture0Location = getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);

		RadialBlurShaderProgram.sUniformFXCenterLocation = getUniformLocation(RadialBlurShaderProgram.UNIFORM_FX_CENTER);
//		RadialBlurShaderProgram.sUniformRegionCenterLocation = getUniformLocation(RadialBlurShaderProgram.UNIFORM_REGION_CENTER);
		RadialBlurShaderProgram.sUniformFXStrength = getUniformLocation(RadialBlurShaderProgram.UNIFORM_RADIALBLUR_STRENGTH);
	}

	@Override
	public void bind(final GLState pGLState,
			final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);

		super.bind(pGLState, pVertexBufferObjectAttributes);

		GLES20.glUniformMatrix4fv(
				RadialBlurShaderProgram.sUniformModelViewPositionMatrixLocation,
				1, false, pGLState.getModelViewProjectionGLMatrix(), 0);
		GLES20.glUniform1i(RadialBlurShaderProgram.sUniformTexture0Location, 0);
	}

	@Override
	public void unbind(final GLState pGLState) throws ShaderProgramException {
		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);

		super.unbind(pGLState);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}