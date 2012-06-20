package eu.nazgee.flower.flower;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;
import org.andengine.util.modifier.ease.EaseElasticOut;
import org.andengine.util.modifier.ease.EaseSineOut;

import android.util.Log;

public class EntitySeed extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private IEntityModifier mSeedAnimator;
	private final Color mColor;
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;

	// ===========================================================
	// Constructors
	// ===========================================================
	public EntitySeed(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			final Color pColor, final EntityDetachRunnablePoolUpdateHandler pDetacher) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		mColor = pColor;
		mDetacher = pDetacher;
	}

	public EntitySeed(float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			final Color pColor, final EntityDetachRunnablePoolUpdateHandler pDetacher) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		mColor = pColor;
		mDetacher = pDetacher;
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
		unregisterEntityModifier(mSeedAnimator);
		this.mSeedAnimator = new ParallelEntityModifier(
				new SequenceEntityModifier(
						new ColorModifier(pTime, getColor(), Color.BLACK)
						),
				new SequenceEntityModifier(
						new ScaleModifier(pTime, getScaleX(), 0.5f, EaseSineOut.getInstance())
						)
				);
		registerEntityModifier(this.mSeedAnimator);
	}

	private void animateWater(final float pTime) {
		Log.d(getClass().getSimpleName(), "animateWater();");
		unregisterEntityModifier(mSeedAnimator);
		this.mSeedAnimator = new ParallelEntityModifier(
				new SequenceEntityModifier(
						new ColorModifier(pTime, Color.GREEN, mColor)
						),
				new SequenceEntityModifier(
						new ScaleModifier(pTime, 0, 1f, EaseElasticOut.getInstance())
						)
				);
		registerEntityModifier(this.mSeedAnimator);
	}

	private void animateGrowth(final float pTime) {
		Log.d(getClass().getSimpleName(), "animateGrowth();");
		unregisterEntityModifier(mSeedAnimator);
		this.mSeedAnimator = new FadeOutModifier(pTime);
		registerEntityModifier(this.mSeedAnimator);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
