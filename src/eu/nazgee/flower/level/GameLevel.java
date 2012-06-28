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

	LEVEL1(1, 1000, 60, false, new Basket<Seed>().add(Seed.SEED1, 3).add(Seed.SEED2, 3).add(Seed.SEED3, 3)),
	LEVEL2(2, new Basket<Seed>().add(Seed.SEED4)),
	LEVEL3(3, new Basket<Seed>().add(Seed.SEED5)),
	LEVEL4(4, new Basket<Seed>().add(Seed.SEED6)),
	LEVEL5(5, new Basket<Seed>().add(Seed.SEED7)),
	LEVEL6(6, new Basket<Seed>().add(Seed.SEED8)),
	LEVEL7(7, new Basket<Seed>().add(Seed.SEED9).add(Seed.SEED10)),
	LEVEL8(8),
	LEVEL9(9),
	LEVEL10(10),
	LEVEL11(11),
	LEVEL12(12),
	LEVEL13(13),
	LEVEL14(14),
	LEVEL15(15),
	LEVEL16(16),
	LEVEL17(17),
	LEVEL18(18),
	LEVEL19(19),
	LEVEL20(20),
	LEVEL21(21),
	LEVEL22(22),
	LEVEL23(23),
	LEVEL24(24);

	public static final int LEVELS_NUMBER = LEVEL24.id;

	protected static final int CASH_DEFAULT = 1000;
	protected static final int DAYLIGHT_TIME_DEFAULT = 60;
	protected static final float LEVEL_WIDTH_DEFAULT = Consts.CAMERA_WIDTH;

	// ===========================================================
	// Fields
	// ===========================================================
	public final int daylight_time;
	public final float level_width = LEVEL_WIDTH_DEFAULT;
	public final int id;
	public final int cash;
	public final boolean lockedByDefault;
	public final LevelResourcesBasic resources;
	public final Basket<Seed> seeds;


	// ===========================================================
	// Constructors
	// ===========================================================
	private GameLevel(final int pID, final int pCash) {
		this(pID, pCash, DAYLIGHT_TIME_DEFAULT, true, new Basket<Seed>());
	}

	private GameLevel(final int pID) {
		this(pID, new Basket<Seed>());
	}

	private GameLevel(final int pID, final Basket<Seed> pSeeds) {
		this(pID, CASH_DEFAULT, DAYLIGHT_TIME_DEFAULT, true, pSeeds);
	}

	private GameLevel(final int pID, final int pCash, final int pDalightTime, final Basket<Seed> pSeeds) {
		this(pID, pCash, pDalightTime, true, pSeeds);
	}

	private GameLevel(final int pID, final int pCash, final int pDalightTime, final boolean pLocked, final Basket<Seed> pSeeds) {
		id = pID;
		cash = pCash;
		daylight_time = pDalightTime;
		lockedByDefault = pLocked;
		resources = new LevelResourcesBasic();
		seeds = pSeeds;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public SmartList<Seed> getSeedsAccumulatedSoFar() {
		final SmartList<Seed> ret = new SmartList<Seed>(id);

		// Current level has it's own collection of seeds, plus seeds from all
		// previous levels- this builds up a collection that is returned here
		for (int i = 1; i <= id; i++) {
			final SmartList<Seed> new_seeds = getLevelById(i).seeds.getItems();
			for (final Seed seed : new_seeds) {
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
		public void setLocked(final Context c, final boolean pLocked) {
			mLocked = pLocked;
			final Editor editor = getEditor(c);
			editor.putBoolean(getKeyLocked(), pLocked);
			editor.commit();
		}
		public void setScore(final Context c, final int pScore) {
			mScore = pScore;
			final Editor editor = getEditor(c);
			editor.putInt(getKeyScore(), pScore);
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
			final SharedPreferences prefs = c.getSharedPreferences(Consts.LEVEL_PREFS_FILE, 0);
			mLocked = prefs.getBoolean(getKeyLocked(), lockedByDefault);
			mScore = prefs.getInt(getKeyScore(), 0);
		}

		@Override
		public void onUnload() {
		}
		// ===========================================================
		// Methods
		// ===========================================================
		private Editor getEditor(final Context c) {
			 return c.getSharedPreferences(Consts.LEVEL_PREFS_FILE, 0).edit();
		}
		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
