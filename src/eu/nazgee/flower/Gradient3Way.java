package eu.nazgee.flower;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.vbo.HighPerformanceRectangleVertexBufferObject;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;
import org.andengine.util.color.Color;

import android.opengl.GLES20;
import eu.nazgee.flower.Gradient3WayVertexBufferObject.eGradientVertices;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:18:49 - 13.03.2010
 */
public class Gradient3Way extends RectangularShape {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = Gradient3Way.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = Gradient3Way.VERTEX_INDEX_Y + 1;

	public static final int VERTEX_SIZE = 2 + 1;
	public static final int VERTICES_PER_GRADIENT3WAY = 6;
	public static final int GRADIENT3WAY_SIZE = Gradient3Way.VERTEX_SIZE * Gradient3Way.VERTICES_PER_GRADIENT3WAY;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.build();

	public enum eGradientPosition {
		GRADIENT_LEFT,
		GRADIENT_MIDDLE,
		GRADIENT_RIGHT
	}

	// ===========================================================
	// Fields
	// ===========================================================

	protected final Gradient3WayVertexBufferObject mGradientVertexBufferObject;
	private float mGradientStep;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Gradient3Way#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Gradient3Way(final float pX, final float pY, final float pWidth, final float pHeight, final float pGradientStep, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pWidth, pHeight, pGradientStep, pVertexBufferObjectManager, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Gradient3Way#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Gradient3Way(final float pX, final float pY, final float pWidth, final float pHeight, final float pGradientStep, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pWidth, pHeight, pGradientStep, new Gradient3WayVertexBufferObject(pVertexBufferObjectManager, Gradient3Way.GRADIENT3WAY_SIZE, pDrawType, true, Gradient3Way.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public Gradient3Way(final float pX, final float pY, final float pWidth, final float pHeight, final float pGradientStep, final Gradient3WayVertexBufferObject pRectangleVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, PositionColorShaderProgram.getInstance());
		this.mGradientStep = pGradientStep;

		this.mGradientVertexBufferObject = pRectangleVertexBufferObject;

		this.onUpdateVertices();
		this.onUpdateColor();

		this.setBlendingEnabled(true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public float getGradientStep() {
		return this.mGradientStep;
	}

	public void setGradientStep(final float pGradientStep) {
		this.mGradientStep = pGradientStep;
		onUpdateVertices();
	}

	public void clearGradientColor() {
		mGradientVertexBufferObject.clearGradientVertices();
	}

	public void setGradientColor(eGradientPosition pGradientPosition, Color pColor) {
		switch(pGradientPosition) {
		case GRADIENT_LEFT:
			mGradientVertexBufferObject.setGradientVertex(eGradientVertices.TOP_LEFT, pColor);
			mGradientVertexBufferObject.setGradientVertex(eGradientVertices.BOTTOM_LEFT, pColor);
			break;
		case GRADIENT_MIDDLE:
			mGradientVertexBufferObject.setGradientVertex(eGradientVertices.TOP_MIDDLE, pColor);
			mGradientVertexBufferObject.setGradientVertex(eGradientVertices.BOTTOM_MIDDLE, pColor);
			break;
		case GRADIENT_RIGHT:
			mGradientVertexBufferObject.setGradientVertex(eGradientVertices.TOP_RIGHT, pColor);
			mGradientVertexBufferObject.setGradientVertex(eGradientVertices.BOTTOM_RIGHT, pColor);
			break;
		}
		this.onUpdateColor();
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Gradient3WayVertexBufferObject getVertexBufferObject() {
		return this.mGradientVertexBufferObject;
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);

		this.mGradientVertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mGradientVertexBufferObject.draw(GLES20.GL_TRIANGLE_STRIP, Gradient3Way.VERTICES_PER_GRADIENT3WAY);
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