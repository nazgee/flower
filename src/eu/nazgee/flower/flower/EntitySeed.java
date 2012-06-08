package eu.nazgee.flower.flower;

import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
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
	private ParallelEntityModifier mSeedAnimator;
	private final Color mColor;

	// ===========================================================
	// Constructors
	// ===========================================================
	public EntitySeed(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			final Color pColor) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		this.mColor = pColor;
	}

	public EntitySeed(float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			final Color pColor) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.mColor = pColor;
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
	public void animateWater() {
		animateWater(1);
	}

	public void animateFry() {
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
		Log.d(getClass().getSimpleName(), "animateGrowth();");
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
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
