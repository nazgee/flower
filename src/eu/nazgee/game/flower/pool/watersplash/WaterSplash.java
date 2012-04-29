package eu.nazgee.game.flower.pool.watersplash;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.game.flower.Statics;
import eu.nazgee.game.utils.helpers.Positioner;


public class WaterSplash extends AnimatedSprite {
	// ===========================================================
	// Constants
	// ===========================================================
	static float GRAVITY_ACCEL = 250;
	// ===========================================================
	// Fields
	// ===========================================================
	private IWaterSplashListener mWaterSplashListener;
	private WaterSplashAnimationListener mWaterSplashAnimationListener = new WaterSplashAnimationListener();

	// ===========================================================
	// Constructors
	// ===========================================================

	public WaterSplash(float pX, float pY, float pWidth, float pHeight,
			ITiledTextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
	}

	public WaterSplash(float pX, float pY, ITiledTextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public IWaterSplashListener getSplashListener() {
		return mWaterSplashListener;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public interface IWaterSplashListener {
		void onSplashFinished(WaterSplash pWaterSplash);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	public synchronized void splat(final float pX, final float pY, IWaterSplashListener pWaterSplashListener) {
		mWaterSplashListener = pWaterSplashListener;
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
				if (mWaterSplashListener != null) {
					mWaterSplashListener.onSplashFinished(WaterSplash.this);
				}
				Statics.ENTITY_DETACH_HANDLER.scheduleDetach(pAnimatedSprite);
			}
		}
	}
}
