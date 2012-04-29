package eu.nazgee.game.flower.pool.waterdrop;

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
import eu.nazgee.game.flower.Kinematics;
import eu.nazgee.game.flower.Statics;
import eu.nazgee.game.utils.helpers.Positioner;


public class WaterDrop extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================
	static float GRAVITY_ACCEL = 250;
	// ===========================================================
	// Fields
	// ===========================================================
	private IEntityModifier mDropModifier;
	private IWaterDropListener mWaterDropListener;
	private WaterDropModifierListener mWaterDropModifierListener = new WaterDropModifierListener();

	// ===========================================================
	// Constructors
	// ===========================================================

	public WaterDrop(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
	}

	public WaterDrop(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public IWaterDropListener getTravelListener() {
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
	public synchronized void fall(final float pX, final float pY, final float H, IWaterDropListener pWaterDropListener) {
		final float time = Kinematics.time(GRAVITY_ACCEL, H);
		mWaterDropListener = pWaterDropListener;
		Positioner.setCentered(this, pX, pY);
		unregisterEntityModifier(mDropModifier);
		mDropModifier = new ParallelEntityModifier(
				new SequenceEntityModifier(
						new MoveYModifier(time, getY(), getY() + H, EaseQuadIn.getInstance())
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
		public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			synchronized (WaterDrop.this) {
			}
		}
		@Override
		public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
			synchronized (WaterDrop.this) {
				if (mWaterDropListener != null) {
					mWaterDropListener.onHitTheGround(WaterDrop.this);
				}
				Statics.ENTITY_DETACH_HANDLER.scheduleDetach(pItem);
			}
		}
	}
}
