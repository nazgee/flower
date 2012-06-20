package eu.nazgee.flower.activity.levelselector.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import android.content.Context;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.activity.game.scene.game.GameBackground;
import eu.nazgee.flower.base.pagerscene.ArrayLayout;
import eu.nazgee.flower.base.pagerscene.IPage;
import eu.nazgee.flower.base.pagerscene.PageMoverCameraZoom;
import eu.nazgee.flower.base.pagerscene.PageRectangleTransparent;
import eu.nazgee.flower.base.pagerscene.ScenePager;
import eu.nazgee.flower.level.GameLevel;
import eu.nazgee.util.LayoutBase;
import eu.nazgee.util.LayoutBase.eAnchorPointXY;

public class SceneLevelselector extends ScenePager<GameLevelItem>{

	// ===========================================================
	// Constants
	// ===========================================================
	public static final int ROWS = 3;
	public static final int COLS = 3;
	public static final float PAGE_WIDTH_EFFECTIVE = 1f;
	public static final float PAGE_WIDTH_FLIP = 0.3f;
	// ===========================================================
	// Fields
	// ===========================================================
	private final GameLevelsLoader mLevelItemsLoader = new GameLevelsLoader();
	private final Font mFontDesc;
	private HudLevelselector mHUD;
	private final TexturesLibrary mTexturesLibrary;
	private GameBackground mBG;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneLevelselector(float W, float H,
			final Font pFontDesc, VertexBufferObjectManager pVertexBufferObjectManager,
			final TexturesLibrary pTexturesLibrary, GameLevel level1) {
		super(W, H, pVertexBufferObjectManager, (int) (W * PAGE_WIDTH_FLIP));
		mFontDesc = pFontDesc;
		mTexturesLibrary = pTexturesLibrary;
		getLoader().install(mLevelItemsLoader);
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
	public void onLoadResources(Engine e, Context c) {
		super.onLoadResources(e, c);
		mBG = new GameBackground(e.getCamera(), mTexturesLibrary, getVertexBufferObjectManager());
	}

	@Override
	public void onLoad(Engine e, Context c) {
		super.onLoad(e, c);
		SmoothCamera camera = (SmoothCamera) e.getCamera();
		setPageMover(new PageMoverCameraZoom<GameLevelItem>(0.7f, camera, getW() * PAGE_WIDTH_EFFECTIVE));
		setBackground(mBG);

//		ParallaxLayer paralaxLayer = new ParallaxLayer(camera.getWidth(), camera.getHeight(), camera, true);
//		paralaxLayer.setAutoParallaxChangePerSecond(1);
//		paralaxLayer.setScrollParallaxFactor(1f);
//
//		Sprite tree1 = new Sprite(0, camera.getHeight() - mTexturesLibrary.getTree(0).getHeight(), mTexturesLibrary.getTree(0), getVertexBufferObjectManager());
//		paralaxLayer.attachParallaxEntity(new ParallaxLayerEntity(-0.5f, tree1, true, 4));
//	
//		Sprite tree2 = new Sprite(0, camera.getHeight() - mTexturesLibrary.getTree(1).getHeight() - 150, mTexturesLibrary.getTree(1), getVertexBufferObjectManager());
//		paralaxLayer.attachParallaxEntity(new ParallaxLayerEntity(0.5f, tree2, true, 4));
//	
//		Sprite tree3 = new Sprite(0, camera.getHeight() - mTexturesLibrary.getTree(2).getHeight() - 300, mTexturesLibrary.getTree(2), getVertexBufferObjectManager());
//		paralaxLayer.attachParallaxEntity(new ParallaxLayerEntity(0.0f, tree3, true, 4));
//
//		attachChild(paralaxLayer);
//		paralaxLayer.setZIndex(-1);

		camera.setHUD(mHUD);
		attachChild(new Rectangle(getW()/2, getH()/2, 15, 15, getVertexBufferObjectManager()));
		attachChild(new Rectangle(getW(), getH(), 15, 15, getVertexBufferObjectManager()));
		attachChild(new Rectangle(getW(), 0, 15, 15, getVertexBufferObjectManager()));
		attachChild(new Rectangle(0, getH(), 15, 15, getVertexBufferObjectManager()));
		attachChild(new Rectangle(0, 0, 15, 15, getVertexBufferObjectManager()));

		sortChildren(false);
	}

	@Override
	protected GameLevelItem populateItem(int pItem, int pItemOnPage, int pPage) {
		GameLevel lvl = mLevelItemsLoader.levels.get(pItem);
		GameLevelItem item = new GameLevelItem(lvl, mFontDesc, 
				mTexturesLibrary.getFactory(),
				getFrameW(), getFrameH(), getVertexBufferObjectManager());
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
//		pPage.setPosition(pPageNumber * getW() * PAGE_WIDTH_EFFECTIVE, 0);
		LayoutBase.setItemPositionTopLeft(pPage, pPageNumber * getW() * PAGE_WIDTH_EFFECTIVE, 0);
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
