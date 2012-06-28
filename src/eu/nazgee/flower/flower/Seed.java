package eu.nazgee.flower.flower;

import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.util.adt.color.Color;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import eu.nazgee.flower.Consts;
import eu.nazgee.game.utils.loadable.LoadableResourceSimple;

public enum Seed {
	// ===========================================================
	// Constants
	// ===========================================================
	/*
	 * When defined as Enum, seeds are singletons
	 */
	SEED1(1,	100,	0,	0,	true,	Color.WHITE, Color.RED),
	SEED2(2,	100,	1,	0,	true, 	Color.WHITE),
	SEED3(3,	100,	2,	0,	true, 	Color.WHITE),
	SEED4(4,	200,	3,	0,	true, 	Color.WHITE),
	SEED5(5,	200,	4,	0,	true, 	Color.WHITE),
	SEED6(6,	200,	5,	0,	true, 	Color.WHITE),
	SEED7(7,	300,	6,	0,	true, 	Color.WHITE),
	SEED8(8,	300,	7,	0,	true, 	Color.WHITE),
	SEED9(9,	300,	8,	0,	true, 	Color.WHITE),
	SEED10(10,	500,	9,	0,	true, 	Color.WHITE),
	SEED11(11,	500,	10,	0,	true, 	Color.WHITE),
	SEED12(12,	500,	11,	0,	true, 	Color.WHITE),
	SEED13(13,	800,	12,	0,	true, 	Color.WHITE),
	SEED14(14,	800,	13,	0,	true, 	Color.WHITE),
	SEED15(15,	800,	14,	0,	true, 	Color.WHITE),
	SEED16(16,	1300,	15,	0,	true, 	Color.WHITE),
	SEED17(17,	1300,	15,	0,	true, 	Color.WHITE),
	SEED18(18,	1300,	15,	0,	true, 	Color.WHITE),
	SEED19(19,	2100,	15,	0,	true, 	Color.WHITE),
	SEED20(20,	2100,	15,	0,	true, 	Color.WHITE),
	SEED21(21,	2100,	15,	0,	true, 	Color.WHITE),
	SEED22(22,	3400,	15,	0,	true, 	Color.WHITE),
	SEED23(23,	3400,	15,	0,	true, 	Color.WHITE),
	SEED24(24,	3400,	15,	0,	true, 	Color.WHITE);

	public static final int SEEDS_NUMBER = SEED24.id;

	// ===========================================================
	// Fields
	// ===========================================================
	public final SeedResourcesBasic resources = new SeedResourcesBasic();
	public final boolean lockedByDefault;
	public final int id;
	public final int cost;
	public final int seedID;
	public final int blossomID;
	public final Color[] col_plant;

	// ===========================================================
	// Constructors
	// ===========================================================
	private Seed(final int pID, final int pCost, final int pBlossomID, final int pSeedID) {
		this(pID, pCost, pBlossomID, pSeedID, true);
	}

	private Seed(final int pID, final int pCost, final int pBlossomID, final int pSeedID, final boolean pLockedByDefault) {
		this(pID, pCost, pBlossomID, pSeedID, pLockedByDefault, Color.WHITE);
	}

	private Seed(final int pID, final int pCost, final int pBlossomID, final int pSeedID, final boolean pLockedByDefault, final Color ... pColors) {
		id = pID;
		cost = pCost;
		seedID = pSeedID;
		blossomID = pBlossomID;
		lockedByDefault = pLockedByDefault;
		col_plant = pColors;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public Color getRandomColor(final Random pRandom) {
		return col_plant[pRandom.nextInt(col_plant.length)];
	}

	public static Seed getSeedById(final int pID) {
		switch (pID) {
		case 1:
			return SEED1;
		case 2:
			return SEED2;
		case 3:
			return SEED3;
		case 4:
			return SEED4;
		case 5:
			return SEED5;
		case 6:
			return SEED6;
		case 7:
			return SEED7;
		case 8:
			return SEED8;
		case 9:
			return SEED9;
		case 10:
			return SEED10;
		case 11:
			return SEED11;
		case 12:
			return SEED12;
		case 13:
			return SEED13;
		case 14:
			return SEED14;
		case 15:
			return SEED15;
		case 16:
			return SEED16;
		case 17:
			return SEED17;
		case 18:
			return SEED18;
		case 19:
			return SEED19;
		case 20:
			return SEED20;
		case 21:
			return SEED21;
		case 22:
			return SEED22;
		case 23:
			return SEED23;
		case 24:
			return SEED24;
		default:
			throw new RuntimeException("No such seed as " + pID + "!");
		}
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public boolean isLocked() {
		return resources.isLocked();
	}

	/**
	 * Unlocks given seed if it was locked, does nothing it already was unlocked
	 * @param c
	 * @return true if seed was just unlocked; false if seed was already unlocked
	 */
	public boolean unlock(final Context c) {
		if (isLocked()) {
			resources.setLocked(c, false);
			return true;
		}
		return false;
	}

	private String getSeedPrefix() {
		return "seed" + id;
	}
	public String getKeyLocked() {
		return getSeedPrefix() + "locked";
	}
	public String getKeyPlanted() {
		return getSeedPrefix() + "planted";
	}
	public String getKeyHarvested() {
		return getSeedPrefix() + "harvested";
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class SeedResourcesBasic extends LoadableResourceSimple {

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================
		private boolean mLocked;
		private int mPlanted;
		private int mHarvested;
		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================
		public boolean isLocked() {
			return mLocked;
		}
		public int getPlanted() {
			return mPlanted;
		}
		public int getHarvested() {
			return mHarvested;
		}
		public void setLocked(final Context c, final boolean pLocked) {
			final Editor editor = getEditor(c);
			editor.putBoolean(getKeyLocked(), pLocked);
			editor.commit();
			mLocked = pLocked;
		}
		public void setScore(final Context c, final int pScore) {
			final Editor editor = getEditor(c);
			editor.putInt(getKeyPlanted(), pScore);
			editor.commit();
		}
		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================
		@Override
		public void onLoadResources(final Engine e, final Context c) {
		}

		@Override
		public void onLoad(final Engine e, final Context c) {
			final SharedPreferences prefs = c.getSharedPreferences(Consts.SEEDS_PREFS_FILE, 0);
			mLocked = prefs.getBoolean(getKeyLocked(), lockedByDefault);
			mPlanted = prefs.getInt(getKeyPlanted(), 0);
			mHarvested = prefs.getInt(getKeyHarvested(), 0);
		}

		@Override
		public void onUnload() {
		}
		// ===========================================================
		// Methods
		// ===========================================================
		private Editor getEditor(final Context c) {
			 return c.getSharedPreferences(Consts.SEEDS_PREFS_FILE, 0).edit();
		}
		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
