package eu.nazgee.flower.seed;

import java.util.List;

import org.andengine.engine.Engine;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.color.Color;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import eu.nazgee.flower.Consts;
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;

public enum Seed {
	// ===========================================================
	// Constants
	// ===========================================================
	/*
	 * When defined as Enum, seeds are singletons 
	 */
	SEED1(1,	100,	"flowers/001.svg",	"seeds/001.png",	false,	Color.RED),
	SEED2(2,	100,	"flowers/001.svg",	"seeds/001.png",	false, 	Color.YELLOW),
	SEED3(3,	100,	"flowers/001.svg",	"seeds/001.png",	false, 	Color.BLUE),
	SEED4(4,	200,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED, Color.YELLOW),
	SEED5(5,	200,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.BLUE, Color.YELLOW),
	SEED6(6,	200,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED, Color.BLUE),
	SEED7(7,	300,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED, Color.YELLOW, Color.BLUE),
	SEED8(8,	300,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED, Color.YELLOW, Color.BLUE),
	SEED9(9,	300,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED, Color.YELLOW, Color.BLUE),
	SEED10(10,	500,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED),
	SEED11(11,	500,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED),
	SEED12(12,	500,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED),
	SEED13(13,	800,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED),
	SEED14(14,	800,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED),
	SEED15(15,	800,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED),
	SEED16(16,	1300,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED),
	SEED17(17,	1300,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED),
	SEED18(18,	1300,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED),
	SEED19(19,	2100,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED),
	SEED20(20,	2100,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED),
	SEED21(21,	2100,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED),
	SEED22(22,	3400,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED),
	SEED23(23,	3400,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED),
	SEED24(24,	3400,	"flowers/001.svg",	"seeds/001.png",	true, 	Color.RED);

	public static final int SEEDS_NUMBER = SEED24.id;

	// ===========================================================
	// Fields
	// ===========================================================
	public final SeedResourcesBasic resources = new SeedResourcesBasic();
	public final boolean lockedByDefault;
	public final int id;
	public final int cost;
	public final CharSequence tex_seed_file;
	public final CharSequence tex_plant_file;
	public final Color[] col_plant;
	public ITextureRegion mTexSeed;
	public ITextureRegion mTexPlant;

	// ===========================================================
	// Constructors
	// ===========================================================
	private Seed(final int pID, final int pCost, final CharSequence pTexSeed, final CharSequence pTexPlant) {
		this(pID, pCost, pTexSeed, pTexPlant, true);
	}

	private Seed(final int pID, final int pCost, final CharSequence pTexSeed, final CharSequence pTexPlant, boolean pLockedByDefault) {
		this(pID, pCost, pTexSeed, pTexPlant, pLockedByDefault, Color.WHITE);
	}

	private Seed(final int pID, final int pCost, final CharSequence pTexSeed_file, final CharSequence pTexPlant_file, boolean pLockedByDefault, Color ... pColors) {
		id = pID;
		cost = pCost;
		tex_seed_file = pTexPlant_file;
		tex_plant_file = pTexSeed_file;
		lockedByDefault = pLockedByDefault;
		col_plant = pColors;
	}

	public static void createSeedAssets(BuildableBitmapTextureAtlas pAtlas,  Context pContext, List<Seed> pSeeds) {
		for (Seed seed : pSeeds) {
			seed.mTexSeed = BitmapTextureAtlasTextureRegionFactory.createFromAsset(pAtlas, pContext, seed.tex_seed_file.toString());
//			if (seed.mTexSeed == null) {
//				throw new RuntimeException("wtf? " + seed.tex_seed_file);
//			} else {
//				Log.w("bmp!", seed.tex_seed_file.toString());
//			}
		}
	}

	public static void createPlantAssets(BuildableBitmapTextureAtlas pAtlas,  Context pContext, List<Seed> pSeeds) {
		for (Seed seed : pSeeds) {
			seed.mTexPlant = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(pAtlas, pContext, seed.tex_plant_file.toString(), Consts.FLOWER_TEX_WIDTH, Consts.FLOWER_TEX_HEIGHT);
//			if (seed.mTexPlant == null) {
//				throw new RuntimeException("wtf? " + seed.tex_plant_file);
//			} else {
//				Log.w("svg!", seed.tex_plant_file.toString());
//			}
		}
	}

	/**
	 * Binds appropriate textures to seeds
	 * @param pAtlasSeeds
	 * @param pAtlasPlants
	 * @param pAssetManager
	 * @param pSeeds
	 */
	public static void createAllAssets(BuildableBitmapTextureAtlas pAtlasSeeds,
			BuildableBitmapTextureAtlas pAtlasPlants,
			Context pContext, List<Seed> pSeeds) {
		createSeedAssets(pAtlasSeeds, pContext, pSeeds);
		createPlantAssets(pAtlasPlants, pContext, pSeeds);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
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
	private String getLevelPrefix() {
		return "seed" + id;
	}
	public String getKeyLocked() {
		return getLevelPrefix() + "locked";
	}
	public String getKeyPlanted() {
		return getLevelPrefix() + "planted";
	}
	public String getKeyHarvested() {
		return getLevelPrefix() + "harvested";
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public class SeedResourcesBasic extends SimpleLoadableResource {

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
		public void setLocked(Context c, boolean pLocked) {
			Editor editor = getEditor(c);
			editor.putBoolean(getKeyLocked(), pLocked);
			editor.commit();
		}
		public void setScore(Context c, int pScore) {
			Editor editor = getEditor(c);
			editor.putInt(getKeyPlanted(), pScore);
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
			SharedPreferences prefs = c.getSharedPreferences(Consts.SEEDS_PREFS_FILE, 0);
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
		private Editor getEditor(Context c) {
			 return c.getSharedPreferences(Consts.SEEDS_PREFS_FILE, 0).edit();
		}
		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
