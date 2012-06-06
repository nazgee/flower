package eu.nazgee.flower.activity.game.scene.shop;

import org.andengine.util.adt.list.SmartList;

import eu.nazgee.flower.level.GameLevel;
import eu.nazgee.flower.seed.Seed;

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
	public SeedsShop(GameLevel pGameLevel) {
		this(pGameLevel.getSeeds(), pGameLevel.cash);
	}

	public SeedsShop(SmartList<Seed> mSeeds, int mCustomerCash) {
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
	public void setCustomerCash(int mCustomerCash) {
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

	public boolean addToBasket(Seed pSeed) {
		if (mCustomerCash >= (mBasketValue + pSeed.cost)) {
			mSeedsInBasket.add(pSeed);
			recalculateBasketValue();
			return true;
		} else {
			return false;
		}
	}

	public boolean removeFromBasket(Seed pSeed) {
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

	protected int calculateValue(SmartList<Seed> pSeeds) {
		int value = 0;
		for (Seed seed : pSeeds) {
			value += seed.cost;
		}
		return value;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================


}
