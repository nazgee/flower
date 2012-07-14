package eu.nazgee.flower.activity.game.scene.shop;

import org.andengine.util.adt.list.SmartList;

import eu.nazgee.flower.flower.LoadableSeed;
import eu.nazgee.flower.level.GameLevel;

public class SeedsShop {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final SmartList<LoadableSeed> mSeedsInShop;
	private final SmartList<LoadableSeed> mSeedsInBasket = new SmartList<LoadableSeed>();
	private int mCustomerCash;
	private int mBasketValue;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SeedsShop(final GameLevel pGameLevel) {
		this(pGameLevel.getSeedsAccumulatedSoFar(), pGameLevel.cash);
	}

	public SeedsShop(final SmartList<LoadableSeed> mSeeds, final int mCustomerCash) {
		super();
		this.mSeedsInShop = mSeeds;
		this.setCustomerCash(mCustomerCash);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public int getCustomerCash() {
		return mCustomerCash;
	}
	public void setCustomerCash(final int mCustomerCash) {
		this.mCustomerCash = mCustomerCash;
	}
	public SmartList<LoadableSeed> getSeedsInShop() {
		return mSeedsInShop;
	}
	public SmartList<LoadableSeed> getSeedsInBasket() {
		return mSeedsInBasket;
	}
	public int getBasketValue() {
		return mBasketValue;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean addToBasket(final LoadableSeed pSeed) {
		if (mCustomerCash >= (mBasketValue + pSeed.cost)) {
			mSeedsInBasket.add(pSeed);
			recalculateBasketValue();
			return true;
		} else {
			return false;
		}
	}

	public boolean removeFromBasket(final LoadableSeed pSeed) {
		if (mSeedsInBasket.remove(pSeed)) {
			recalculateBasketValue();
			return true;
		} else {
			return false;
		}
	}

	public void emptyBasket() {
		mSeedsInBasket.clear();
		recalculateBasketValue();
	}

	protected void recalculateBasketValue() {
		mBasketValue = calculateValue(mSeedsInBasket);
	}

	protected int calculateValue(final SmartList<LoadableSeed> pSeeds) {
		int value = 0;
		for (final LoadableSeed seed : pSeeds) {
			value += seed.cost;
		}
		return value;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================


}
