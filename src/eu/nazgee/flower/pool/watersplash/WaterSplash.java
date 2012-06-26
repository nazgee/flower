package eu.nazgee.flower.pool.watersplash;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.flower.pool.watersplash.WaterSplashPool.WaterSplashItem;
import eu.nazgee.util.Anchor;


public class WaterSplash extends AnimatedSprite {
	// ===========================================================
	// Constants
	// ===========================================================
	static float GRAVITY_ACCEL = 250;
	// ===========================================================
	// Fields
	// ===========================================================
	private final WaterSplashAnimationListener mWaterSplashAnimationListener = new WaterSplashAnimationListener();
	private final WaterSplashItem mWaterSplashItem;

	// ===========================================================
	// Constructors
	// ===========================================================

	public WaterSplash(final float pX, final float pY, final ITiledTextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, final WaterSplashItem waterSplashItem) {
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
		Anchor.setPosCenter(this, pX, pY);
		this.animate(100, false, mWaterSplashAnimationListener);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private class WaterSplashAnimationListener implements IAnimationListener {
		@Override
		public void onAnimationStarted(final AnimatedSprite pAnimatedSprite,
				final int pInitialLoopCount) {
		}
		@Override
		public void onAnimationFrameChanged(final AnimatedSprite pAnimatedSprite,
				final int pOldFrameIndex, final int pNewFrameIndex) {
		}
		@Override
		public void onAnimationLoopFinished(final AnimatedSprite pAnimatedSprite,
				final int pRemainingLoopCount, final int pInitialLoopCount) {
		}
		@Override
		public void onAnimationFinished(final AnimatedSprite pAnimatedSprite) {
			synchronized (WaterSplash.this) {
				mWaterSplashItem.scheduleDetachAndRecycle();
			}
		}
	}
}
