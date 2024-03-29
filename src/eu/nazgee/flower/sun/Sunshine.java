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

	private final float EASING_DURATION = 0.8f;
	private final float EASING_RESET_THRESHOLD = 50;

	// ===========================================================
	// Fields
	// ===========================================================

	private final Sprite mSpriteRayBody;
	private final Sprite mSpriteRayTail;

	private final SunshineLengthModifier mSunshineLengthModifier = new SunshineLengthModifier(0, 0, 0, EaseBounceOut.getInstance());

	// ===========================================================
	// Constructors
	// ===========================================================

	public Sunshine (final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVBO) {
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

	public void setTargetTop(final IEntity pTarget, final Sky pSky) {
		final float target = pSky.getHeightOnSkyTop(pTarget);
		setTarget(target, pSky);
	}

	public void setTargetCenter(final Entity pTarget, final Sky pSky) {
		final float target = pSky.getHeightOnSkyCenter(pTarget);
		setTarget(target, pSky);
	}

	public void setTarget(float pTargetHeightOnSky, final Sky pSky) {
		final float myHeightOnSky = pSky.getHeightOnSkyCenter(this);
		if (myHeightOnSky < pTargetHeightOnSky) {
			pTargetHeightOnSky = pSky.getHeightOnSky(0);
		}
		configureRaybeamLengthModifier(Math.max(0, myHeightOnSky - pTargetHeightOnSky));
	}

	public boolean isShiningAt(final IEntity pTarget) {
		final float pos[] = pTarget.getSceneCenterCoordinates();
//		pos = mSpriteRayTail.convertSceneCoordinatesToLocalCoordinates(pos[Constants.VERTEX_INDEX_X], pos[Constants.VERTEX_INDEX_Y]);
		return mSpriteRayBody.contains(pos[Constants.VERTEX_INDEX_X], pos[Constants.VERTEX_INDEX_Y]) ||
				mSpriteRayTail.contains(pos[Constants.VERTEX_INDEX_X], pos[Constants.VERTEX_INDEX_Y]);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
	}
	// ===========================================================
	// Methods
	// ===========================================================

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

		public SunshineLengthModifier(final float pDuration, final float pFromValue,
				final float pToValue, final IEaseFunction pEaseFunction) {
			super(pDuration, pFromValue, pToValue, pEaseFunction);
			setTarget(pToValue);
		}

		public SunshineLengthModifier(final float pDuration, final float pFromValue,
				final float pToValue,
				final IEntityModifierListener pEntityModifierListener,
				final IEaseFunction pEaseFunction) {
			super(pDuration, pFromValue, pToValue, pEntityModifierListener, pEaseFunction);
			setTarget(pToValue);
		}

		public SunshineLengthModifier(final float pDuration, final float pFromValue,
				final float pToValue, final IEntityModifierListener pEntityModifierListener) {
			super(pDuration, pFromValue, pToValue, pEntityModifierListener);
			setTarget(pToValue);
		}

		public SunshineLengthModifier(final float pDuration, final float pFromValue,
				final float pToValue) {
			super(pDuration, pFromValue, pToValue);
			setTarget(pToValue);
		}

		protected SunshineLengthModifier(final SunshineLengthModifier pSunshineLengthModifier) {
			super(pSunshineLengthModifier);
			setTarget(pSunshineLengthModifier.getToValue());
		}

		@Override
		protected void onSetInitialValue(final IEntity pItem, final float pValue) {
			final Sunshine s = (Sunshine) pItem;
			s.setRayBeamLength(pValue);
		}

		@Override
		protected void onSetValue(final IEntity pItem, final float pPercentageDone, final float pValue) {
			final float from = getFromValue();
			final float to = getTarget();
			final float span = to - from;

			final Sunshine s = (Sunshine) pItem;
			s.setRayBeamLength(from + span * pPercentageDone);
		}

		@Override
		public SunshineLengthModifier deepCopy() {
			return new SunshineLengthModifier(this);
		}

		@Override
		public void reset(final float pDuration, final float pFromValue, final float pToValue) {
			super.reset(pDuration, pFromValue, pToValue);
			setTarget(pToValue);
		}

		public void setTarget(final float pTarget) {
			mRaysTarget = pTarget;
		}

		public float getTarget() {
			return mRaysTarget;
		}
	}
}
