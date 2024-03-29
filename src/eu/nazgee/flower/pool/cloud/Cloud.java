package eu.nazgee.flower.pool.cloud;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveByModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;

import eu.nazgee.flower.activity.game.scene.game.Sky;
import eu.nazgee.flower.pool.cloud.CloudPool.CloudItem;
import eu.nazgee.flower.pool.waterdrop.WaterDrop;
import eu.nazgee.flower.pool.waterdrop.WaterDrop.IWaterDropListener;
import eu.nazgee.util.Anchor;


public class Cloud extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private IEntityModifier mTravelModifier;
	private CloudListener mCloudListener;
	private final CloudModifierListener mCloudModifierListener = new CloudModifierListener();
	private final CloudItem mCloudItem;

	// ===========================================================
	// Constructors
	// ===========================================================
	public Cloud(final float pX, final float pY, final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, final CloudItem cloudItem) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		mCloudItem = cloudItem;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public CloudListener getTravelListener() {
		return mCloudListener;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public interface CloudListener {
		void onFinished(Cloud pCloud);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	/**
	 * Places a moving cloud in a given location.
	 *
	 * @note CloudItem of this Cloud WILL be automagically recycled after
	 * animation end. NO need to call scheduleDetachAndRecycle() manually on it
	 *
	 * @param pX
	 * @param pY
	 * @param W
	 * @param time
	 * @param pTravelListener
	 */
	synchronized public void travel(final float pX, final float pY,
			final float W, final float time, final CloudListener pTravelListener) {

		setAlpha(0);
		mCloudListener = pTravelListener;
		Anchor.setPosCenter(this, pX, pY);
		unregisterEntityModifier(mTravelModifier);
		mTravelModifier = new ParallelEntityModifier(
				new SequenceEntityModifier(
						new MoveByModifier(time, W, 0)
						),
				new SequenceEntityModifier(
						new FadeInModifier(time*0.1f),
						new DelayModifier(time * 0.8f),
						new FadeOutModifier(time*0.1f)
						)
				);
		mTravelModifier.setAutoUnregisterWhenFinished(false);
		mTravelModifier.addModifierListener(mCloudModifierListener);
		registerEntityModifier(mTravelModifier);
	}

	public synchronized void drop(final WaterDrop pWaterDrop, final Sky pSky, final IWaterDropListener pWaterDropListener) {
		final float height = pSky.getHeightOnSkyCenter(this);
		attachChild(pWaterDrop);
		pWaterDrop.fall(getWidth()/2, getHeight()/2, height, pWaterDropListener);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private class CloudModifierListener implements IModifierListener<IEntity> {
		@Override
		public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {
		}
		@Override
		public void onModifierFinished(final IModifier<IEntity> pModifier, final IEntity pItem) {
			synchronized (Cloud.this) {
				if (mCloudListener != null) {
					mCloudListener.onFinished(Cloud.this);
				}
				mCloudItem.scheduleDetachAndRecycle();
			}
		}
	}
}
