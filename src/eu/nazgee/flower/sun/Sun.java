package eu.nazgee.flower.sun;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveByModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;
import org.andengine.util.modifier.ease.EaseQuadIn;
import org.andengine.util.modifier.ease.EaseQuadOut;

import android.opengl.GLES20;
import android.util.Log;
import eu.nazgee.flower.activity.game.scene.game.Sky;
import eu.nazgee.misc.FisheyeShaderProgram;
import eu.nazgee.util.Anchor;
import eu.nazgee.util.Anchor.eAnchorPointXY;


public class Sun extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private IEntityModifier mTravelModifier;
	private ISunListener mTravelListener;
	private final MyModifierListener mMyModifierListener = new MyModifierListener();
	private final Sunshine mSunshine;
	private final Sprite mSun;
	// ===========================================================
	// Constructors
	// ===========================================================
	public Sun(final float pX, final float pY, final ITextureRegion pTextureRegion,
			final ITiledTextureRegion pSunshineTextures,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY);

		mSun = initSun(this, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager);
		mSunshine = initSunshine(this, pSunshineTextures, pVertexBufferObjectManager);
	}

	/**
	 * Creates a SunSprite, and attach it to the Sun
	 */
	private static Sprite initSun(final Sun pSun, final float w, final float h, final ITextureRegion pSunTexture, final VertexBufferObjectManager pVertexBufferObjectManager) {
//		final ShaderProgram shdr = FisheyeShaderProgram.getInstance();
//		final float cw = (pSunTexture.getU2() - pSunTexture.getU());
//		final float ch = (pSunTexture.getV2() - pSunTexture.getV());
//		final float cx = pSunTexture.getU() + cw/2;
//		final float cy = pSunTexture.getV() + ch/2;
//		Log.e("aaa", "w=" + cw + "; ch=" + ch);


		final Sprite sun = new Sprite(0, 0, w, h, pSunTexture, pVertexBufferObjectManager);
//		final Sprite sun = new Sprite(0, 0, w, h, pSunTexture, pVertexBufferObjectManager, shdr) {
//			private float step = 0;
//			private float seed = 666;
//			private float valfx = 0;
//			@Override
//			protected void preDraw(final GLState pGLState, final Camera pCamera) {
//				super.preDraw(pGLState, pCamera);
//				if (seed > Math.PI * 2) {
//					step = MathUtils.random(0.05f, 0.2f);
//					seed = 0;
//				}
//				seed += step;
//
//				valfx = (float) (Math.sin(seed) * 0.5f + 0.5f);
//				GLES20.glUniform1f(FisheyeShaderProgram.sUniformFXStrength, valfx);
//				GLES20.glUniform2f(FisheyeShaderProgram.sUniformFXCenterLocation, cx,  cy);
//				GLES20.glUniform2f(FisheyeShaderProgram.sUniformRegionSizeLocation, cw, ch);
//			}
//		};

		pSun.attachChild(sun);
		Anchor.setPosCenterAtParent(sun, eAnchorPointXY.CENTERED);
		sun.registerEntityModifier(new LoopEntityModifier(
					new RotationByModifier(5, 360)
				));
		sun.setZIndex(0);
		pSun.sortChildren();

		return sun;
	}

	/**
	 * Creates a Sunshine, and attach it to the Sun
	 */
	private static Sunshine initSunshine(final Sun pSun, final ITiledTextureRegion pSunshineTextures, final VertexBufferObjectManager pVertexBufferObjectManager) {
		final Sunshine sunshine = new Sunshine(pSunshineTextures, pVertexBufferObjectManager);
		pSun.attachChild(sunshine);
		sunshine.setZIndex(-1);
		Anchor.setPosCenterAtParent(sunshine, eAnchorPointXY.CENTERED);
		pSun.sortChildren();
//		sunshine.setPosition(pSun.mSun.getWidth()/2, pSun.mSun.getHeight()/2);

		return sunshine;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setRaysTargetTop(final IEntity pTarget, final Sky pSky) {
		mSunshine.setTargetTop(pTarget, pSky);
	}

	public void setRaysTargetCenter(final Entity pTarget, final Sky pSky) {
		mSunshine.setTargetCenter(pTarget, pSky);
	}

	public void setRaysTarget(final float pHeightOnSky, final Sky pSky) {
		mSunshine.setTarget(pHeightOnSky, pSky);
	}

	public boolean isShiningAt(final IEntity pTarget) {
		return mSunshine.isShiningAt(pTarget);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public interface ISunListener {
		void onStarted(Sun pSun);
		void onFinished(Sun pSun);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	synchronized public void travel(final float pX, final float pY, final float W, final float H, final float time, final ISunListener pTravelListener) {
		mTravelListener = pTravelListener;
		setPosition(pX, pY);
		unregisterEntityModifier(mTravelModifier);
		mTravelModifier = new ParallelEntityModifier(
				new SequenceEntityModifier(
						new MoveByModifier(time, W, 0)
						),
				new SequenceEntityModifier(
						new MoveYModifier(time/2, getY(), getY() + H, EaseQuadOut.getInstance()),
						new MoveYModifier(time/2, getY() + H, getY(), EaseQuadIn.getInstance())
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
		public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {
			synchronized (Sun.this) {
				if (mTravelListener != null) {
					mTravelListener.onStarted(Sun.this);
				}
			}
		}
		@Override
		public void onModifierFinished(final IModifier<IEntity> pModifier, final IEntity pItem) {
			synchronized (Sun.this) {
				if (mTravelListener != null) {
					mTravelListener.onFinished(Sun.this);
				}
			}
		}
	}
}
