package eu.nazgee.flower.flower;

import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.ease.EaseElasticOut;

import android.util.Log;

public class EntityBlossom extends Sprite {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private IEntityModifier mBloomAnimator;
	private final Color mColor;
	// ===========================================================
	// Constructors
	// ===========================================================

	public EntityBlossom(float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			final Color pColor) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, pColor);
	}

	public EntityBlossom(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			final Color pColor) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
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
	synchronized public void animateBloom() {
		animateBloom(1, 0);
	}

	synchronized public void animateBloom(final float pDelay) {
		animateBloom(1, pDelay);
	}

	private void animateBloom(final float pTime, final float pDelay) {
		Log.d(getClass().getSimpleName(), "animateBloom();");
		unregisterEntityModifier(mBloomAnimator);
		setScale(0);
		this.mBloomAnimator = new ParallelEntityModifier(
				new SequenceEntityModifier(
						new DelayModifier(pDelay),
						new ColorModifier(pTime, Color.GREEN, mColor)
						),
				new SequenceEntityModifier(
						new DelayModifier(pDelay),
						new ScaleModifier(pTime, 0, 1f, EaseElasticOut.getInstance())
						)
				);
		registerEntityModifier(this.mBloomAnimator);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
