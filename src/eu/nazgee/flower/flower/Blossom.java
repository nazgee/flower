package eu.nazgee.flower.flower;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;
import org.andengine.util.modifier.ease.EaseElasticOut;

import android.util.Log;

public class Blossom extends Sprite {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private IEntityModifier mBloomAnimator;
	private IBlossomListener mBlossomListener;
	private final int mBlossomID;
	// ===========================================================
	// Constructors
	// ===========================================================
	public Blossom(final float pX, final float pY,
			final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final Color pColor) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, pColor, 0);
	}

	public Blossom(final float pX, final float pY,
			final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final Color pColor, final int pBlossomID) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, pColor, pBlossomID);
	}

	public Blossom(final float pX, final float pY, final float pWidth, final float pHeight,
			final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final Color pColor) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, pColor, 0);
	}

	public Blossom(final float pX, final float pY, final float pWidth, final float pHeight,
			final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final Color pColor, final int pBlossomID) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		setColor(pColor);
		this.mBlossomID = pBlossomID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public IBlossomListener getBlossomListener() {
		return mBlossomListener;
	}

	public void setBlossomListener(final IBlossomListener pBlossomListener) {
		mBlossomListener = pBlossomListener;
	}

	public int getBlossomID() {
		return mBlossomID;
	}
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

		final IEntityModifier bloomer = new ColorModifier(pTime, Color.GREEN, mColor);
		mBloomAnimator = new ParallelEntityModifier(
				new SequenceEntityModifier(
						new DelayModifier(pDelay),
						bloomer
						),
				new SequenceEntityModifier(
						new DelayModifier(pDelay),
						new ScaleModifier(pTime, 0, 1f, EaseElasticOut.getInstance())
						)
				);

		bloomer.addModifierListener(new IModifierListener<IEntity>() {
			@Override
			public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {
				if (getBlossomListener() != null) {
					getBlossomListener().onBlooming(Blossom.this);
				}
			}
			@Override
			public void onModifierFinished(final IModifier<IEntity> pModifier, final IEntity pItem) {
				if (getBlossomListener() != null) {
					getBlossomListener().onBloomed(Blossom.this);
				}
			}
		});
		registerEntityModifier(this.mBloomAnimator);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public interface IBlossomListener {
		void onBlooming(Blossom pBlossom);
		void onBloomed(Blossom pBlossom);
	}
}
