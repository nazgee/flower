package eu.nazgee.flower.sun;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.SingleValueSpanEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;
import org.andengine.util.modifier.ease.EaseBounceOut;
import org.andengine.util.modifier.ease.IEaseFunction;

import eu.nazgee.flower.activity.game.scene.game.Sky;
import eu.nazgee.util.Anchor;
import eu.nazgee.util.Anchor.eAnchorPointXY;

public class Sunshine extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================

	private float EASING_DURATION = 0.8f;
	private float EASING_RESET_THRESHOLD = 50;

	// ===========================================================
	// Fields
	// ===========================================================

	private final Sprite mSpriteRayBody;
	private final Sprite mSpriteRayTail;

	private SunshineLengthModifier mSunshineLengthModifier = new SunshineLengthModifier(0, 0, 0, EaseBounceOut.getInstance());

	// ===========================================================
	// Constructors
	// ===========================================================

	public Sunshine (final ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVBO) {
		mSpriteRayBody = new Sprite(0, 0, pTiledTextureRegion.getTextureRegion(0), pVBO);
		mSpriteRayTail = new Sprite(0, 0, pTiledTextureRegion.getTextureRegion(1), pVBO);

		attachChild(mSpriteRayBody);
		attachChild(mSpriteRayTail);

		Anchor.setPosTopMiddleAtParent(mSpriteRayBody, eAnchorPointXY.CENTERED);
		Anchor.setPosTopMiddleAtParent(mSpriteRayTail, eAnchorPointXY.CENTERED);

		mSpriteRayBody.setScaleCenterY(eAnchorPointXY.TOP_MIDDLE.y.ratio);

		setRayBeamLength(0);
		mSunshineLengthModifier.setAutoUnregisterWhenFinished(false);
		registerEntityModifier(mSunshineLengthModifier);

		configureRaybeamLengthModifier(100); // just a dummy value o start with
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setTargetTop(IEntity pTarget, Sky pSky) {
		float target = pSky.getHeightOnSkyTop(pTarget);
		setTarget(target, pSky);
	}

	public void setTargetCenter(Entity pTarget, Sky pSky) {
		float target = pSky.getHeightOnSky(pTarget);
		setTarget(target, pSky);
	}

	public boolean isShiningAt(IEntity pTarget) {
		float pos[] = pTarget.getSceneCenterCoordinates();
		return mSpriteRayTail.contains(pos[Constants.VERTEX_INDEX_X], pos[Constants.VERTEX_INDEX_Y]);
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
			pSkyHeight = pSky.getHeightOnSky(0);
		}
		configureRaybeamLengthModifier(Math.max(0, me - pSkyHeight));
	}

	private void configureRaybeamLengthModifier(final float pTargetRays) {
		if (isRaybeamModifierOffTrack(pTargetRays)) {
			mSunshineLengthModifier.reset(EASING_DURATION, getRayBeamLength(), pTargetRays);
		} else {
			mSunshineLengthModifier.setTarget(pTargetRays);
		}
	}

	private boolean isRaybeamModifierOffTrack(final float pNewRaysValue) {
		return mSunshineLengthModifier.isFinished() || (EASING_RESET_THRESHOLD < Math.abs(mSunshineLengthModifier.getTarget() - pNewRaysValue));
	}

	private float getRayBeamLength() {
		final float current = mSpriteRayBody.getScaleY() * mSpriteRayBody.getHeight();
		return current;
	}

	private void setRayBeamLength(final float pNewRaysValue) {
		final float scale = pNewRaysValue / mSpriteRayBody.getHeight();
		mSpriteRayBody.setScaleY(scale);
		mSpriteRayTail.setY(mSpriteRayBody.getY() - pNewRaysValue);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private class SunshineLengthModifier extends SingleValueSpanEntityModifier {
		private float mRaysTarget;

		public SunshineLengthModifier(float pDuration, float pFromValue,
				float pToValue, IEaseFunction pEaseFunction) {
			super(pDuration, pFromValue, pToValue, pEaseFunction);
			setTarget(pToValue);
		}

		public SunshineLengthModifier(float pDuration, float pFromValue,
				float pToValue,
				IEntityModifierListener pEntityModifierListener,
				IEaseFunction pEaseFunction) {
			super(pDuration, pFromValue, pToValue, pEntityModifierListener, pEaseFunction);
			setTarget(pToValue);
		}

		public SunshineLengthModifier(float pDuration, float pFromValue,
				float pToValue, IEntityModifierListener pEntityModifierListener) {
			super(pDuration, pFromValue, pToValue, pEntityModifierListener);
			setTarget(pToValue);
		}

		public SunshineLengthModifier(float pDuration, float pFromValue,
				float pToValue) {
			super(pDuration, pFromValue, pToValue);
			setTarget(pToValue);
		}

		protected SunshineLengthModifier(final SunshineLengthModifier pSunshineLengthModifier) {
			super(pSunshineLengthModifier);
			setTarget(pSunshineLengthModifier.getToValue());
		}

		@Override
		protected void onSetInitialValue(IEntity pItem, float pValue) {
			Sunshine s = (Sunshine) pItem;
			s.setRayBeamLength(pValue);
		}

		@Override
		protected void onSetValue(IEntity pItem, float pPercentageDone, float pValue) {
			float from = getFromValue();
			float to = getTarget();
			float span = to - from;

			Sunshine s = (Sunshine) pItem;
			s.setRayBeamLength(from + span * pPercentageDone);
		}

		@Override
		public SunshineLengthModifier deepCopy() {
			return new SunshineLengthModifier(this);
		}

		@Override
		public void reset(float pDuration, float pFromValue, float pToValue) {
			super.reset(pDuration, pFromValue, pToValue);
			setTarget(pToValue);
		}

		public void setTarget(float pTarget) {
			mRaysTarget = pTarget;
		}

		public float getTarget() {
			return mRaysTarget;
		}
	}
}
