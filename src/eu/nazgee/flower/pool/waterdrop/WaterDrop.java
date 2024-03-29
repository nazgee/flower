package eu.nazgee.flower.pool.waterdrop;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;
import org.andengine.util.modifier.ease.EaseQuadIn;

import android.util.Log;
import eu.nazgee.flower.pool.waterdrop.WaterDropPool.WaterDropItem;
import eu.nazgee.util.Anchor;
import eu.nazgee.util.Kinematics;


public class WaterDrop extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private IEntityModifier mDropModifier;
	private IWaterDropListener mWaterDropListener;
	private final WaterDropModifierListener mWaterDropModifierListener = new WaterDropModifierListener();
	private final WaterDropItem mWaterDropItem;

	// ===========================================================
	// Constructors
	// ===========================================================
	public WaterDrop(final float pX, final float pY, final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, final WaterDropItem waterDropItem) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.mWaterDropItem = waterDropItem;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public IWaterDropListener getDropListener() {
		return mWaterDropListener;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public interface IWaterDropListener {
		void onHitTheGround(WaterDrop pWaterDrop);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	/**
	 * Starts a falling rain drop animation.
	 *
	 * @note WaterDropItem of this WaterDrop WILL be automagically recycled after
	 * animation end. NO need to call scheduleDetachAndRecycle() manually on it
	 *
	 * @param pX
	 * @param pY
	 * @param H
	 * @param pWaterDropListener
	 */
	public synchronized void fall(final float pX, final float pY, final float H, final IWaterDropListener pWaterDropListener) {
		final float time = Kinematics.time(Kinematics.GRAVITY_WATER_ACCEL, H);
		mWaterDropListener = pWaterDropListener;
		Anchor.setPosCenter(this, pX, pY);
		unregisterEntityModifier(mDropModifier);
		mDropModifier = new ParallelEntityModifier(
				new SequenceEntityModifier(
						new MoveYModifier(time, getY(), getY() - H, EaseQuadIn.getInstance())
						),
				new SequenceEntityModifier(
						new FadeInModifier(time*0.2f),
						new DelayModifier(time * 0.8f)
						)
				);
		mDropModifier.setAutoUnregisterWhenFinished(false);
		mDropModifier.addModifierListener(mWaterDropModifierListener);
		registerEntityModifier(mDropModifier);
		Log.d(getClass().getSimpleName(), "fall(); time=" + time);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private class WaterDropModifierListener implements IModifierListener<IEntity> {
		@Override
		public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {
			synchronized (WaterDrop.this) {
			}
		}
		@Override
		public void onModifierFinished(final IModifier<IEntity> pModifier, final IEntity pItem) {
			synchronized (WaterDrop.this) {
				if (mWaterDropListener != null) {
					mWaterDropListener.onHitTheGround(WaterDrop.this);
				}
				mWaterDropItem.scheduleDetachAndRecycle();
			}
		}
	}
}
