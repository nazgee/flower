package eu.nazgee.flower.activity.levelselector.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.background.Background;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.content.Context;
import eu.nazgee.flower.LayoutBase.eAnchorPointXY;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.base.pagerscene.ArrayLayout;
import eu.nazgee.flower.base.pagerscene.IPage;
import eu.nazgee.flower.base.pagerscene.PageMoverCameraZoom;
import eu.nazgee.flower.base.pagerscene.PageRectangleTransparent;
import eu.nazgee.flower.base.pagerscene.ScenePager;
import eu.nazgee.flower.level.GameLevel;

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
	private final GameLevelsLoader mLevelItemsLoader = new GameLevelsLoader();
	private final LoadableParallaxBackground mLoadableParallaxBackground;
	private final Font mFontDesc;
	private HudLevelselector mHUD;
	private final TexturesLibrary mTexturesLibrary;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneLevelselector(float W, float H,
			final Font pFontDesc, VertexBufferObjectManager pVertexBufferObjectManager,
			final TexturesLibrary pTexturesLibrary, GameLevel level1) {
		super(W, H, pVertexBufferObjectManager, (int) (W * PAGE_WIDTH_FLIP));
		mFontDesc = pFontDesc;
		mTexturesLibrary = pTexturesLibrary;
		mLoadableParallaxBackground = new LoadableParallaxBackground(mTexturesLibrary, pVertexBufferObjectManager);
		getLoader().install(mLevelItemsLoader);
		getLoader().install(mLoadableParallaxBackground);
		setBackgroundEnabled(true);
		setBackground(new Background(Color.BLUE));

		// Install HUD
		this.mHUD = new HudLevelselector(W, H, pVertexBufferObjectManager);
		getLoader().install(this.mHUD);
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
		SmoothCamera camera = (SmoothCamera) e.getCamera();
		setPageMover(new PageMoverCameraZoom<GameLevelItem>(camera, getW() * PAGE_WIDTH_EFFECTIVE));
		setBackground(mLoadableParallaxBackground.getLoadedBacground());

		camera.setHUD(mHUD);
	}

	@Override
	protected GameLevelItem populateItem(int pItem, int pItemOnPage, int pPage) {
		GameLevel lvl = mLevelItemsLoader.levels.get(pItem);
		GameLevelItem item = new GameLevelItem(lvl, mFontDesc, mTexturesLibrary.getFrameLevel(), getFrameW(), getFrameH(), getVertexBufferObjectManager());
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
		pPage.setPosition(pPageNumber * getW() * PAGE_WIDTH_EFFECTIVE, 0);
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

}
