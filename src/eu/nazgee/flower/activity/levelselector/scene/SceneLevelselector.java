package eu.nazgee.flower.activity.levelselector.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.content.Context;
import eu.nazgee.flower.Consts;
import eu.nazgee.flower.base.pagerscene.ArrayLayout;
import eu.nazgee.flower.base.pagerscene.ArrayLayout.eAnchorPointXY;
import eu.nazgee.flower.base.pagerscene.IPage;
import eu.nazgee.flower.base.pagerscene.PageMoverCameraZoom;
import eu.nazgee.flower.base.pagerscene.PageRectangleTransparent;
import eu.nazgee.flower.base.pagerscene.ScenePager;
import eu.nazgee.flower.level.GameLevel;
import eu.nazgee.game.utils.helpers.AtlasLoader;
import eu.nazgee.game.utils.loadable.LoadableResourceSimple;

public class SceneLevelselector extends ScenePager<GameLevelItem>{

	// ===========================================================
	// Constants
	// ===========================================================
	public static final int ROWS = 3;
	public static final int COLS = 3;
	public static final float PAGE_WIDTH_EFFECTIVE = 0.66f;
	public static final float PAGE_WIDTH_FLIP = 0.3f;
	// ===========================================================
	// Fields
	// ===========================================================
	private final MyResources mResources = new MyResources();
	private final GameLevelsLoader mLevelItemsLoader = new GameLevelsLoader();
	private final LoadableParallaxBackground mLoadableParallaxBackground;
	private final Font mFontDesc;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneLevelselector(float W, float H,
			final Font pFontDesc, VertexBufferObjectManager pVertexBufferObjectManager, GameLevel level1) {
		super(W, H, pVertexBufferObjectManager, (int) (W * PAGE_WIDTH_FLIP));
		this.mFontDesc = pFontDesc;
		mLoadableParallaxBackground = new LoadableParallaxBackground(pVertexBufferObjectManager);
		getLoader().install(mResources);
		getLoader().install(mLevelItemsLoader);
		getLoader().install(mLoadableParallaxBackground);
		setBackgroundEnabled(true);
		setBackground(new Background(Color.BLUE));
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	@Override
	protected int getItemsNumber() {
		return mLevelItemsLoader.levels.size();
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onLoad(Engine e, Context c) {
		super.onLoad(e, c);
		setPageMover(new PageMoverCameraZoom<GameLevelItem>((SmoothCamera) e.getCamera(), getW()));
		setBackground(mLoadableParallaxBackground.getLoadedBacground());
	}

	@Override
	protected GameLevelItem populateItem(int pItem, int pItemOnPage, int pPage) {
		GameLevel lvl = mLevelItemsLoader.levels.get(pItem);
		GameLevelItem item = new GameLevelItem(lvl, mFontDesc, mResources.TEX_FRAME, getVertexBufferObjectManager());
		return item;
	}

	@Override
	protected IPage<GameLevelItem> populatePage(int pPageNumber) {
		IPage<GameLevelItem> page = new PageRectangleTransparent<GameLevelItem>(0, 0, getW() * PAGE_WIDTH_EFFECTIVE, getH(), 
				getVertexBufferObjectManager(),
				new ArrayLayout(COLS, ROWS, getW() * PAGE_WIDTH_EFFECTIVE, getH(), eAnchorPointXY.CENTERED));
		return page;
	}

	@Override
	protected void attachPage(final IPage<GameLevelItem> pPage, int pPageNumber) {
		pPage.setPosition(pPageNumber * getW(), 0);
		attachChild(pPage);
	}

	// ===========================================================
	// Methods
	// ===========================================================
	private int getFrameW() {
		return (int) (getW() * PAGE_WIDTH_EFFECTIVE / COLS);
	}

	private int getFrameH() {
		return (int) (getH()/ROWS);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private class MyResources extends LoadableResourceSimple {
		private BuildableBitmapTextureAtlas[] mAtlases;
		public ITextureRegion TEX_FRAME;

		@Override
		public void onLoadResources(Engine e, Context c) {

			mAtlases = new BuildableBitmapTextureAtlas[1];
			for (int i = 0; i < mAtlases.length; i++) {
				mAtlases[i] = new BuildableBitmapTextureAtlas(e.getTextureManager(), 1024, 1024, TextureOptions.REPEATING_BILINEAR);
			}
			BuildableBitmapTextureAtlas atlasMain = mAtlases[0];

			TEX_FRAME = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(atlasMain, c, Consts.FILE_LEVELSELECTOR_ITEM_FRAME, getFrameW(), getFrameH());

		}

		@Override
		public void onLoad(Engine e, Context c) {
			AtlasLoader.buildAndLoad(mAtlases);
		}

		@Override
		public void onUnload() {
			for (BuildableBitmapTextureAtlas atlas : mAtlases) {
				atlas.unload();
			}
		}
	}
}
