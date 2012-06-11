package eu.nazgee.flower.activity.game.scene.shop;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.content.Context;
import eu.nazgee.flower.BaseButton;
import eu.nazgee.flower.BaseHUD;
import eu.nazgee.flower.Gradient3Way;
import eu.nazgee.flower.Gradient3Way.eGradientPosition;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.TexturesLibrary.TexturesMain;
import eu.nazgee.flower.activity.levelselector.scene.SceneLevelselector;

public class HudShop extends BaseHUD {

	// ===========================================================
	// Constants
	// ===========================================================
	protected static int ZINDEX_GRADIENT = -1;
	// ===========================================================
	// Fields
	// ===========================================================
	public BaseButton mButtonDone;
	private final TexturesLibrary mTexturesLibrary;

	// ===========================================================
	// Constructors
	// ===========================================================
	public HudShop(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager, TexturesLibrary pTexturesLibrary) {
		super(W, H, 2, pVertexBufferObjectManager);
		this.mTexturesLibrary = pTexturesLibrary;
	}


	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public BaseButton getButtonDone() {
		return mButtonDone;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onLoad(Engine e, Context c) {
		super.onLoad(e, c);
		Camera camera = e.getCamera();

		/*
		 * Prepare done button
		 */
		mButtonDone = new BaseButton(0, 0, mTexturesLibrary.getMain().get(TexturesMain.ICONS_CHECK_MARK_ID), getVertexBufferObjectManager());
		attachChild(mButtonDone);
		this.mButtonDone.setPosition(getW() - mButtonDone.getWidth(), getH() - mButtonDone.getHeight());
		this.registerTouchArea(mButtonDone);

		/*
		 * Prepare cash icon
		 */
		final float h = getTextCash().getHeight();
		Sprite cash = new Sprite(0, 0, h, h, mTexturesLibrary.getMain().get(TexturesMain.ICONS_CASH_ID), getVertexBufferObjectManager());
		attachChild(cash);
		cash.setPosition(getW() - cash.getWidth(), 0);

		/*
		 * Prepare basket icon
		 */
		Sprite basket = new Sprite(0, 0, h, h, mTexturesLibrary.getMain().get(TexturesMain.ICONS_SHOP_ID), getVertexBufferObjectManager());
		attachChild(basket);
		basket.setPosition(getW() - basket.getWidth(), cash.getY() + cash.getHeight());

		final float gradW = camera.getWidth() * (1 - SceneLevelselector.PAGE_WIDTH_EFFECTIVE);
		Gradient3Way grad = new Gradient3Way(camera.getWidth() - gradW, 0, gradW, camera.getHeight(), 0.1f, getVertexBufferObjectManager());
		attachChild(grad);
		Color col = new Color(0,0,0,1);
		grad.setColor(col);
		grad.setAlpha(0.9f);
		col.setAlpha(0);
		grad.setGradientColor(eGradientPosition.GRADIENT_LEFT, col);
		grad.setZIndex(ZINDEX_GRADIENT);

		sortChildren();
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void setTextBasket(CharSequence pText) {
		setTextLine(1, pText);
	}
	public void setTextCash(CharSequence pText) {
		setTextLine(0, pText);
	}
	public Text getTextCash() {
		return getTextLine(1);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
