package eu.nazgee.flower.level;

import org.andengine.engine.Engine;
import org.andengine.util.adt.list.SmartList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import eu.nazgee.flower.Consts;
import eu.nazgee.flower.flower.Seed;
import eu.nazgee.game.utils.loadable.LoadableResourceSimple;

public enum GameLevel {
	// ===========================================================
	// Constants
	// ===========================================================
	/*
	 * When defined as Enum, levels are singletons 
	 */
	LEVEL1(1, 1000, false, Seed.SEED1, Seed.SEED2, Seed.SEED3, Seed.SEED14, Seed.SEED15, Seed.SEED16, Seed.SEED17, Seed.SEED18, Seed.SEED19),
//	LEVEL1(1, 1000, false, Seed.SEED1, Seed.SEED2, Seed.SEED3),
	LEVEL2(2, 1000),
	LEVEL3(3, 1000),
	LEVEL4(4, 1000, Seed.SEED4),
	LEVEL5(5, 1000, Seed.SEED5),
	LEVEL6(6, 1000, Seed.SEED6),
	LEVEL7(7, 1000, Seed.SEED7, Seed.SEED8, Seed.SEED9),
	LEVEL8(8, 1000),
	LEVEL9(9, 1000),
	LEVEL10(10, 1000, Seed.SEED10, Seed.SEED11),
	LEVEL11(11, 1000),
	LEVEL12(12, 1000, Seed.SEED12, Seed.SEED13),
	LEVEL13(13, 1000),
	LEVEL14(14, 1000),
	LEVEL15(15, 1000),
	LEVEL16(16, 1000),
	LEVEL17(17, 1000),
	LEVEL18(18, 1000),
	LEVEL19(19, 1000),
	LEVEL20(20, 1000),
	LEVEL21(21, 1000),
	LEVEL22(22, 1000),
	LEVEL23(23, 1000),
	LEVEL24(24, 1000);

	public static final int LEVELS_NUMBER = LEVEL24.id;

	// ===========================================================
	// Fields
	// ===========================================================
	public final int id;
	public final int cash;
	public final boolean lockedByDefault;
	public final LevelResourcesBasic resources;
	public final Seed[] seeds;

	// ===========================================================
	// Constructors
	// ===========================================================
	private GameLevel(final int pID, final int pCash) {
		this(pID, pCash, true);
	}

	private GameLevel(final int pID, final int pCash, Seed ... pSeeds) {
		this(pID, pCash, true, pSeeds);
	}

	private GameLevel(final int pID, final int pCash, boolean pLocked, Seed ... pSeeds) {
		id = pID;
		cash = pCash;
		lockedByDefault = pLocked;
		resources = new LevelResourcesBasic();
		seeds = pSeeds;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public SmartList<Seed> getSeeds() {
		SmartList<Seed> ret = new SmartList<Seed>(id);

		// Current level has it's own collection of seeds, plus seeds from all
		// previous levels- this builds up a collection that is returned here
		for (int i = 1; i <= id; i++) {
			Seed[] new_seeds = getLevelById(i).seeds;
			for (Seed seed : new_seeds) {
				ret.add(seed);
			}
		}

		return ret;
	}

	public static GameLevel getLevelById(final int pID) {
		switch (pID) {
		case 1:
			return LEVEL1;
		case 2:
			return LEVEL2;
		case 3:
			return LEVEL3;
		case 4:
			return LEVEL4;
		case 5:
			return LEVEL5;
		case 6:
			return LEVEL6;
		case 7:
			return LEVEL7;
		case 8:
			return LEVEL8;
		case 9:
			return LEVEL9;
		case 10:
			return LEVEL10;
		case 11:
			return LEVEL11;
		case 12:
			return LEVEL12;
		case 13:
			return LEVEL13;
		case 14:
			return LEVEL14;
		case 15:
			return LEVEL15;
		case 16:
			return LEVEL16;
		case 17:
			return LEVEL17;
		case 18:
			return LEVEL18;
		case 19:
			return LEVEL19;
		case 20:
			return LEVEL20;
		case 21:
			return LEVEL21;
		case 22:
			return LEVEL22;
		case 23:
			return LEVEL23;
		case 24:
			return LEVEL24;
		default:
			throw new RuntimeException("No such level as " + pID + "!");
		}
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	private String getLevelPrefix() {
		return "level" + id;
	}
	public String getKeyLocked() {
		return getLevelPrefix() + "locked";
	}
	public String getKeyScore() {
		return getLevelPrefix() + "score";
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public class LevelResourcesBasic extends LoadableResourceSimple {

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================
		private boolean mLocked;
		private int mScore;
		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================
		public boolean isLocked() {
			return mLocked;
		}
		public int getScore() {
			return mScore;
		}
		public void setLocked(Context c, boolean pLocked) {
			Editor editor = getEditor(c);
			editor.putBoolean(getKeyLocked(), pLocked);
			editor.commit();
		}
		public void setScore(Context c, int pScore) {
			Editor editor = getEditor(c);
			editor.putInt(getKeyScore(), pScore);
			editor.commit();
		}
		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================
		@Override
		public void onLoadResources(Engine e, Context c) {
		}

		@Override
		public void onLoad(Engine e, Context c) {
			SharedPreferences prefs = c.getSharedPreferences(Consts.LEVEL_PREFS_FILE, 0);
			mLocked = prefs.getBoolean(getKeyLocked(), lockedByDefault);
			mScore = prefs.getInt(getKeyScore(), 0);
		}

		@Override
		public void onUnload() {
		}
		// ===========================================================
		// Methods
		// ===========================================================
		private Editor getEditor(Context c) {
			 return c.getSharedPreferences(Consts.LEVEL_PREFS_FILE, 0).edit();
		}
		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
