package eu.nazgee.game.flower.sun;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.SingleValueSpanEntityModifier;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;
import org.andengine.util.modifier.ease.EaseElasticOut;
import org.andengine.util.modifier.ease.IEaseFunction;

import eu.nazgee.game.flower.scene.main.Sky;
import eu.nazgee.game.utils.helpers.Positioner;

public class Sunshine extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================

	private float EASING_DURATION = 1;
	private float EASING_RESET_THRESHOLD = 50;

	// ===========================================================
	// Fields
	// ===========================================================

	private final Sprite mSpriteTop;
	private final Sprite mSpriteMiddle;
	private final Sprite mSpriteBottom;

	private SunshineLengthModifier mSunshineLengthModifier = new SunshineLengthModifier(0, 0, 0, EaseElasticOut.getInstance());

	// ===========================================================
	// Constructors
	// ===========================================================

	public Sunshine (final ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVBO) {
		mSpriteTop = new Sprite(0, 0, pTiledTextureRegion.getTextureRegion(0), pVBO);
		mSpriteMiddle = new Sprite(0, 0, pTiledTextureRegion.getTextureRegion(1), pVBO);
		mSpriteBottom = new Sprite(0, 0, pTiledTextureRegion.getTextureRegion(2), pVBO);

		attachChild(mSpriteTop);
		attachChild(mSpriteMiddle);
		attachChild(mSpriteBottom);

		Positioner.setCenteredX(mSpriteTop, 0);
		Positioner.setCenteredX(mSpriteMiddle, 0);
		Positioner.setCenteredX(mSpriteBottom, 0);

		mSpriteMiddle.setScaleCenterY(0);
		mSpriteMiddle.setY(mSpriteTop.getY() + mSpriteTop.getHeight());

		setCurrentRaysLength(0);
		mSunshineLengthModifier.setAutoUnregisterWhenFinished(false);
		registerEntityModifier(mSunshineLengthModifier);

		configureRaysLengthModifier(100);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setTargetTop(IAreaShape pTarget, Sky pSky) {
		float target = pSky.getHeightOnSkyTop(pTarget);
		setTarget(target, pSky);
	}

	public void setTargetCenter(Entity pTarget, Sky pSky) {
		float target = pSky.getHeightOnSky(pTarget);
		setTarget(target, pSky);
	}

	public boolean isShiningAt(IEntity pTarget) {
		float pos[] = pTarget.getSceneCenterCoordinates();
		return mSpriteBottom.contains(pos[Constants.VERTEX_INDEX_X], pos[Constants.VERTEX_INDEX_Y]);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	private void setTarget(float pSkyHeight, Sky pSky) {
		float me = pSky.getHeightOnSky(this);
		if (me < pSkyHeight) {
			pSkyHeight = pSky.getHeightOnScene(0);
		}
		configureRaysLengthModifier(Math.max(0, me - pSkyHeight - mSpriteTop.getHeight()));
	}

	private void configureRaysLengthModifier(final float pTargetRays) {
		if (isModifierResetNeeded(pTargetRays)) {
			mSunshineLengthModifier.reset(EASING_DURATION, getCurrentRaysLength(), pTargetRays);
		} else {
			mSunshineLengthModifier.updateTarget(pTargetRays);
		}
	}

	private boolean isModifierResetNeeded(final float pNewRaysValue) {
		return mSunshineLengthModifier.isFinished() || (EASING_RESET_THRESHOLD < Math.abs(mSunshineLengthModifier.getTarget() - pNewRaysValue));
	}

	private float getCurrentRaysLength() {
		final float current = mSpriteMiddle.getScaleY() * mSpriteMiddle.getHeight();
		return current;
	}

	private void setCurrentRaysLength(final float pNewRaysValue) {
		final float scale = pNewRaysValue / mSpriteMiddle.getHeight();
		mSpriteMiddle.setScaleY(scale);
		mSpriteBottom.setY(mSpriteMiddle.getY() + pNewRaysValue);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private class SunshineLengthModifier extends SingleValueSpanEntityModifier {
		private float mRaysTarget;

		public SunshineLengthModifier(float pDuration, float pFromValue,
				float pToValue, IEaseFunction pEaseFunction) {
			super(pDuration, pFromValue, pToValue, pEaseFunction);
			updateTarget(pToValue);
		}

		public SunshineLengthModifier(float pDuration, float pFromValue,
				float pToValue,
				IEntityModifierListener pEntityModifierListener,
				IEaseFunction pEaseFunction) {
			super(pDuration, pFromValue, pToValue, pEntityModifierListener, pEaseFunction);
			updateTarget(pToValue);
		}

		public SunshineLengthModifier(float pDuration, float pFromValue,
				float pToValue, IEntityModifierListener pEntityModifierListener) {
			super(pDuration, pFromValue, pToValue, pEntityModifierListener);
			updateTarget(pToValue);
		}

		public SunshineLengthModifier(float pDuration, float pFromValue,
				float pToValue) {
			super(pDuration, pFromValue, pToValue);
			updateTarget(pToValue);
		}

		protected SunshineLengthModifier(final SunshineLengthModifier pSunshineLengthModifier) {
			super(pSunshineLengthModifier);
			updateTarget(pSunshineLengthModifier.getToValue());
		}

		@Override
		protected void onSetInitialValue(IEntity pItem, float pValue) {
			Sunshine s = (Sunshine) pItem;
			s.setCurrentRaysLength(pValue);
		}

		@Override
		protected void onSetValue(IEntity pItem, float pPercentageDone, float pValue) {
			float from = getFromValue();
			float to = getTarget();
			float span = to - from;

			Sunshine s = (Sunshine) pItem;
			s.setCurrentRaysLength(from + span * pPercentageDone);
		}

		@Override
		public SunshineLengthModifier deepCopy() {
			return new SunshineLengthModifier(this);
		}

		@Override
		public void reset(float pDuration, float pFromValue, float pToValue) {
			super.reset(pDuration, pFromValue, pToValue);
			updateTarget(pToValue);
		}

		public void updateTarget(float pTarget) {
			mRaysTarget = pTarget;
		}

		public float getTarget() {
			return mRaysTarget;
		}
	}
}
