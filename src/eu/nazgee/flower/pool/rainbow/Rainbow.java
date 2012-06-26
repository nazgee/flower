package eu.nazgee.flower.pool.rainbow;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;
import org.andengine.util.modifier.ease.EaseElasticOut;

import eu.nazgee.flower.pool.rainbow.RainbowPool.RainbowItem;
import eu.nazgee.misc.Animator;
import eu.nazgee.util.Anchor;
import eu.nazgee.util.Anchor.eAnchorPointXY;

public class Rainbow extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================
	public static final float LIFE_TIME = 8;
	private static final float ANIMATION_TIME = LIFE_TIME * 0.15f;
	// ===========================================================
	// Fields
	// ===========================================================
	private final Animator mAnimator = new Animator(this);
	private final RainbowItem mPoolItem;
	private final RainbowLifetimeListener mLifetimeListener = new RainbowLifetimeListener();
	// ===========================================================
	// Constructors
	// ===========================================================
	public Rainbow(final float pX, final float pY, final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, final RainbowItem pPoolItem) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		mPoolItem = pPoolItem;
		setScaleCenter(eAnchorPointXY.BOTTOM_MIDDLE.x.ratio, eAnchorPointXY.BOTTOM_MIDDLE.y.ratio);
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
	public void fxPopOut() {
		detachChildren();
		setScale(0);
		setAlpha(1);
		final IEntityModifier mod = new SequenceEntityModifier(
				new ScaleModifier(ANIMATION_TIME, 0, 1, EaseElasticOut.getInstance()),
				new DelayModifier(LIFE_TIME - 2 * ANIMATION_TIME),
				new FadeOutModifier(ANIMATION_TIME)
				);
		mod.addModifierListener(mLifetimeListener);
		mAnimator.runModifier(mod);
	}

	public void fxPopOutWithText(final Font pFont, final String pText, final Color pColor) {
		fxPopOut();
		final Text text = new Text(0, 0, pFont, pText, getVertexBufferObjectManager());
		text.setColor(pColor);
		attachChild(text);
		Anchor.setPosTopMiddleAtParent(text, eAnchorPointXY.TOP_MIDDLE);
		text.registerEntityModifier(new SequenceEntityModifier(
				new FadeInModifier(ANIMATION_TIME),
				new DelayModifier(ANIMATION_TIME),
				new FadeOutModifier(ANIMATION_TIME)
				));

	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private class RainbowLifetimeListener implements IModifierListener<IEntity> {
		@Override
		public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {
		}

		@Override
		public void onModifierFinished(final IModifier<IEntity> pModifier, final IEntity pItem) {
			synchronized (Rainbow.this) {
				mPoolItem.scheduleDetachAndRecycle();
			}
		}
	}
}
