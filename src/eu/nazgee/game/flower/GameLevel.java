package eu.nazgee.game.flower;

import org.andengine.engine.Engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;

public enum GameLevel {
	// ===========================================================
	// Constants
	// ===========================================================
	/*
	 * When defined as Enum, levels are singletons 
	 */
	LEVEL1(1, 1000, false),
	LEVEL2(2, 1000),
	LEVEL3(3, 1000),
	LEVEL4(4, 1000),
	LEVEL5(5, 1000),
	LEVEL6(6, 1000),
	LEVEL7(7, 1000),
	LEVEL8(8, 1000),
	LEVEL9(9, 1000),
	LEVEL10(10, 1000),
	LEVEL11(11, 1000),
	LEVEL12(12, 1000),
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

	// ===========================================================
	// Fields
	// ===========================================================
	public final int id;
	public final int cash;
	public final boolean lockedByDefault;
	public final LevelResourcesBasic resources;

	// ===========================================================
	// Constructors
	// ===========================================================
	private GameLevel(final int pID, final int pCash) {
		this(pID, pCash, true);
	}

	private GameLevel(final int pID, final int pCash, boolean pLocked) {
		id = pID;
		cash = pCash;
		resources = new LevelResourcesBasic();
		lockedByDefault = pLocked;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
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
			return LEVEL1;
		case 12:
			return LEVEL2;
		case 13:
			return LEVEL3;
		case 14:
			return LEVEL4;
		case 15:
			return LEVEL5;
		case 16:
			return LEVEL6;
		case 17:
			return LEVEL7;
		case 18:
			return LEVEL8;
		case 19:
			return LEVEL9;
		case 20:
			return LEVEL10;
		case 21:
			return LEVEL1;
		case 22:
			return LEVEL2;
		case 23:
			return LEVEL3;
		case 24:
			return LEVEL4;
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
	
	public class LevelResourcesBasic extends SimpleLoadableResource {

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
