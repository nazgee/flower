package eu.nazgee.flower.rainbow;

import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.ease.EaseElasticOut;

import eu.nazgee.misc.Animator;
import eu.nazgee.util.Anchor;
import eu.nazgee.util.Anchor.eAnchorPointXY;

public class Rainbow extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final float ANIMATION_TIME = 2;
	// ===========================================================
	// Fields
	// ===========================================================
	private final Animator mAnimator = new Animator(this);
	// ===========================================================
	// Constructors
	// ===========================================================
	public Rainbow(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
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
	public void animate() {
		mAnimator.runModifier(new ScaleModifier(ANIMATION_TIME, 0, 1, EaseElasticOut.getInstance()));
		detachChildren();
	}

	public void animate(final Font pFont, final String pText, final Color pColor) {
		animate();
		Text text = new Text(0, 0, pFont, pText, getVertexBufferObjectManager());
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
}
