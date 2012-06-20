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

public class EntityBlossom extends Sprite {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private IEntityModifier mBloomAnimator;
	private final Color mColor;
	private IBlossomListener mBlossomListener;
	private final int mBlossomID;
	// ===========================================================
	// Constructors
	// ===========================================================
	public EntityBlossom(float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			final Color pColor) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, pColor, 0);
	}

	public EntityBlossom(float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			final Color pColor, int pBlossomID) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, pColor, pBlossomID);
	}

	public EntityBlossom(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			final Color pColor) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, pColor, 0);
	}

	public EntityBlossom(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			final Color pColor, int pBlossomID) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		this.mColor = pColor;
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
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				if (getBlossomListener() != null) {
					getBlossomListener().onBlooming(EntityBlossom.this);
				} 
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				if (getBlossomListener() != null) {
					getBlossomListener().onBloomed(EntityBlossom.this);
				} 
			}
		});
		registerEntityModifier(this.mBloomAnimator);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public interface IBlossomListener {
		void onBlooming(EntityBlossom pBlossom);
		void onBloomed(EntityBlossom pBlossom);
	}
}
