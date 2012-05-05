package eu.nazgee.game.flower.activity.pager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.math.MathUtils;

import android.content.Context;
import android.util.Log;
import eu.nazgee.game.flower.Consts;
import eu.nazgee.game.flower.GameLevel;
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;
import eu.nazgee.game.utils.scene.SceneLoadable;

public class ScenePager extends SceneLoadable implements IOnSceneTouchListener, IScrollDetectorListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final MyResources mResources = new MyResources();
	private Camera mCamera;
	private final SurfaceScrollDetector mSurfaceScrollDetector = new SurfaceScrollDetector(this);
	private int mScrollDistanceX;

	private int mCurrentPage;
	private int mItemsCount;
	private int mColums;
	private int mRows;
	private int mLevelsPerPage;
	private int mLevelsPages;
	private int maxLevelReached = 15;
	private final static int LEVEL_PADDING = 30;
	private final static int TURN_PAGE_DISTANCE = 150;

	// ===========================================================
	// Constructors
	// ===========================================================
	public ScenePager(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pVertexBufferObjectManager);
		getLoader().install(mResources);
		mItemsCount = 60;
		mColums = 3;
		mRows = 3;
		mLevelsPerPage = mColums * mRows;
		mLevelsPages = (mItemsCount % mLevelsPerPage) == 0 ? (mItemsCount / mLevelsPerPage) : (mItemsCount / mLevelsPerPage) + 1;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onLoadResources(Engine e, Context c) {
		mCamera = e.getCamera();
	}

	@Override
	public void onLoad(Engine e, Context c) {
		e.registerUpdateHandler(new FPSLogger());

		setBackground(new Background(0.9f, 0.9f, 0.9f));

		setOnSceneTouchListener(this);
		setOnSceneTouchListenerBindingOnActionDownEnabled(true);

		createLevelBoxes();
	}

	@Override
	public void onUnload() {
	}


	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		return mSurfaceScrollDetector.onTouchEvent(pSceneTouchEvent);
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		mScrollDistanceX = 0;
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		mCamera.offsetCenter(-pDistanceX, 0);

		mScrollDistanceX += pDistanceX;
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		Log.d(getClass().getSimpleName(), "page: " + mCurrentPage);

		if ((mScrollDistanceX > TURN_PAGE_DISTANCE) && (mCurrentPage > 0)) {
			Log.d(getClass().getSimpleName(), "page dec");
			mCurrentPage--;
		} else if ((mScrollDistanceX < -TURN_PAGE_DISTANCE)
				&& (mCurrentPage < mLevelsPages - 1)) {
			Log.d(getClass().getSimpleName(), "page inc");
			mCurrentPage++;
		}
		mCurrentPage = MathUtils.bringToBounds(0, mLevelsPages, mCurrentPage);
		mCamera.setCenter(mCurrentPage * getW() + getW()/2, mCamera.getCenterY());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void createLevelBoxes() {
		int spaceBetweenRaws = (int) ((getH() / mRows)
				- LEVEL_PADDING);
		int spaceBetweenColumns = (int) ((getW() / mColums)
				- LEVEL_PADDING);

		int level = 0;

		int boxX = LEVEL_PADDING;
		int boxY = LEVEL_PADDING;

		for (int i = 0; i < mLevelsPages; i++) {
			int startX = (int) (i * getW());

			for (int j = 0; j < mRows; j++) {
				for (int k = 0; k < mColums; k++) {
					final int levelToLoad = level;
					Rectangle box = new Rectangle(startX + boxX, boxY, 100,
							100, getVertexBufferObjectManager()) {
						SelectDetector selectDetector = new SelectDetector();
						ClickDetector clickDetector = new ClickDetector(selectDetector);
						@Override
						public boolean onAreaTouched(
								TouchEvent pSceneTouchEvent,
								float pTouchAreaLocalX, float pTouchAreaLocalY) {
							boolean ret = clickDetector.onTouchEvent(pSceneTouchEvent);
							Log.d(getClass().getSimpleName(), "area_handled=" + ret);
							return ret;
						}

						class SelectDetector implements IClickDetectorListener {
							@Override
							public void onClick(
									org.andengine.input.touch.detector.ClickDetector pClickDetector,
									int pPointerID, float pSceneX, float pSceneY) {
								loadLevel(levelToLoad);
							}
						}
					};
					if (level >= maxLevelReached ) {
						box.setColor(0, 0, 0.9f);
					} else {
						box.setColor(0, 0.9f, 0);
					}
					attachChild(box);
					registerTouchArea(box);
					int textOffX = 0;
					if (level < 10) {
						textOffX = 28;
					} else {
						textOffX = 20;
					}
					box.attachChild(new Text(textOffX, 20, mResources.FONT_HUD, String
							.valueOf(level + 1), getVertexBufferObjectManager()));

					level++;
					boxX += spaceBetweenColumns + LEVEL_PADDING;
					if (level > mItemsCount) {
						break;
					}
				}
				if (level > mItemsCount) {
					break;
				}
				boxY += spaceBetweenRaws + LEVEL_PADDING;
				boxX = LEVEL_PADDING;
			}

			boxY = LEVEL_PADDING;
		}
	}

	/**
	 * 
	 * @param level
	 */
	private void loadLevel(final int level) {

	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public interface ILevelselectorListener {
		void loadLevel(GameLevel pLevel);
	}

	private static class MyResources extends SimpleLoadableResource {
		public volatile Font FONT_HUD;

		@Override
		public void onLoadResources(Engine e, Context c) {
		}

		@Override
		public void onLoad(Engine e, Context c) {
			final TextureManager textureManager = e.getTextureManager();
			final FontManager fontManager = e.getFontManager();

			final ITexture font_texture = new BitmapTextureAtlas(textureManager, 512, 256, TextureOptions.BILINEAR);
			FONT_HUD = FontFactory.createFromAsset(fontManager, font_texture, c.getAssets(), Consts.HUD_FONT, Consts.CAMERA_HEIGHT*0.1f, true, Color.WHITE.getARGBPackedInt());
			FONT_HUD.load();
		}

		@Override
		public void onUnload() {
			FONT_HUD.unload();
		}
	}
}
