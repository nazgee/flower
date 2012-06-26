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
import eu.nazgee.flower.base.pagerscene.ScenePagerBasic;
import eu.nazgee.flower.flower.Seed;
import eu.nazgee.flower.level.GameLevel;
import eu.nazgee.flower.pool.popup.PopupPool;
import eu.nazgee.flower.pool.popup.PopupPool.PopupItem;

public class SceneSeedsShop extends ScenePagerBasic<SeedItem> {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final HudShop mHUD;
	private final Font mDescFont;
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;
	private final PopupPool mPopupPool;

	private GameBackground mBG;
	private IShoppingListener mShoppingListener;
	private final SeedsShop mShop;
	private final TexturesLibrary mTexturesLibrary;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneSeedsShop(final float W, final float H,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final GameLevel pGameLevel, final Font pDescFont,
			final EntityDetachRunnablePoolUpdateHandler pDetacher,
			final TexturesLibrary pTexturesLibrary) {
		super(W, H, pVertexBufferObjectManager, 2, 2, 0.66f, 0.33f);
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
		for (final Seed seed : this.mShop.getSeedsInShop()) {
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

	public void setShoppingListener(final IShoppingListener pShoppingListener) {
		this.mShoppingListener = pShoppingListener;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void callClickListener(final SeedItem pItem) {
		if (!pItem.getSeed().resources.isLocked()) {
			if (mShop.addToBasket(pItem.getSeed())) {
				updateHUDBasket();
				updateHUDCash();
				final PopupItem popup = mPopupPool.obtainPoolItem();
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
	public void onLoadResources(final Engine e, final Context c) {
		super.onLoadResources(e, c);
		mBG = new GameBackground(e.getCamera(), mTexturesLibrary, getVertexBufferObjectManager());
	}

	@Override
	public void onLoad(final Engine e, final Context c) {
		super.onLoad(e, c);
		setPageMover(new PageMoverCameraZoom<SeedItem>(0.75f, (SmoothCamera) e.getCamera(), getEffectiveWidth()));
		setBackground(mBG);

		e.getCamera().setHUD(mHUD);
		updateHUDBasket();
		updateHUDCash();

		// HUD buttons clicks
		this.mHUD.getButtonDone().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final ButtonSprite pButtonSprite, final float pTouchAreaLocalX,
					final float pTouchAreaLocalY) {
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
	protected SeedItem populateItem(final int pItem, final int pItemOnPage, final int pPage) {
		final Seed seed = this.mShop.getSeedsInShop().get(pItem);
		final SeedItem item = new SeedItem(seed, getFrameW(), getFrameH(), mDescFont, mTexturesLibrary.getFactory(), getVertexBufferObjectManager(), mTexturesLibrary);
		return item;
	}

	@Override
	protected IPage<SeedItem> populatePage(final int pPageNumber) {
		final IPage<SeedItem> page = new PageRectangleTransparent<SeedItem>(0, 0, getEffectiveWidth(), getH(),
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
