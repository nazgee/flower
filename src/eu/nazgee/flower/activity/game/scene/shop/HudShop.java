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
		 * Prepare basket icon
		 */
		final float h = getTextBasket().getHeight();
		Sprite basket = new Sprite(0, 0, h, h, mTexturesLibrary.getMain().get(TexturesMain.ICONS_SHOP_ID), getVertexBufferObjectManager());
		attachChild(basket);
		basket.setPosition(getW() - basket.getWidth(), 0);

		/*
		 * Prepare cash icon
		 */
		Sprite cash = new Sprite(0, 0, h, h, mTexturesLibrary.getMain().get(TexturesMain.ICONS_CASH_ID), getVertexBufferObjectManager());
		attachChild(cash);
		cash.setPosition(getW() - cash.getWidth(), basket.getY() + basket.getHeight());

		Gradient3Way grad = new Gradient3Way(camera.getWidth()/2, 0, camera.getWidth()/2, camera.getHeight(), 0.5f, getVertexBufferObjectManager());
		attachChild(grad);
		grad.setColor(Color.BLACK);
		grad.setGradientColor(eGradientPosition.GRADIENT_LEFT, Color.TRANSPARENT);
		grad.setZIndex(ZINDEX_GRADIENT);

		sortChildren();
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void setTextCash(CharSequence pText) {
		setTextLine(1, pText);
	}
	public void setTextBasketValue(CharSequence pText) {
		setTextLine(0, pText);
	}
	public Text getTextBasket() {
		return getTextLine(1);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
