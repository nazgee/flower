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
import eu.nazgee.flower.base.pagerscene.ScenePagerBasic;
import eu.nazgee.flower.level.GameLevel;
import eu.nazgee.util.Anchor;
import eu.nazgee.util.Anchor.eAnchorPointXY;

public class SceneLevelselector extends ScenePagerBasic<GameLevelItem>{

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final GameLevelsLoader mLevelItemsLoader = new GameLevelsLoader();
	private final Font mFontDesc;
	private final HudLevelselector mHUD;
	private final TexturesLibrary mTexturesLibrary;
	private GameBackground mBG;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneLevelselector(final float W, final float H,
			final Font pFontDesc, final VertexBufferObjectManager pVertexBufferObjectManager,
			final TexturesLibrary pTexturesLibrary) {
		super(W, H, pVertexBufferObjectManager, 3, 3, 1, 0.33f);
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
	public void onLoadResources(final Engine e, final Context c) {
		super.onLoadResources(e, c);
		mBG = new GameBackground(e.getCamera(), mTexturesLibrary, getVertexBufferObjectManager());
	}

	@Override
	public void onLoad(final Engine e, final Context c) {
		super.onLoad(e, c);
		final SmoothCamera camera = (SmoothCamera) e.getCamera();
		setPageMover(new PageMoverCameraZoom<GameLevelItem>(0.7f, camera, getEffectiveWidth()));
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

		final float BW = 100;
		final float BH = 100;
		final float TW = 20;
		final float TH = 20;

		final Rectangle base = new Rectangle(100, 100, BW, BH, getVertexBufferObjectManager());
		final Rectangle tl = new Rectangle(0, BH, TW, TH, getVertexBufferObjectManager());
		final Rectangle tr = new Rectangle(BW, BH, TW, TH, getVertexBufferObjectManager());
		final Rectangle bl = new Rectangle(0, 0, TW, TH, getVertexBufferObjectManager());
		final Rectangle br = new Rectangle(BW, 0, TW, TH, getVertexBufferObjectManager());

		base.setColor(Color.BLACK);
		tl.setColor(Color.RED);
		tr.setColor(Color.GREEN);
		bl.setColor(Color.BLUE);
		br.setColor(Color.YELLOW);

		attachChild(base);
		base.attachChild(tl);
		base.attachChild(tr);
		base.attachChild(bl);
		base.attachChild(br);

		Anchor.setPosTopLeftAtParent(tl, eAnchorPointXY.TOP_LEFT);
		Anchor.setPosTopRightAtParent(tr, eAnchorPointXY.TOP_RIGHT);
		Anchor.setPosBottomLeftAtParent(bl, eAnchorPointXY.BOTTOM_LEFT);
		Anchor.setPosBottomRightAtParent(br, eAnchorPointXY.BOTTOM_RIGHT);

		final Rectangle ttl = new Rectangle(0, BH, TW, TH, getVertexBufferObjectManager());
		final Rectangle ttr = new Rectangle(BW, BH, TW, TH, getVertexBufferObjectManager());
		final Rectangle bbl = new Rectangle(0, 0, TW, TH, getVertexBufferObjectManager());
		final Rectangle bbr = new Rectangle(BW, 0, TW, TH, getVertexBufferObjectManager());

		base.attachChild(ttl);
		base.attachChild(ttr);
		base.attachChild(bbl);
		base.attachChild(bbr);

		Anchor.setPosTopLeftAtSibling(ttl, tl, eAnchorPointXY.BOTTOM_RIGHT);
		Anchor.setPosTopRightAtSibling(ttr, tr, eAnchorPointXY.BOTTOM_LEFT);
		Anchor.setPosBottomLeftAtSibling(bbl, bl, eAnchorPointXY.TOP_RIGHT);
		Anchor.setPosBottomRightAtSibling(bbr, br, eAnchorPointXY.TOP_LEFT);

		sortChildren(false);
	}

	@Override
	protected GameLevelItem populateItem(final int pItem, final int pItemOnPage, final int pPage) {
		final GameLevel lvl = mLevelItemsLoader.levels.get(pItem);
		final GameLevelItem item = new GameLevelItem(lvl, mFontDesc,
				mTexturesLibrary.getFactory(),
				getFrameW(), getFrameH(), getVertexBufferObjectManager());
		return item;
	}

	@Override
	protected IPage<GameLevelItem> populatePage(final int pPageNumber) {
		final IPage<GameLevelItem> page = new PageRectangleTransparent<GameLevelItem>(0, 0, getEffectiveWidth(), getH(),
				getVertexBufferObjectManager(),
				new ArrayLayout(getCols(), getRrows(), getEffectiveWidth(), getH()));
		return page;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	private int getFrameW() {
		return (int) (getEffectiveWidth() / getCols());
	}

	private int getFrameH() {
		return (int) (getH() / getRrows());
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
