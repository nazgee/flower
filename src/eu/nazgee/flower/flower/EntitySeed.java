package eu.nazgee.flower.flower;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;
import org.andengine.util.modifier.ease.EaseBounceIn;
import org.andengine.util.modifier.ease.EaseBounceOut;
import org.andengine.util.modifier.ease.EaseQuadOut;
import org.andengine.util.modifier.ease.EaseSineOut;

import android.util.Log;
import eu.nazgee.util.Anchor;
import eu.nazgee.util.Anchor.eAnchorPointXY;

public class EntitySeed extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int ZINDEX_WATERMARK = -1;
	// ===========================================================
	// Fields
	// ===========================================================
	private IEntityModifier mSeedAnimator;
	private IEntityModifier mWaterMarkAnimator;
	private final Color mColor;
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;
	private final Sprite mWaterMark;

	// ===========================================================
	// Constructors
	// ===========================================================
	public EntitySeed(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pSeedTexture, ITextureRegion pWaterMarkerTexture,
			VertexBufferObjectManager pVertexBufferObjectManager,
			final Color pColor, final EntityDetachRunnablePoolUpdateHandler pDetacher) {
		super(pX, pY, pWidth, pHeight, pSeedTexture, pVertexBufferObjectManager);
		mColor = pColor;
		mDetacher = pDetacher;
		mWaterMark = new Sprite(0, 0, pWaterMarkerTexture, pVertexBufferObjectManager);
	}

	public EntitySeed(float pX, float pY,
			ITextureRegion pTextureRegion, ITextureRegion pWaterMarkerTexture,
			VertexBufferObjectManager pVertexBufferObjectManager,
			final Color pColor, final EntityDetachRunnablePoolUpdateHandler pDetacher) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		mColor = pColor;
		mDetacher = pDetacher;
		mWaterMark = new Sprite(0, 0, pWaterMarkerTexture, pVertexBufferObjectManager);
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
	public synchronized void animateGrowthAndDetachSelf() {
		animateGrowth(1);
		mSeedAnimator.addModifierListener(new IModifierListener<IEntity>() {
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				mDetacher.scheduleDetach(pItem);
			}
		});
	}

	public synchronized void animateWater() {
		animateWater(1);
	}

	public synchronized void animateFry() {
		animateFry(0.5f);
	}

	private void animateFry(final float pTime) {
		Log.d(getClass().getSimpleName(), "animateFry();");

		setModifier(new ParallelEntityModifier(
				new SequenceEntityModifier(
						new ColorModifier(pTime, getColor(), Color.BLACK)
						),
				new SequenceEntityModifier(
						new ScaleModifier(pTime, getScaleX(), 0.5f, EaseSineOut.getInstance())
						)
				));
	}

	private void animateWater(final float pTime) {
		Log.d(getClass().getSimpleName(), "animateWater();");

//		setModifier(new ParallelEntityModifier(
//				new SequenceEntityModifier(
//						new ColorModifier(pTime, Color.GREEN, mColor)
//						),
//				new SequenceEntityModifier(
//						new ScaleModifier(pTime, 0, 1f, EaseElasticOut.getInstance())
//						)
//				));

		attachChild(mWaterMark);
		mWaterMark.setZIndex(ZINDEX_WATERMARK);
		mWaterMark.setScale(0);
		Anchor.setPosCenterAtParent(mWaterMark, eAnchorPointXY.CENTERED);
		setWaterMarkModifier(new ParallelEntityModifier(
				new ScaleModifier(pTime, 0, 1, EaseBounceOut.getInstance()),
				new LoopEntityModifier(new SequenceEntityModifier(
						new RotationModifier(pTime, 330, 390, EaseQuadOut.getInstance()),
						new RotationModifier(pTime, 390, 330, EaseQuadOut.getInstance())
						))
				));
		sortChildren(false);
	}

	private void animateGrowth(final float pTime) {
		Log.d(getClass().getSimpleName(), "animateGrowth();");

		setModifier(new FadeOutModifier(pTime));
		setWaterMarkModifier(new ScaleModifier(pTime, 1, 0, EaseBounceIn.getInstance()));
	}

	synchronized private void setModifier(final IEntityModifier pModifier) {
		pModifier.setAutoUnregisterWhenFinished(false);
		unregisterEntityModifier(mSeedAnimator);
		mSeedAnimator = pModifier;
		registerEntityModifier(pModifier);
	}

	synchronized private void setWaterMarkModifier(final IEntityModifier pModifier) {
		pModifier.setAutoUnregisterWhenFinished(false);
		mWaterMark.unregisterEntityModifier(mWaterMarkAnimator);
		mWaterMarkAnimator = pModifier;
		mWaterMark.registerEntityModifier(pModifier);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
