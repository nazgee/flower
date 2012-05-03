package eu.nazgee.game.flower.sun;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveByModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;
import org.andengine.util.modifier.ease.EaseQuadIn;
import org.andengine.util.modifier.ease.EaseQuadOut;

import eu.nazgee.game.flower.scene.main.Sky;
import eu.nazgee.game.utils.helpers.Positioner;


public class Sun extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private IEntityModifier mTravelModifier;
	private TravelListener mTravelListener;
	private MyModifierListener mMyModifierListener = new MyModifierListener();
	private final Sunshine mSunshine;
	// ===========================================================
	// Constructors
	// ===========================================================
	public Sun(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			ITiledTextureRegion pSunshineTextures,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);

		mSunshine = initSunshine(this, pSunshineTextures, pVertexBufferObjectManager);
	}

	public Sun(float pX, float pY, ITextureRegion pTextureRegion,
			ITiledTextureRegion pSunshineTextures,
			VertexBufferObjectManager pVertexBufferObjectManager,
			DrawType pDrawType, ShaderProgram pShaderProgram) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, pDrawType,
				pShaderProgram);

		mSunshine = initSunshine(this, pSunshineTextures, pVertexBufferObjectManager);
	}

	public Sun(float pX, float pY, ITextureRegion pTextureRegion,
			ITiledTextureRegion pSunshineTextures,
			VertexBufferObjectManager pVertexBufferObjectManager,
			ShaderProgram pShaderProgram) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, pShaderProgram);

		mSunshine = initSunshine(this, pSunshineTextures, pVertexBufferObjectManager);
	}

	public Sun(float pX, float pY, ITextureRegion pTextureRegion,
			ITiledTextureRegion pSunshineTextures,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);

		mSunshine = initSunshine(this, pSunshineTextures, pVertexBufferObjectManager);
	}

	/**
	 * Creates a Sunshine, and attach it to the Sun
	 */
	private static Sunshine initSunshine(final Sun pSun, ITiledTextureRegion pSunshineTextures, VertexBufferObjectManager pVertexBufferObjectManager) {
		Sunshine sunshine = new Sunshine(pSunshineTextures, pVertexBufferObjectManager);
		pSun.attachChild(sunshine);
		sunshine.setZIndex(-1);
		pSun.sortChildren();
		sunshine.setPosition(pSun.getWidth()/2, pSun.getHeight()/2);

		return sunshine;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setRaysTargetTop(IAreaShape pTarget, Sky pSky) {
		mSunshine.setTargetTop(pTarget, pSky);
	}

	public void setRaysTargetCenter(Entity pTarget, Sky pSky) {
		mSunshine.setTargetCenter(pTarget, pSky);
	}

	public boolean isShiningAt(IEntity pTarget) {
		return mSunshine.isShiningAt(pTarget);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public interface TravelListener {
		void onStarted(Sun pSun);
		void onFinished(Sun pSun);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	synchronized public void travel(final float pX, final float pY, final float W, final float H, final float time, TravelListener pTravelListener) {
		mTravelListener = pTravelListener;
		Positioner.setCentered(this, pX, pY);
		unregisterEntityModifier(mTravelModifier);
		mTravelModifier = new ParallelEntityModifier(
				new SequenceEntityModifier(
						new MoveByModifier(time, W, 0)
						),
				new SequenceEntityModifier(
						new MoveYModifier(time/2, getY(), getY() - H, EaseQuadOut.getInstance()),
						new MoveYModifier(time/2, getY() - H, getY(), EaseQuadIn.getInstance())
						)
				);
		mTravelModifier.setAutoUnregisterWhenFinished(false);
		mTravelModifier.addModifierListener(mMyModifierListener);
		registerEntityModifier(mTravelModifier);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private class MyModifierListener implements IModifierListener<IEntity> {
		@Override
		public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			synchronized (Sun.this) {
				if (mTravelListener != null) {
					mTravelListener.onStarted(Sun.this);
				}
			}
		}
		@Override
		public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
			synchronized (Sun.this) {
				if (mTravelListener != null) {
					mTravelListener.onFinished(Sun.this);
				}
			}
		}
	}
}
