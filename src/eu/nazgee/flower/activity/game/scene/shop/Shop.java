package eu.nazgee.flower.activity.game.scene.shop;

import org.andengine.util.adt.list.SmartList;

import eu.nazgee.flower.level.GameLevel;
import eu.nazgee.flower.seed.Seed;

public class Shop {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final SmartList<Seed> mSeeds;
	private int mCustomerCash;
	// ===========================================================
	// Constructors
	// ===========================================================
	public Shop(GameLevel pGameLevel) {
		this(pGameLevel.getSeeds(), pGameLevel.cash);
	}

	public Shop(SmartList<Seed> mSeeds, int mCustomerCash) {
		super();
		this.mSeeds = mSeeds;
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
	public SmartList<Seed> getSeeds() {
		return mSeeds;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
