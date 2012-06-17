package eu.nazgee.misc;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.IVertexBufferObject;

public class ParallaxLayer extends RectangularShape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<ParallaxLayerEntity> mParallaxEntities = new ArrayList<ParallaxLayerEntity>();

	protected float mParallaxValue;
	protected float mParallaxScrollValue;

	protected float mParallaxChangePerSecond;

	protected float mParallaxScrollFactor = 0.2f;

	private final Camera mCamera;

	private float mCameraPreviousX;
	private float mCameraOffsetX;

	private boolean mIsScrollable = false;

	// ===========================================================
	// Constructors
	// ===========================================================
	public ParallaxLayer(final float pWidth, final float pHeight, final Camera pCamera, final boolean pIsScrollable) {
		super(0, 0, pWidth, pHeight, null);
		this.mCamera = pCamera;
		this.mIsScrollable = pIsScrollable;
		this.mCameraPreviousX = pCamera.getCenterX();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public void setParallaxValue(final float pParallaxValue) {
		this.mParallaxValue = pParallaxValue;
	}

	public void setParallaxChangePerSecond(final float pParallaxChangePerSecond) {
		this.mParallaxChangePerSecond = pParallaxChangePerSecond;
	}

	public void setParallaxScrollFactor(final float pParallaxScrollFactor) {
		this.mParallaxScrollFactor = pParallaxScrollFactor;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onManagedDraw(GLState pGLState, Camera pCamera) {
		super.preDraw(pGLState, pCamera);

		final float parallaxValue = this.mParallaxValue;
		final float parallaxScrollValue = this.mParallaxScrollValue;
		final ArrayList<ParallaxLayerEntity> parallaxEntities = this.mParallaxEntities;

		int entitiesCount = mParallaxEntities.size();
		for (int i = 0; i < entitiesCount; i++) {
			if (parallaxEntities.get(i).isScrollable()) {
				parallaxEntities.get(i).onDraw(pGLState, pCamera, parallaxScrollValue, getWidth());
			} else {
				parallaxEntities.get(i).onDraw(pGLState, pCamera, parallaxValue, getWidth());
			}
		}
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {

		if (mIsScrollable && mCameraPreviousX != this.mCamera.getCenterX()) {
			mCameraOffsetX = mCameraPreviousX - this.mCamera.getCenterX();
			mCameraPreviousX = this.mCamera.getCenterX();

			this.mParallaxScrollValue += mCameraOffsetX * this.mParallaxScrollFactor;
			mCameraOffsetX = 0;
		}

		this.mParallaxValue += this.mParallaxChangePerSecond * pSecondsElapsed;
		super.onManagedUpdate(pSecondsElapsed);
	}

	@Override
	public IVertexBufferObject getVertexBufferObject() {
		// we are not drawing anything - our entities are
		return null;
	}

	@Override
	protected void onUpdateVertices() {
		// we are not drawing anything - our entities are		
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void attachParallaxEntity(final ParallaxLayerEntity parallaxEntity) {
		this.mParallaxEntities.add(parallaxEntity);
	}

	public boolean detachParallaxEntity(final ParallaxLayerEntity pParallaxEntity) {
		return this.mParallaxEntities.remove(pParallaxEntity);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
