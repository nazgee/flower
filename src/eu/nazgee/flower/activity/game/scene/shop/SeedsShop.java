package eu.nazgee.flower.activity.game.scene.shop;

import org.andengine.util.adt.list.SmartList;

import eu.nazgee.flower.flower.Seed;
import eu.nazgee.flower.level.GameLevel;

public class SeedsShop {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final SmartList<Seed> mSeedsInShop;
	private final SmartList<Seed> mSeedsInBasket = new SmartList<Seed>();
	private int mCustomerCash;
	private int mBasketValue;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SeedsShop(final GameLevel pGameLevel) {
		this(pGameLevel.getSeedsAccumulatedSoFar(), pGameLevel.cash);
	}

	public SeedsShop(final SmartList<Seed> mSeeds, final int mCustomerCash) {
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
	public SmartList<Seed> getSeedsInShop() {
		return mSeedsInShop;
	}
	public SmartList<Seed> getSeedsInBasket() {
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

	public boolean addToBasket(final Seed pSeed) {
		if (mCustomerCash >= (mBasketValue + pSeed.cost)) {
			mSeedsInBasket.add(pSeed);
			recalculateBasketValue();
			return true;
		} else {
			return false;
		}
	}

	public boolean removeFromBasket(final Seed pSeed) {
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

	protected int calculateValue(final SmartList<Seed> pSeeds) {
		int value = 0;
		for (final Seed seed : pSeeds) {
			value += seed.cost;
		}
		return value;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================


}
