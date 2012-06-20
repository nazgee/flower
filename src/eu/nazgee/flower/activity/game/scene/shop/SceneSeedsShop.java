package eu.nazgee.flower.activity.game.scene.shop;

import java.util.List;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;

import android.content.Context;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.activity.game.scene.game.GameBackground;
import eu.nazgee.flower.base.pagerscene.ArrayLayout;
import eu.nazgee.flower.base.pagerscene.IPage;
import eu.nazgee.flower.base.pagerscene.PageMoverCameraZoom;
import eu.nazgee.flower.base.pagerscene.PageRectangleTransparent;
import eu.nazgee.flower.base.pagerscene.ScenePager;
import eu.nazgee.flower.flower.Seed;
import eu.nazgee.flower.level.GameLevel;
import eu.nazgee.flower.pool.popup.PopupPool;
import eu.nazgee.flower.pool.popup.PopupPool.PopupItem;
import eu.nazgee.util.LayoutBase.eAnchorPointXY;

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
	private final HudShop mHUD;
	private final Font mDescFont;
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;
	private PopupPool mPopupPool;

	private GameBackground mBG;
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

		// Install shop's background
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
			getLoader().install(seed.resources);
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
		if (!pItem.getSeed().resources.isLocked()) {
			if (mShop.addToBasket(pItem.getSeed())) {
				updateHUDBasket();
				updateHUDCash();
				PopupItem popup = mPopupPool.obtainPoolItem();
				popup.getEntity().put(pItem, "$" + pItem.getSeed().cost);
				popup.getEntity().fxMoveTo(0.75f, mHUD.getTextCash());
				attachChild(popup.getEntity());

				pItem.animateYes();
			} else {
				pItem.animateNo();
			}
		} else {
			pItem.animateNo();
		}
		super.callClickListener(pItem);
	}

	@Override
	public void onLoadResources(Engine e, Context c) {
		super.onLoadResources(e, c);
		mBG = new GameBackground(e.getCamera(), mTexturesLibrary, getVertexBufferObjectManager());
	}

	@Override
	public void onLoad(Engine e, Context c) {
		super.onLoad(e, c);
		setPageMover(new PageMoverCameraZoom<SeedItem>(0.75f, (SmoothCamera) e.getCamera(), getW() * PAGE_WIDTH_EFFECTIVE));
		setBackground(mBG);

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
		SeedItem item = new SeedItem(seed, getFrameW(), getFrameH(), mDescFont, mTexturesLibrary.getFactory(), getVertexBufferObjectManager(), mTexturesLibrary);
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
}
