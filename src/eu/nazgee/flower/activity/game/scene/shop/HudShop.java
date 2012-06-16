package eu.nazgee.flower.activity.game.scene.shop;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.bases.BaseHUD;
import eu.nazgee.util.NineSliceSprite;

public class HudShop extends BaseHUD {

	// ===========================================================
	// Constants
	// ===========================================================
	protected static int ZINDEX_BG_FRAME = -1;
	// ===========================================================
	// Fields
	// ===========================================================
	public ButtonSprite mButtonDone;
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
	public ButtonSprite getButtonDone() {
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
		mButtonDone = new ButtonSprite(0, 0, mTexturesLibrary.getIconCheck(), getVertexBufferObjectManager());
		attachChild(mButtonDone);
		this.mButtonDone.setPosition(getW() - mButtonDone.getWidth(), getH() - mButtonDone.getHeight());
		this.registerTouchArea(mButtonDone);

		/*
		 * Prepare cash icon
		 */
		final float h = getTextCash().getHeight();
		Sprite cash = new Sprite(0, 0, h, h, mTexturesLibrary.getIconCash(), getVertexBufferObjectManager());
		attachChild(cash);
		cash.setPosition(getW() - cash.getWidth(), 0);

		/*
		 * Prepare basket icon
		 */
		Sprite basket = new Sprite(0, 0, h, h, mTexturesLibrary.getIconShop(), getVertexBufferObjectManager());
		attachChild(basket);
		basket.setPosition(getW() - basket.getWidth(), cash.getY() + cash.getHeight());

		final NineSliceSprite bg = mTexturesLibrary.getFactory().populateFrameHudShop( 0.33f * getW(), getH(), getVertexBufferObjectManager());
		bg.setPosition(0.66f * getW(), 0);
		bg.setZIndex(ZINDEX_BG_FRAME);
		attachChild(bg);
//		final float gradW = camera.getWidth() * (1 - SceneLevelselector.PAGE_WIDTH_EFFECTIVE);
//		GradientRectangle grad = new GradientRectangle(camera.getWidth() - gradW, 0, gradW, camera.getHeight(), 5, getVertexBufferObjectManager());
//		attachChild(grad);
//		grad.setColor(new Color(Color.BLACK));
//		grad.setAlpha(0.9f);
//		grad.setGradientBand(0, new Color(0.5f, 0.8f, 0.5f));
//		grad.setGradientBand(1, new Color(0.1f, 0.2f, 0.1f));
//		grad.setGradientBand(2, new Color(0.1f, 0.3f, 0.1f));
//		grad.setGradientBand(3, new Color(0.0f, 0.2f, 0.0f));
//		grad.setGradientBand(4, new Color(0.0f, 0.2f, 0.0f));
//		grad.setGradientBandAlpha(0, 0.9f);
//		grad.setGradientBandAlpha(1, 1.0f);
//		grad.setGradientBandAlpha(2, 1.0f);
//		grad.setGradientBandAlpha(3, 1.0f);
//		grad.setGradientBandAlpha(4, 1.0f);
//
//		grad.setZIndex(ZINDEX_GRADIENT);
//
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
