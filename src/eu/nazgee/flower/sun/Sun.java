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
import org.andengine.entity.shape.IAreaShape;
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
import eu.nazgee.flower.FisheyeShaderProgram;
import eu.nazgee.flower.activity.game.scene.game.Sky;


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
	private static Sprite initSun(final Sun pSun, final float w, final float h, final ITextureRegion pSunTexture, VertexBufferObjectManager pVertexBufferObjectManager) {
		ShaderProgram shdr = new FisheyeShaderProgram();
		final float cw = (pSunTexture.getU2() - pSunTexture.getU());
		final float ch = (pSunTexture.getV2() - pSunTexture.getV());
		final float cx = pSunTexture.getU() + cw/2;
		final float cy = pSunTexture.getV() + ch/2;


		Sprite sun = new Sprite(-w/2, -h/2, w, h, pSunTexture, pVertexBufferObjectManager, shdr) {
			private float step = 0;
			private float seed = 666;
			private final float r = 0.5f;
			private float angle = 0; 
			private float valx = 0;
			private float valy = 0;
			private float valfx = 0;
			@Override
			protected void preDraw(final GLState pGLState, final Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				if (seed > Math.PI * 2) {
					step = MathUtils.random(0.05f, 0.2f);
					seed = 0;
					angle = MathUtils.random(0, (float) (Math.PI * 2));
//					valx = (float) (r * Math.sin(angle));
//					valy = (float) (r * Math.cos(angle));
					valx = 0;
					valy = 0;
				}
				seed += step;

				valfx = (float) (Math.sin(seed) * 0.5f + 0.5f);
				GLES20.glUniform2f(FisheyeShaderProgram.sUniformFXCenterLocation, cx + valx * cw,  cy + valy * ch);
				GLES20.glUniform2f(FisheyeShaderProgram.sUniformRegionSizeLocation, cw, ch);
				GLES20.glUniform1f(FisheyeShaderProgram.sUniformFXStrength, valfx);
//				seed += 0.01f;
//
//				float valx = 0.3f;
//				float valy = 0.3f;
//				float valfx = 0.08f;
//
//				valx = (float) (Math.sin(seed)*valx);
//				valy = (float) (Math.cos(seed)*valy);
//				valfx = (float) (Math.cos(seed * 6)*valfx/2 + valfx/2);
//				GLES20.glUniform2f(RadialBlurShaderProgram.sUniformFXCenterLocation, cx + valx * cw,  cy + valy * ch);
//				GLES20.glUniform1f(RadialBlurShaderProgram.sUniformFXStrength, valfx);
////				GLES20.glUniform2f(RadialBlurShaderProgram.sUniformRegionCenterLocation, cx,  cy);
			}
		};

		pSun.attachChild(sun);
//		sun.registerEntityModifier(new LoopEntityModifier(
//					new RotationByModifier(5, 360)
//				));
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
