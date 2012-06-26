package eu.nazgee.flower.rainbow;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.CubicBezierCurveMoveModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import eu.nazgee.misc.Animator;
import eu.nazgee.util.Anchor.eAnchorPointXY;

public class Butterfly extends AnimatedSprite {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final float ANIMATION_TIME = 1;
	// ===========================================================
	// Fields
	// ===========================================================
	private final Animator mAnimator = new Animator(this);
	// ===========================================================
	// Constructors
	// ===========================================================
	public Butterfly(float pX, float pY, ITiledTextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		setScaleCenter(eAnchorPointXY.BOTTOM_MIDDLE.x.ratio, eAnchorPointXY.BOTTOM_MIDDLE.y.ratio);
		registerUpdateHandler(new IUpdateHandler() {
			float lastX;
			float lastY;
			@Override
			public void onUpdate(float pSecondsElapsed) {
				final float dX = getX() - lastX;
				final float dY = getY() - lastY;
				final float len = MathUtils.length(dX, dY);
				final float angle = MathUtils.atan2(dY, dX);
				setRotation(MathUtils.radToDeg(-angle));
				lastX = getX();
				lastY = getY();
			}
			@Override
			public void reset() {
			}
		});
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
		setPosition(pX, pY);
		final float offset = 100;
		float startX = pX;
		float startY = pY;
		IEntityModifier mods[] = new IEntityModifier[5];
		for (int i = 0; i < mods.length; i++) {
			float cnt1X = MathUtils.random(pX - offset, pY + offset);
			float cnt1Y = MathUtils.random(pX - offset, pY + offset);
			float cnt2X = MathUtils.random(pX - offset, pY + offset);
			float cnt2Y = MathUtils.random(pX - offset, pY + offset);
			float endX = MathUtils.random(pX - offset, pY + offset);
			float endY = MathUtils.random(pX - offset, pY + offset);
			mods[i] = new CubicBezierCurveMoveModifier(ANIMATION_TIME, startX, startY, cnt1X, cnt1Y, cnt2X, cnt2Y, endX, endY);
			startX = endX;
			startY = endY;
		}

		mAnimator.runModifier(new SequenceEntityModifier(mods));
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}