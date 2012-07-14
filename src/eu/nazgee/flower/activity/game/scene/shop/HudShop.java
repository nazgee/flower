package eu.nazgee.flower.activity.game.scene.shop;

import org.andengine.engine.Engine;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.bases.BaseHUD;
import eu.nazgee.util.Anchor;
import eu.nazgee.util.Anchor.eAnchorPointXY;
import eu.nazgee.util.NineSliceSprite;

public class HudShop extends BaseHUD {

	// ===========================================================
	// Constants
	// ===========================================================
	protected static int ZINDEX_BG_FRAME = -1;
	private static int TEXT_LINE_CASH = 0;
	private static int TEXT_LINE_BASKET = 1;
	// ===========================================================
	// Fields
	// ===========================================================
	public ButtonSprite mButtonDone;
	private final TexturesLibrary mTexturesLibrary;

	// ===========================================================
	// Constructors
	// ===========================================================
	public HudShop(final float W, final float H,
			final VertexBufferObjectManager pVertexBufferObjectManager, final TexturesLibrary pTexturesLibrary) {
		super(W, H, 2, pVertexBufferObjectManager);
		this.mTexturesLibrary = pTexturesLibrary;
	}


	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public ButtonSprite getButtonDone() {
		return mButtonDone;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onLoad(final Engine e, final Context c) {
		super.onLoad(e, c);
		e.getCamera();

		/*
		 * Prepare background
		 */
		final NineSliceSprite bg = mTexturesLibrary.getFactory().populateFrameHudShop( 0.33f * getW(), getH(), getVertexBufferObjectManager());
		Anchor.setPosBottomLeft(bg, 0.66f * getW(), 0);
		bg.setZIndex(ZINDEX_BG_FRAME);
		attachChild(bg);

		/*
		 * Prepare done button
		 */
		mButtonDone = new ButtonSprite(0, 0, mTexturesLibrary.getIconCheck(), getVertexBufferObjectManager());
		bg.attachChild(mButtonDone);
		mButtonDone.setPosition(bg.getWidth()/2, mButtonDone.getHeight()/2);
		registerTouchArea(mButtonDone);

		/*
		 * Prepare cash icon
		 */
		final float icons_height = getTextCash().getHeight() * 0.7f;
		final float margin = 15;
		final Sprite cash = new Sprite(0, 0, icons_height, icons_height, mTexturesLibrary.getIconCash(), getVertexBufferObjectManager());
		detachChild(getTextCash());
		bg.attachChild(getTextCash());
		bg.attachChild(cash);
		Anchor.setPosTopRight(cash, bg.getWidth() - margin, bg.getHeight() - margin);
		Anchor.setPosCenterRightAtSibling(getTextCash(), cash, eAnchorPointXY.CENTERED_LEFT);

		/*
		 * Prepare basket icon
		 */
		final Sprite basket = new Sprite(0, 0, icons_height, icons_height, mTexturesLibrary.getIconShop(), getVertexBufferObjectManager());
		detachChild(getTextBasket());
		bg.attachChild(getTextBasket());
		bg.attachChild(basket);
		Anchor.setPosTopMiddleAtSibling(basket, cash, eAnchorPointXY.BOTTOM_MIDDLE);
		Anchor.setPosCenterRightAtSibling(getTextBasket(), basket, eAnchorPointXY.CENTERED_LEFT);

		sortChildren();
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void setTextBasket(final CharSequence pText) {
		setTextLine(TEXT_LINE_BASKET, pText);
	}
	public void setTextCash(final CharSequence pText) {
		setTextLine(TEXT_LINE_CASH, pText);
	}
	public Text getTextCash() {
		return getTextLine(TEXT_LINE_CASH);
	}
	public Text getTextBasket() {
		return getTextLine(TEXT_LINE_BASKET);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
