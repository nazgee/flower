package eu.nazgee.game.flower.pool.cloud;

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

import eu.nazgee.game.flower.Consts;
import eu.nazgee.game.flower.pool.waterdrop.WaterDrop;
import eu.nazgee.game.flower.pool.waterdrop.WaterDrop.IWaterDropListener;
import eu.nazgee.game.utils.helpers.Positioner;


public class Cloud extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================
	static private float LAND_LEVEL = Consts.CAMERA_HEIGHT * 0.9f;
	// ===========================================================
	// Fields
	// ===========================================================
	final private WaterDrop mWaterDrop;
	private IEntityModifier mTravelModifier;
	private CloudListener mCloudListener;
	private CloudModifierListener mCloudModifierListener = new CloudModifierListener();

	// ===========================================================
	// Constructors
	// ===========================================================

	public Cloud(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion, ITextureRegion pWaterDropTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		mWaterDrop = new WaterDrop(0, 0, pWaterDropTextureRegion, pVertexBufferObjectManager);
	}

	public Cloud(float pX, float pY, ITextureRegion pTextureRegion,
			ITextureRegion pWaterDropTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		mWaterDrop = new WaterDrop(0, 0, pWaterDropTextureRegion, pVertexBufferObjectManager);
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
		void onStarted(Cloud pCloud);
		void onFinished(Cloud pCloud);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	synchronized public void travel(final float pX, final float pY,
			final float W, final float time, CloudListener pTravelListener) {

		mCloudListener = pTravelListener;
		Positioner.setCentered(this, pX, pY);
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

	public synchronized void drop(IWaterDropListener pWaterDropListener) {
		final float y = Positioner.getCenteredY(this);
		attachChild(mWaterDrop);
		mWaterDrop.fall(getWidth()/2, getHeight()/2, LAND_LEVEL - y, pWaterDropListener);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private class CloudModifierListener implements IModifierListener<IEntity> {
		@Override
		public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			synchronized (Cloud.this) {
				if (mCloudListener != null) {
					mCloudListener.onStarted(Cloud.this);
				}
			}
		}
		@Override
		public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
			synchronized (Cloud.this) {
				if (mCloudListener != null) {
					mCloudListener.onFinished(Cloud.this);
				}
			}
		}
	}
}
