package eu.nazgee.flower.activity.game.scene.shop;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.color.Color;

import android.content.Context;
import eu.nazgee.flower.Consts;
import eu.nazgee.flower.Statics;
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
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;

public class SceneSeedsSelector extends ScenePager<SeedItem>{

	// ===========================================================
	// Constants
	// ===========================================================
	public static final int ROWS = 2;
	public static final int COLS = 2;
	// ===========================================================
	// Fields
	// ===========================================================
	private final MyResources mResources = new MyResources();
	private final LoadableParallaxBackground mLoadableParallaxBackground;
	private final GameLevel mGameLevel;
	private final SmartList<Seed> mSeeds;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneSeedsSelector(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager, GameLevel pGameLevel) {
		super(W, H, pVertexBufferObjectManager, (int) (W * 0.3f));
		this.mGameLevel = pGameLevel;
		this.mLoadableParallaxBackground = new LoadableParallaxBackground(pVertexBufferObjectManager);
		getLoader().install(this.mResources);
		getLoader().install(this.mLoadableParallaxBackground);
		setBackgroundEnabled(true);
		setBackground(new Background(Color.BLUE));
		this.mSeeds = this.mGameLevel.getSeeds();
		for (Seed seed : this.mSeeds) {
			getLoader().install(seed.resources);
		}
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	@Override
	protected int getItemsNumber() {
		return mSeeds.size();
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onLoadResources(Engine e, Context c) {
		super.onLoadResources(e, c);
		// Make sure that statics are safe to use from now on
		Statics.getInstanceSafe(e, c);
	}

	@Override
	public void onLoad(Engine e, Context c) {
		super.onLoad(e, c);
		setPageMover(new PageMoverCameraZoom<SeedItem>(e.getCamera(), getW()));
		setBackground(mLoadableParallaxBackground.getLoadedBacground());
	}

	@Override
	protected SeedItem populateItem(int pItem, int pItemOnPage, int pPage) {
		Seed seed = mSeeds.get(pItem);
		SeedItem item = new SeedItem(seed, Statics.getInstanceUnsafe().FONT_DESC, mResources.TEX_FRAME, getVertexBufferObjectManager());
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
	private class MyResources extends SimpleLoadableResource {
		private BuildableBitmapTextureAtlas[] mAtlases;
		public ITextureRegion TEX_FRAME;

		@Override
		public void onLoadResources(Engine e, Context c) {

			mAtlases = new BuildableBitmapTextureAtlas[1];
			for (int i = 0; i < mAtlases.length; i++) {
				mAtlases[i] = new BuildableBitmapTextureAtlas(e.getTextureManager(), 1024, 1024, TextureOptions.REPEATING_BILINEAR);
			}
			BuildableBitmapTextureAtlas atlasMain = mAtlases[0];

			TEX_FRAME = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(atlasMain, c, "hud/frame.svg", getFrameW(), getFrameH());

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
