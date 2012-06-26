package eu.nazgee.flower.pool.butterfly;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.CubicBezierCurveMoveModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathConstants;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;

import eu.nazgee.flower.Consts;
import eu.nazgee.flower.pool.butterfly.ButterflyPool.ButterflyItem;
import eu.nazgee.misc.Animator;
import eu.nazgee.util.Anchor.eAnchorPointXY;

public class Butterfly extends AnimatedSprite {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final float CURVE_TIME = 1.2f;
	private static final long FRAME_TIME = 40;
	private static final float FLIGHT_RANGE = Consts.CAMERA_HEIGHT * 0.2f;
	// ===========================================================
	// Fields
	// ===========================================================
	private final Animator mAnimator = new Animator(this);
	private final RotateToHeading mRotator = new RotateToHeading();
	private final LifetimeLisetner mLifetimeListener = new LifetimeLisetner();
	private final ButterflyItem mPoolItem;
	// ===========================================================
	// Constructors
	// ===========================================================
	public Butterfly(float pX, float pY, ITiledTextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, final ButterflyItem pPoolItem) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		mPoolItem = pPoolItem;
		setScaleCenter(eAnchorPointXY.BOTTOM_MIDDLE.x.ratio, eAnchorPointXY.BOTTOM_MIDDLE.y.ratio);
		registerUpdateHandler(mRotator);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void animate(final float pX, final float pY) {
		// start wings flapping
		animate(FRAME_TIME);

		// set initial position
		setPosition(pX, pY);

		// prepare smooth bezzier path
		final float offset = FLIGHT_RANGE;
		float startX = pX;
		float startY = pY;
		float cnt1X = MathUtils.random(pX - offset, pX + offset);
		float cnt1Y = MathUtils.random(pY - offset, pY + offset);
		IEntityModifier bezziers[] = new IEntityModifier[5];
		for (int i = 0; i < bezziers.length; i++) {
			float cnt2X = MathUtils.random(pX - offset, pX + offset);
			float cnt2Y = MathUtils.random(pY - offset, pY + offset);
			float endX = MathUtils.random(pX - offset, pX + offset);
			float endY = MathUtils.random(pY - offset, pY + offset);
			bezziers[i] = new CubicBezierCurveMoveModifier(CURVE_TIME,
					startX, startY,
					cnt1X, cnt1Y,
					cnt2X, cnt2Y,
					endX, endY);
			startX = endX;
			startY = endY;
			float p = MathUtils.random(-0.5f, 0.5f);
			cnt1X = (endX - p * cnt2X) / (1 - p);
			cnt1Y = (endY - p * cnt2Y) / (1 - p);
		}

		// register bezzier path
		IEntityModifier mod = new ParallelEntityModifier(
				new SequenceEntityModifier(
						new FadeInModifier(CURVE_TIME/2),
						new DelayModifier(bezziers.length * CURVE_TIME - CURVE_TIME),
						new FadeOutModifier(CURVE_TIME/2)
						),
				new SequenceEntityModifier(bezziers));
		mod.addModifierListener(mLifetimeListener);
		mAnimator.runModifier(mod);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private class LifetimeLisetner implements IModifierListener<IEntity> {
		@Override
		public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
		}
		
		@Override
		public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
			synchronized (Butterfly.this) {
				mPoolItem.scheduleDetachAndRecycle();
			}
		}
	}
	private class RotateToHeading implements IUpdateHandler {
		float lastX;
		float lastY;
		@Override
		public void onUpdate(float pSecondsElapsed) {
			final float dX = getX() - lastX;
			final float dY = getY() - lastY;
			float angle = MathUtils.atan2(dY, dX);
			lastX = getX();
			lastY = getY();
			if(angle < 0) {
				angle += MathConstants.PI_TWICE; /* 360 degrees. */
			}
			setRotation(-MathUtils.radToDeg(angle) + 90);
		}
		@Override
		public void reset() {
		}
	}
}