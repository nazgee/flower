package eu.nazgee.flower.activity.game.scene.shop;

import java.util.List;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.color.Color;

import android.content.Context;
import eu.nazgee.flower.LayoutBase.eAnchorPointXY;
import eu.nazgee.flower.Consts;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.activity.levelselector.scene.LoadableParallaxBackground;
import eu.nazgee.flower.base.pagerscene.ArrayLayout;
import eu.nazgee.flower.base.pagerscene.IPage;
import eu.nazgee.flower.base.pagerscene.PageMoverCameraZoom;
import eu.nazgee.flower.base.pagerscene.PageRectangleTransparent;
import eu.nazgee.flower.base.pagerscene.ScenePager;
import eu.nazgee.flower.flower.Seed;
import eu.nazgee.flower.level.GameLevel;
import eu.nazgee.flower.pool.popup.PopupPool;
import eu.nazgee.flower.pool.popup.PopupPool.PopupItem;
import eu.nazgee.game.utils.helpers.AtlasLoader;
import eu.nazgee.game.utils.loadable.LoadableResourceSimple;

public class SceneSeedsShop extends ScenePager<SeedItem> {

	// ===========================================================
	// Constants
	// ===========================================================
	public static final int ROWS = 2;
	public static final int COLS = 2;
	public static final float PAGE_WIDTH_EFFECTIVE = 0.66f;
	public static final float PAGE_WIDTH_FLIP = 0.3f;
	// ===========================================================
	// Fields
	// ===========================================================
	private final MyResources mResources = new MyResources();
	private final HudShop mHUD;
	private final LoadableParallaxBackground mLoadableParallaxBackground;
	private final Font mDescFont;
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;
	private PopupPool mPopupPool;

	private IShoppingListener mShoppingListener;
	private final SeedsShop mShop;
	private final TexturesLibrary mTexturesLibrary;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneSeedsShop(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager,
			GameLevel pGameLevel, final Font pDescFont,
			final EntityDetachRunnablePoolUpdateHandler pDetacher,
			final TexturesLibrary pTexturesLibrary) {
		super(W, H, pVertexBufferObjectManager, (int) (W * PAGE_WIDTH_FLIP));
		this.mDescFont = pDescFont;
		this.mDetacher = pDetacher;
		this.mTexturesLibrary = pTexturesLibrary;

		// Install resources
		getLoader().install(this.mResources);

		// Install shop's background
		this.mLoadableParallaxBackground = new LoadableParallaxBackground(pVertexBufferObjectManager);
		getLoader().install(this.mLoadableParallaxBackground);
		setBackgroundEnabled(true);
		setBackground(new Background(Color.BLUE));

		// Install HUD
		this.mHUD = new HudShop(W, H, pVertexBufferObjectManager, pTexturesLibrary);
		getLoader().install(this.mHUD);

		// Prepare pools
		this.mPopupPool = new PopupPool(this.mDescFont, this.mDetacher, pVertexBufferObjectManager);

		// Create a shop
		this.mShop = new SeedsShop(pGameLevel);

		// Install all the seeds resources (this is needed, as long as seeds will
		// be considered as needing resources)
		for (Seed seed : this.mShop.getSeedsInShop()) {
			this.mResources.getLoader().install(seed.resources);
		}

	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	@Override
	protected int getItemsNumber() {
		return this.mShop.getSeedsInShop().size();
	}

	public final SeedsShop getShop() {
		return this.mShop;
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
	protected void callClickListener(SeedItem pItem) {
		if (mShop.addToBasket(pItem.getSeed())) {
			updateHUDBasket();
			updateHUDCash();
			PopupItem popup = mPopupPool.obtainPoolItem();
			popup.getEntity().put(pItem, "$" + pItem.getSeed().cost);
			popup.getEntity().fxMoveTo(0.75f, mHUD.getTextCash());
			attachChild(popup.getEntity());
		}
		super.callClickListener(pItem);
	}

	@Override
	public void onLoad(Engine e, Context c) {
		super.onLoad(e, c);
		setPageMover(new PageMoverCameraZoom<SeedItem>((SmoothCamera) e.getCamera(), getW() * PAGE_WIDTH_EFFECTIVE));
		setBackground(mLoadableParallaxBackground.getLoadedBacground());

		e.getCamera().setHUD(mHUD);
		updateHUDBasket();
		updateHUDCash();

		// HUD buttons clicks
		this.mHUD.getButtonDone().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				if (null != getShoppingListener()) {
					getShoppingListener().onShoppingFinished(null);
				}
				
			}
		});
	}

	@Override
	public void onUnload() {
		super.onUnload();
		this.mShop.emptyBasket();
	}

	@Override
	protected SeedItem populateItem(int pItem, int pItemOnPage, int pPage) {
		Seed seed = this.mShop.getSeedsInShop().get(pItem);
		SeedItem item = new SeedItem(seed, mDescFont, mResources.TEX_FRAME, getVertexBufferObjectManager(), mTexturesLibrary);
		return item;
	}

	@Override
	protected IPage<SeedItem> populatePage(int pPageNumber) {
		IPage<SeedItem> page = new PageRectangleTransparent<SeedItem>(0, 0, getW() * PAGE_WIDTH_EFFECTIVE, getH(), 
				getVertexBufferObjectManager(),
				new ArrayLayout(COLS, ROWS, getW() * PAGE_WIDTH_EFFECTIVE, getH(), eAnchorPointXY.CENTERED));
		return page;
	}

	@Override
	protected void attachPage(final IPage<SeedItem> pPage, int pPageNumber) {
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
		return (int) (getH() / ROWS);
	}

	private void updateHUDBasket() {
		mHUD.setTextBasket("$" + mShop.getBasketValue());
	}

	private void updateHUDCash() {
		mHUD.setTextCash("$" + (mShop.getCustomerCash() - mShop.getBasketValue()));
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
		public static final int MISC_ATLAS_NUM = 0;

		@Override
		public void onLoadResources(Engine e, Context c) {

			mAtlases = new BuildableBitmapTextureAtlas[3];
			for (int i = 0; i < mAtlases.length; i++) {
				mAtlases[i] = new BuildableBitmapTextureAtlas(e.getTextureManager(), 2048, 2048, TextureOptions.REPEATING_BILINEAR);
			}
			BuildableBitmapTextureAtlas atlas = mAtlases[MISC_ATLAS_NUM];

			TEX_FRAME = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, c, Consts.FILE_SHOP_ITEM_FRAME, getFrameW(), getFrameH());

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
