package eu.nazgee.flower.pool.watersplash;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.flower.pool.watersplash.WaterSplashPool.WaterSplashItem;
import eu.nazgee.game.utils.helpers.Positioner;


public class WaterSplash extends AnimatedSprite {
	// ===========================================================
	// Constants
	// ===========================================================
	static float GRAVITY_ACCEL = 250;
	// ===========================================================
	// Fields
	// ===========================================================
	private WaterSplashAnimationListener mWaterSplashAnimationListener = new WaterSplashAnimationListener();
	private final WaterSplashItem mWaterSplashItem;

	// ===========================================================
	// Constructors
	// ===========================================================

	public WaterSplash(float pX, float pY, ITiledTextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, WaterSplashItem waterSplashItem) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.mWaterSplashItem = waterSplashItem;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	/**
	 * Starts a splash animation in a given location
	 * 
	 * @note WaterSplashItem of this WaterSplash WILL be automagically recycled after
	 * animation end. NO need to call scheduleDetachAndRecycle() manually on it
	 * 
	 * @param pX
	 * @param pY
	 */
	public synchronized void splat(final float pX, final float pY) {
		Positioner.setCentered(this, pX, pY);
		this.animate(100, false, mWaterSplashAnimationListener);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private class WaterSplashAnimationListener implements IAnimationListener {
		@Override
		public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
				int pInitialLoopCount) {
		}
		@Override
		public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
				int pOldFrameIndex, int pNewFrameIndex) {
		}
		@Override
		public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
				int pRemainingLoopCount, int pInitialLoopCount) {
		}
		@Override
		public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
			synchronized (WaterSplash.this) {
				mWaterSplashItem.scheduleDetachAndRecycle();
			}
		}
	}
}
