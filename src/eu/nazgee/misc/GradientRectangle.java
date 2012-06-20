package eu.nazgee.misc;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.vbo.HighPerformanceRectangleVertexBufferObject;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;
import org.andengine.util.adt.color.Color;

import android.opengl.GLES20;
import eu.nazgee.misc.GradientVertexBufferObject.eGradientLayout;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:18:49 - 13.03.2010
 */
public class GradientRectangle extends Shape {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = GradientRectangle.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = GradientRectangle.VERTEX_INDEX_Y + 1;
	public static final int VERTEX_SIZE = 2 + 1;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected final GradientVertexBufferObject mGradientVertexBufferObject;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link GradientRectangle#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public GradientRectangle(final float pX, final float pY, final float pWidth, final float pHeight, final int pGradientBands, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pWidth, pHeight, pGradientBands, pVertexBufferObjectManager, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link GradientRectangle#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public GradientRectangle(final float pX, final float pY, final float pWidth, final float pHeight, final int pGradientBands, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pWidth, pHeight, new GradientVertexBufferObject(pVertexBufferObjectManager, pGradientBands, pDrawType, true, GradientRectangle.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT, eGradientLayout.LAYOUT_HORIZONTAL));
	}

	public GradientRectangle(final float pX, final float pY, final float pWidth, final float pHeight, final GradientVertexBufferObject pRectangleVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, PositionColorShaderProgram.getInstance());

		this.mGradientVertexBufferObject = pRectangleVertexBufferObject;

		this.onUpdateVertices();
		this.onUpdateColor();

		this.setBlendingEnabled(true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public int getGradientsCount() {
		return mGradientVertexBufferObject.getGradientBandsNumber();
	}

	public void clearGradientBands() {
		mGradientVertexBufferObject.clearGradientVertices();
	}

	public void clearGradientBand(int pGradientBand) {
		mGradientVertexBufferObject.clearGradientBand(pGradientBand);
	}

	public void setGradientBand(int pGradientBand, Color pColor) {
		mGradientVertexBufferObject.setGradientBand(pGradientBand, pColor);
		this.onUpdateColor();
	}

	public void setGradientBandAlpha(int pGradientBand, final float pAlpha) {
		mGradientVertexBufferObject.setGradientBandAlpha(pGradientBand, pAlpha);
		this.onUpdateColor();
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public GradientVertexBufferObject getVertexBufferObject() {
		return this.mGradientVertexBufferObject;
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);

		this.mGradientVertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mGradientVertexBufferObject.draw(GLES20.GL_TRIANGLE_STRIP, mGradientVertexBufferObject.getGradientVerticesNumber());
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		this.mGradientVertexBufferObject.unbind(pGLState, this.mShaderProgram);

		super.postDraw(pGLState, pCamera);
	}

	@Override
	protected void onUpdateColor() {
		this.mGradientVertexBufferObject.onUpdateColor(this);
	}

	@Override
	protected void onUpdateVertices() {
		this.mGradientVertexBufferObject.onUpdateVertices(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}