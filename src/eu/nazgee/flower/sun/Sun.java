package eu.nazgee.flower.sun;

import org.andengine.engine.camera.Camera;
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
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;
import org.andengine.util.modifier.ease.EaseQuadIn;
import org.andengine.util.modifier.ease.EaseQuadOut;

import android.opengl.GLES20;
import eu.nazgee.flower.RadialBlurShaderProgram;
import eu.nazgee.flower.activity.game.scene.main.Sky;
import eu.nazgee.game.utils.misc.UtilsMath;


public class Sun extends Entity {
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
	private final Sprite mSun;
	// ===========================================================
	// Constructors
	// ===========================================================
	public Sun(float pX, float pY, ITextureRegion pTextureRegion,
			ITiledTextureRegion pSunshineTextures,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY);

		mSun = initSun(this, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager);
		mSunshine = initSunshine(this, pSunshineTextures, pVertexBufferObjectManager);
	}

	/**
	 * Creates a SunSprite, and attach it to the Sun
	 */
	private static Sprite initSun(final Sun pSun, final float w, final float h, ITextureRegion pSunTexture, VertexBufferObjectManager pVertexBufferObjectManager) {
		ShaderProgram shdr = new RadialBlurShaderProgram();
		final float cx = pSunTexture.getU() + (pSunTexture.getU2() - pSunTexture.getU())/2;
		final float cy = pSunTexture.getV() + (pSunTexture.getV2() - pSunTexture.getV())/2;

		Sprite sun = new Sprite(-w/2, -h/2, w, h, pSunTexture, pVertexBufferObjectManager, shdr) {
			private float bloorstrength = 0;
			@Override
			protected void preDraw(final GLState pGLState, final Camera pCamera) {
				super.preDraw(pGLState, pCamera);

				// srodek
				GLES20.glUniform2f(RadialBlurShaderProgram.sUniformRadialBlurCenterLocation, cx, cy);

				// sila
				bloorstrength += 0.07f;
				UtilsMath.normalizeAngleRad(bloorstrength);

				final float val = 2f;
				GLES20.glUniform1f(RadialBlurShaderProgram.sUniformRadialBlurStrength, (float) (Math.sin(bloorstrength)*val/2 + val/2));
			}
		};

		pSun.attachChild(sun);
		sun.setZIndex(0);
		pSun.sortChildren();

		return sun;
	}

	/**
	 * Creates a Sunshine, and attach it to the Sun
	 */
	private static Sunshine initSunshine(final Sun pSun, ITiledTextureRegion pSunshineTextures, VertexBufferObjectManager pVertexBufferObjectManager) {
		Sunshine sunshine = new Sunshine(pSunshineTextures, pVertexBufferObjectManager);
		pSun.attachChild(sunshine);
		sunshine.setZIndex(-1);
		pSun.sortChildren();
//		sunshine.setPosition(pSun.mSun.getWidth()/2, pSun.mSun.getHeight()/2);

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
		setPosition(pX, pY);
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
