package eu.nazgee.game.flower;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveByModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;
import org.andengine.util.modifier.ease.EaseBounceOut;
import org.andengine.util.modifier.ease.EaseQuadIn;

import eu.nazgee.game.flower.Consts;
import eu.nazgee.game.flower.pool.waterdrop.WaterDrop;
import eu.nazgee.game.flower.pool.waterdrop.WaterDrop.IWaterDropListener;
import eu.nazgee.game.flower.scene.Sky;
import eu.nazgee.game.utils.helpers.Positioner;


public class Flower extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private IEntityModifier mTravelModifier;
	private FlowerListener mFlowerListener;
//	private FlowerModifierListener mCloudModifierListener = new FlowerModifierListener();

	// ===========================================================
	// Constructors
	// ===========================================================

	public Flower(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
	}

	public Flower(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public FlowerListener getTravelListener() {
		return mFlowerListener;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public interface FlowerListener {

	}
	// ===========================================================
	// Methods
	// ===========================================================
	synchronized public void travel(final float pX, final float pY, Sky pSky) {
		final float time = 1;
		Positioner.setCentered(this, pX, pY);
		unregisterEntityModifier(mTravelModifier);

		final float height = pSky.getHeightOnSky(pY);
		mTravelModifier = new ParallelEntityModifier(
				new SequenceEntityModifier(
						new MoveYModifier(time, getY(), getY() + height, EaseBounceOut.getInstance())
						)
				);
		mTravelModifier.setAutoUnregisterWhenFinished(false);
		registerEntityModifier(mTravelModifier);
	}

	public void bloom() {
		final float time = 1;
		IEntityModifier bloomer = new ParallelEntityModifier(
				new SequenceEntityModifier(
						new ColorModifier(time, getColor(), Color.RED)
						),
				new SequenceEntityModifier(
						new ScaleModifier(time, getScaleX(), 1.25f, EaseBounceOut.getInstance())
						)
				);
		registerEntityModifier(bloomer);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

//	private class FlowerModifierListener implements IModifierListener<IEntity> {
//		@Override
//		public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
//			synchronized (Flower.this) {
//				if (mFlowerListener != null) {
//				}
//			}
//		}
//		@Override
//		public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
//			synchronized (Flower.this) {
//				if (mFlowerListener != null) {
//				}
//			}
//		}
//	}
}
