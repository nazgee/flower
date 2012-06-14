package eu.nazgee.flower;

import org.andengine.engine.Engine;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePack;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackLoader;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackTextureRegionLibrary;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception.TexturePackParseException;
import org.andengine.util.debug.Debug;

import android.content.Context;

import eu.nazgee.game.utils.loadable.LoadableResourceSimple;

public class TexturesLibrary extends LoadableResourceSimple{
	final boolean mLoadMain;
	private TexturePackTextureRegionLibrary mSpritesheetMain;

	public TexturesLibrary(boolean pLoadMain) {
		mLoadMain = pLoadMain;
	}

	@Override
	public void onLoadResources(Engine e, Context c) {
		// Load spritesheets

		if (mLoadMain) {
			try {
				final TexturePack spritesheetTexturePack = new TexturePackLoader(e.getTextureManager(), "gfx/spritesheets/").loadFromAsset(c.getAssets(), "main.xml");
				spritesheetTexturePack.loadTexture();
				this.mSpritesheetMain = spritesheetTexturePack.getTexturePackTextureRegionLibrary();
			} catch (final TexturePackParseException ex) {
				Debug.e(ex);
			}
		}
	}

	@Override
	public void onLoad(Engine e, Context c) {
	}

	@Override
	public void onUnload() {
	}

	public TexturePackTextureRegionLibrary getMain() {
		return mSpritesheetMain;
	}

	public static final int MAIN_AMBIENT_CLOUD_CL03_ID = 0;
	public static final int MAIN_AMBIENT_CLOUD_CL03A_ID = 1;
	public static final int MAIN_AMBIENT_CLOUD_CL10_ID = 2;
	public static final int MAIN_AMBIENT_RAINDROP_DROP1_ID = 3;
	public static final int MAIN_AMBIENT_RAINSPLASH_SPLASH1_ID = 4;
	public static final int MAIN_AMBIENT_RAINSPLASH_SPLASH2_ID = 5;
	public static final int MAIN_AMBIENT_RAINSPLASH_SPLASH3_ID = 6;
	public static final int MAIN_AMBIENT_SKY_SKY1_ID = 7;
	public static final int MAIN_FLOWERS_FLOWER0001_ID = 8;
	public static final int MAIN_FLOWERS_FLOWER0002_ID = 9;
	public static final int MAIN_FLOWERS_FLOWER0003_ID = 10;
	public static final int MAIN_FLOWERS_FLOWER0004_ID = 11;
	public static final int MAIN_FLOWERS_FLOWER0005_ID = 12;
	public static final int MAIN_FLOWERS_FLOWER0006_ID = 13;
	public static final int MAIN_FLOWERS_FLOWER0007_ID = 14;
	public static final int MAIN_FLOWERS_FLOWER0008_ID = 15;
	public static final int MAIN_FLOWERS_FLOWER0009_ID = 16;
	public static final int MAIN_FLOWERS_FLOWER0010_ID = 17;
	public static final int MAIN_FLOWERS_FLOWER0011_ID = 18;
	public static final int MAIN_FLOWERS_FLOWER0012_ID = 19;
	public static final int MAIN_FLOWERS_FLOWER0013_ID = 20;
	public static final int MAIN_FLOWERS_FLOWER0014_ID = 21;
	public static final int MAIN_FLOWERS_FLOWER0015_ID = 22;
	public static final int MAIN_FLOWERS_FLOWER0016_ID = 23;
	public static final int MAIN_FRAME_LEVEL_COMPLETE_ID = 24;
	public static final int MAIN_FRAME_LEVEL_ID = 25;
	public static final int MAIN_FRAME_SEED_ID = 26;
	public static final int MAIN_ICONS_ADD_ID = 27;
	public static final int MAIN_ICONS_BACK_ID = 28;
	public static final int MAIN_ICONS_BRIEFCASE_ID = 29;
	public static final int MAIN_ICONS_CASH_ID = 30;
	public static final int MAIN_ICONS_CHECK_MARK_ID = 31;
	public static final int MAIN_ICONS_CLOCK_ID = 32;
	public static final int MAIN_ICONS_COMMENT_ID = 33;
	public static final int MAIN_ICONS_DELETE_ID = 34;
	public static final int MAIN_ICONS_DOWN_ID = 35;
	public static final int MAIN_ICONS_FLOWERS_ID = 36;
	public static final int MAIN_ICONS_HOME_ID = 37;
	public static final int MAIN_ICONS_INFO_ID = 38;
	public static final int MAIN_ICONS_LOCK_ID = 39;
	public static final int MAIN_ICONS_MUSIC_ID = 40;
	public static final int MAIN_ICONS_NEW_ID = 41;
	public static final int MAIN_ICONS_NEXT_ID = 42;
	public static final int MAIN_ICONS_NOTIFICATION_ID = 43;
	public static final int MAIN_ICONS_QUESTION_ID = 44;
	public static final int MAIN_ICONS_REFRESH_ID = 45;
	public static final int MAIN_ICONS_SALE_ID = 46;
	public static final int MAIN_ICONS_SETTINGS_ID = 47;
	public static final int MAIN_ICONS_SHOP_ID = 48;
	public static final int MAIN_ICONS_STAR_ID = 49;
	public static final int MAIN_ICONS_STATS_ID = 50;
	public static final int MAIN_ICONS_UNLOCK_ID = 51;
	public static final int MAIN_ICONS_UP_ID = 52;
	public static final int MAIN_ICONS_WARNING_ID = 53;
	public static final int MAIN_PARALAX_BACK1_ID = 54;
	public static final int MAIN_PARALAX_BACK2_ID = 55;
	public static final int MAIN_PARALAX_FRONT1_ID = 56;
	public static final int MAIN_PARALAX_GROUND_ID = 57;
	public static final int MAIN_POT_POT_ID = 58;
	public static final int MAIN_POT_WATER_ID = 59;
	public static final int MAIN_SEEDS_001_ID = 60;
	public static final int MAIN_SUN_RAY_TAIL_ID = 61;
	public static final int MAIN_SUN_RAY_ID = 62;
	public static final int MAIN_SUN_SUN_ID = 63;
}
