package eu.nazgee.flower.activity.game.scene.shop;

import java.util.List;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.color.Color;

import android.content.Context;
import eu.nazgee.flower.Consts;
import eu.nazgee.flower.activity.levelselector.scene.LoadableParallaxBackground;
import eu.nazgee.flower.base.pagerscene.ArrayLayout;
import eu.nazgee.flower.base.pagerscene.ArrayLayout.eAnchorPointXY;
import eu.nazgee.flower.base.pagerscene.IPage;
import eu.nazgee.flower.base.pagerscene.PageMoverCameraZoom;
import eu.nazgee.flower.base.pagerscene.PageRectangleTransparent;
import eu.nazgee.flower.base.pagerscene.ScenePager;
import eu.nazgee.flower.level.GameLevel;
import eu.nazgee.flower.seed.Seed;
import eu.nazgee.game.utils.helpers.AtlasLoader;
import eu.nazgee.game.utils.loadable.LoadableResourceSimple;

public class SceneSeedsShop extends ScenePager<SeedItem>{

	// ===========================================================
	// Constants
	// ===========================================================
	public static final int ROWS = 2;
	public static final int COLS = 2;
	// ===========================================================
	// Fields
	// ===========================================================
	private final MyResources mResources = new MyResources();
	private final HudShop mHUD;
	private final LoadableParallaxBackground mLoadableParallaxBackground;
	private final GameLevel mGameLevel;
	private final SmartList<Seed> mSeeds;
	private final Font mDescFont;
	private IShoppingListener mShoppingListener;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneSeedsShop(float W, float H,
			final Font pDescFont,
			VertexBufferObjectManager pVertexBufferObjectManager, GameLevel pGameLevel) {
		super(W, H, pVertexBufferObjectManager, (int) (W * 0.3f));
		this.mDescFont = pDescFont;
		this.mGameLevel = pGameLevel;
		this.mHUD = new HudShop(W, H, pVertexBufferObjectManager);

		// Install shop's background
		this.mLoadableParallaxBackground = new LoadableParallaxBackground(pVertexBufferObjectManager);
		getLoader().install(this.mResources);
		getLoader().install(this.mLoadableParallaxBackground);
		getLoader().install(mHUD);

		setBackgroundEnabled(true);
		setBackground(new Background(Color.BLUE));

		// Get a collection of the seeds for current level
		this.mSeeds = this.mGameLevel.getSeeds();

		// Install all the seeds resources (this is needed, as long as seeds will
		// be considered as needing resources)
		for (Seed seed : this.mSeeds) {
			this.mResources.getLoader().install(seed.resources);
		}
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	@Override
	protected int getItemsNumber() {
		return this.mSeeds.size();
	}

	public IShoppingListener getShoppingListener() {
		return mShoppingListener;
	}

	public void setShoppingListener(IShoppingListener pShoppingListener) {
		this.mShoppingListener = pShoppingListener;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onLoad(Engine e, Context c) {
		super.onLoad(e, c);
		setPageMover(new PageMoverCameraZoom<SeedItem>(e.getCamera(), getW()));
		setBackground(mLoadableParallaxBackground.getLoadedBacground());

		e.getCamera().setHUD(mHUD);
	}

	@Override
	protected SeedItem populateItem(int pItem, int pItemOnPage, int pPage) {
		Seed seed = mSeeds.get(pItem);
		SeedItem item = new SeedItem(seed, mDescFont, mResources.TEX_FRAME, getVertexBufferObjectManager());
		return item;
	}

	@Override
	protected IPage<SeedItem> populatePage(int pPageNumber) {
		IPage<SeedItem> page = new PageRectangleTransparent<SeedItem>(0, 0, getW(), getH(), 
				getVertexBufferObjectManager(),
				new ArrayLayout(COLS, ROWS, getW(), getH(), eAnchorPointXY.CENTERED));
		return page;
	}

	@Override
	protected void attachPage(final IPage<SeedItem> pPage, int pPageNumber) {
		pPage.setPosition(pPageNumber * getW(), 0);
		attachChild(pPage);
	}

	// ===========================================================
	// Methods
	// ===========================================================
	private int getFrameW() {
		return (int) (getW()/COLS);
	}

	private int getFrameH() {
		return (int) (getH()/ROWS);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public interface IShoppingListener {
		public void onShoppingFinished(List<Seed> pBoughtItems);
	}

	private class MyResources extends LoadableResourceSimple {
		private BuildableBitmapTextureAtlas[] mAtlases;
		public ITextureRegion TEX_FRAME;
		public static final int SEEDS_ATLAS_NUM = 0;
		public static final int PLANTS_ATLAS_NUM = 1;
		public static final int MISC_ATLAS_NUM = 2;

		@Override
		public void onLoadResources(Engine e, Context c) {

			mAtlases = new BuildableBitmapTextureAtlas[3];
			for (int i = 0; i < mAtlases.length; i++) {
				mAtlases[i] = new BuildableBitmapTextureAtlas(e.getTextureManager(), 1024, 1024, TextureOptions.REPEATING_BILINEAR);

				if (i == SEEDS_ATLAS_NUM) {
					Seed.createSeedAssets(mAtlases[SEEDS_ATLAS_NUM], c, SceneSeedsShop.this.mSeeds);
				} else if (i == PLANTS_ATLAS_NUM) {
					Seed.createPlantAssets(mAtlases[PLANTS_ATLAS_NUM], c, SceneSeedsShop.this.mSeeds);
				} else {
				}
			}
			BuildableBitmapTextureAtlas atlasMain = mAtlases[MISC_ATLAS_NUM];

			TEX_FRAME = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(atlasMain, c, Consts.FILE_SHOP_ITEM_FRAME, getFrameW(), getFrameH());

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
