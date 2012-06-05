package eu.nazgee.flower.activity.game.scene.shop;

import org.andengine.engine.Engine;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.flower.BaseHUD;
import eu.nazgee.flower.Consts;
import eu.nazgee.game.utils.helpers.AtlasLoader;

public class HudShop extends BaseHUD {
	private Sprite mButtonDone;

	// ===========================================================
	// Constants
	// ===========================================================
	// ===========================================================
	// Fields
	// ===========================================================
	private IHudListener mHudListener;
	// ===========================================================
	// Constructors
	// ===========================================================
	public HudShop(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, 1, pVertexBufferObjectManager);

		// make sure, that we use our new version of resources, instead of a base one
		getLoader().uninstall(mResources);
		mResources = new ShopResources();
		getLoader().install(mResources);
	}

	public void setTextCash(CharSequence pText) {
		setTextLine(0, pText);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public IHudListener getHudListener() {
		return mHudListener;
	}

	public void setHudListener(IHudListener mHudListener) {
		this.mHudListener = mHudListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onLoad(Engine e, Context c) {
		mButtonDone = new Sprite(0, 0, ((ShopResources)mResources).TEX_BUTTON_DONE, getVertexBufferObjectManager()) {
			ClickDetector mDetector = new ClickDetector(new IClickDetectorListener() {
				@Override
				public void onClick(ClickDetector pClickDetector, int pPointerID,
						float pSceneX, float pSceneY) {
					registerEntityModifier(new RotationByModifier(0.1f, 30));
					if (null != getHudListener()) {
						getHudListener().onFinishedClicked();
					}
				}
			});
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				return mDetector.onTouchEvent(pSceneTouchEvent);
			}
		};
		attachChild(mButtonDone);
		this.mButtonDone.setPosition(getW() - mButtonDone.getWidth(), getH() - mButtonDone.getHeight());

		this.registerTouchArea(mButtonDone);
	}

	// ===========================================================
	// Methods
	// ===========================================================
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public interface IHudListener {
		public void onFinishedClicked();
	}

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
