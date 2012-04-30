package eu.nazgee.game.flower.scene.sun;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.SingleValueSpanEntityModifier;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.EaseElasticOut;
import org.andengine.util.modifier.ease.IEaseFunction;

import android.util.Log;
import eu.nazgee.game.flower.scene.Sky;
import eu.nazgee.game.utils.helpers.Positioner;

public class Sunshine extends Entity {
	private final Sprite mSpriteTop;
	private final Sprite mSpriteMiddle;
	private final Sprite mSpriteBottom;

	private SunshineLengthModifier mSunshineLengthModifier = new SunshineLengthModifier(0, 0, 0, EaseElasticOut.getInstance());

	private float EASING_DURATION = 1;
	private float EASING_RESET_THRESHOLD = 50;

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

		setRaysLength(0);
		mSunshineLengthModifier.setAutoUnregisterWhenFinished(false);
		registerEntityModifier(mSunshineLengthModifier);

		setRaysTarget(100);
	}

	private void setRaysLength(final float pNewRaysValue) {
		final float scale = pNewRaysValue / mSpriteMiddle.getHeight();
		mSpriteMiddle.setScaleY(scale);
		mSpriteBottom.setY(mSpriteMiddle.getY() + pNewRaysValue);
	}

	private boolean isChangeBig(final float pNewRaysValue) {
		return (EASING_RESET_THRESHOLD < Math.abs(mSunshineLengthModifier.getTarget() - pNewRaysValue));
	}

	public void setRaysTargetTop(IAreaShape pTarget, Sky pSky) {
		float target = pSky.getHeightOnSkyTop(pTarget);
		setRaysTargetSky(target, pSky);
	}

	public void setRaysTargetCenter(Entity pTarget, Sky pSky) {
		float target = pSky.getHeightOnSky(pTarget);
		setRaysTargetSky(target, pSky);
	}

	private void setRaysTargetSky(float pSkyHeight, Sky pSky) {
		float me = pSky.getHeightOnSky(this);
		if (me < pSkyHeight) {
			pSkyHeight = pSky.getHeightOnScene(0);
		}
		setRaysTarget(Math.max(0, me - pSkyHeight - mSpriteTop.getHeight()));
	}

	private void setRaysTarget(final float pTargetRays) {
		if (isChangeBig(pTargetRays) || mSunshineLengthModifier.isFinished()) {
			Log.w(getClass().getSimpleName(), "reset=" + pTargetRays);
			mSunshineLengthModifier.reset(EASING_DURATION, getRaysCurrent(), pTargetRays);
		} else {
			Log.d(getClass().getSimpleName(), "update=" + pTargetRays);
			mSunshineLengthModifier.updateTarget(pTargetRays);
		}
	}

	float getRaysCurrent() {
		final float current = mSpriteMiddle.getScaleY() * mSpriteMiddle.getHeight();
		Log.w(getClass().getSimpleName(), "current=" + current);
		return current;
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
	}

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
			s.setRaysLength(pValue);
		}

		@Override
		protected void onSetValue(IEntity pItem, float pPercentageDone, float pValue) {
			float from = getFromValue();
			float to = getTarget();
			float span = to - from;

			Sunshine s = (Sunshine) pItem;
			s.setRaysLength(from + span * pPercentageDone);
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
