package eu.nazgee.flower.activity.game.scene.shop;

import org.andengine.engine.Engine;
import org.andengine.entity.text.Text;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.flower.BaseButton;
import eu.nazgee.flower.BaseHUD;
import eu.nazgee.flower.Consts;
import eu.nazgee.game.utils.helpers.AtlasLoader;

public class HudShop extends BaseHUD {

	// ===========================================================
	// Constants
	// ===========================================================
	// ===========================================================
	// Fields
	// ===========================================================
	public BaseButton mButtonDone;

	// ===========================================================
	// Constructors
	// ===========================================================
	public HudShop(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, 2, pVertexBufferObjectManager);

		// make sure, that we use our new version of resources, instead of a base one
		getLoader().uninstall(mResources);
		mResources = new ShopResources();
		getLoader().install(mResources);
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

		mButtonDone = new BaseButton(0, 0, ((ShopResources)mResources).TEX_BUTTON_DONE, getVertexBufferObjectManager());
		attachChild(mButtonDone);
		this.mButtonDone.setPosition(getW() - mButtonDone.getWidth(), getH() - mButtonDone.getHeight());

		this.registerTouchArea(mButtonDone);
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void setTextCash(CharSequence pText) {
		setTextLine(0, pText);
	}
	public void setTextBasketValue(CharSequence pText) {
		setTextLine(1, pText);
	}
	public Text getTextBasket() {
		return getTextLine(1);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	protected class ShopResources extends HudResources {
		private BuildableBitmapTextureAtlas[] mAtlases;

		public ITextureRegion TEX_BUTTON_DONE;

		@Override
		public void onLoadResources(Engine e, Context c) {
			super.onLoadResources(e, c);

			mAtlases = new BuildableBitmapTextureAtlas[1];
			for (int i = 0; i < mAtlases.length; i++) {
				mAtlases[i] = new BuildableBitmapTextureAtlas(e.getTextureManager(), 1024, 1024, TextureOptions.REPEATING_BILINEAR);
			}

			TEX_BUTTON_DONE = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(mAtlases[0], c,
					Consts.FILE_SHOP_BUTTON_DONE, (int) (getW() * 0.3f), (int) (getH() * 0.3f));
		}

		@Override
		public void onLoad(Engine e, Context c) {
			super.onLoad(e, c);

			AtlasLoader.buildAndLoad(mAtlases);
		}

		@Override
		public void onUnload() {
			super.onUnload();

			for (BuildableBitmapTextureAtlas atlas : mAtlases) {
				atlas.unload();
			}
		}
	}
}
