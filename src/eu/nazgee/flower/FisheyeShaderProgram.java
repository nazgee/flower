package eu.nazgee.flower;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramException;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

public class FisheyeShaderProgram extends ShaderProgram {
	// ===========================================================
	// Constants
	// ===========================================================

	private static FisheyeShaderProgram INSTANCE;

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
	private static final String UNIFORM_REGION_SIZE = "u_region_size";
	private static final String UNIFORM_FX_STRENGTH = "u_fx_strength";

	public static final String FRAGMENTSHADER =
	"precision lowp float;\n" +

	"uniform sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_0	+ ";\n"  +
	"varying mediump vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +

	"uniform vec2 " + FisheyeShaderProgram.UNIFORM_FX_CENTER + ";\n" +
	"uniform float " + FisheyeShaderProgram.UNIFORM_FX_STRENGTH + ";\n" 	+
	"uniform vec2 " + FisheyeShaderProgram.UNIFORM_REGION_CENTER + ";\n" +
	"uniform vec2 " + FisheyeShaderProgram.UNIFORM_REGION_SIZE + ";\n" +

	"const float sampleDist = 48.0 / 1024.0;\n" +
	"const float sampleStrength = 1.0;\n" +

	"float f1(float seed, vec2 centeroffset) {\n" +
	"	return exp(0.5 + 20.0*seed * length(centeroffset));\n" +
	"}\n" +

	"void main() {\n" +
	"	vec2 fxdirection = " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " - " + FisheyeShaderProgram.UNIFORM_FX_CENTER + ";\n" +
	"	float seed = "+ FisheyeShaderProgram.UNIFORM_FX_STRENGTH + ";\n" + // 0..1

	"	float z = f1(seed, fxdirection);\n" +
	"	float zmax = f1(seed, vec2("+ FisheyeShaderProgram.UNIFORM_REGION_SIZE +".y * 0.5, 0.0));\n" +
	"	float zmin = f1(seed, vec2(0.0, 0.0));\n" +

	"	z = z / zmax;\n" +
	"	vec2 translated = fxdirection * z;\n" +

	"	if (length(translated) < " + FisheyeShaderProgram.UNIFORM_REGION_SIZE +".y * 0.5) {\n" +
	"		gl_FragColor = texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + FisheyeShaderProgram.UNIFORM_FX_CENTER + " + translated);\n" +
	"	} else {\n" +
	"		gl_FragColor = vec4(1.0, 1.0, 0.0, 0.0);\n" +
	"	}\n" +

//			/* The actual (unburred) sample. */
//			"	vec4 color = texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ");\n" +
//
//			/* fragment => radial direction */
//			"	vec2 direction = " + FisheyeShaderProgram.UNIFORM_FX_CENTER + " - " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
//			/* radial => center direction */
//			"	vec2 dir_rad2cen = " + FisheyeShaderProgram.UNIFORM_REGION_CENTER + " - " + FisheyeShaderProgram.UNIFORM_FX_CENTER + ";\n" +
//
//			/* Calculate the distance to the center of the blur. */
//			"	float distance = sqrt(direction.x * direction.x + direction.y * direction.y);\n" +
//
//			/* Normalize the direction (reuse the distance). */
//			"	direction = direction / distance;\n" +
//
//			//"	float t = sqrt(distance) * sampleStrength;\n" +
//			//"	t = clamp(t, 0.0, 1.0);\n" + // 0 <= t >= 1
//
////			"	vec4 sum = color * sampleShare;\n" +
//			"	vec2 directionSampleDist = direction * sampleDist;\n" +
//			"	vec4 sum = texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " + "+ FisheyeShaderProgram.UNIFORM_FX_STRENGTH +" * directionSampleDist);\n" +
//
//			"	gl_FragColor = sum; \n" +

	"}";

	// ===========================================================
	// Fields
	// ===========================================================

	public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexture0Location = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformFXCenterLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformFXStrength = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformRegionCenterLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformRegionSizeLocation = ShaderProgramConstants.LOCATION_INVALID;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FisheyeShaderProgram() {
		super(FisheyeShaderProgram.VERTEXSHADER,
				FisheyeShaderProgram.FRAGMENTSHADER);
	}

	public static FisheyeShaderProgram getInstance() {
		if (FisheyeShaderProgram.INSTANCE == null) {
			FisheyeShaderProgram.INSTANCE = new FisheyeShaderProgram();
		}
		return FisheyeShaderProgram.INSTANCE;
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

		FisheyeShaderProgram.sUniformModelViewPositionMatrixLocation = getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
		FisheyeShaderProgram.sUniformTexture0Location = getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);

		FisheyeShaderProgram.sUniformFXStrength = getUniformLocation(FisheyeShaderProgram.UNIFORM_FX_STRENGTH);
		FisheyeShaderProgram.sUniformFXCenterLocation = getUniformLocation(FisheyeShaderProgram.UNIFORM_FX_CENTER);
		FisheyeShaderProgram.sUniformRegionSizeLocation = getUniformLocation(FisheyeShaderProgram.UNIFORM_REGION_SIZE);
//		FisheyeShaderProgram.sUniformRegionCenterLocation = getUniformLocation(FisheyeShaderProgram.UNIFORM_REGION_CENTER);
	}

	@Override
	public void bind(final GLState pGLState,
			final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);

		super.bind(pGLState, pVertexBufferObjectAttributes);

		GLES20.glUniformMatrix4fv(
				FisheyeShaderProgram.sUniformModelViewPositionMatrixLocation,
				1, false, pGLState.getModelViewProjectionGLMatrix(), 0);
		GLES20.glUniform1i(FisheyeShaderProgram.sUniformTexture0Location, 0);
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