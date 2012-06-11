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

	public interface TexturesMain
	{
		public static final int FLOWERS_FLOWER0001_ID = 0;
		public static final int FLOWERS_FLOWER0002_ID = 1;
		public static final int FLOWERS_FLOWER0003_ID = 2;
		public static final int FLOWERS_FLOWER0004_ID = 3;
		public static final int FLOWERS_FLOWER0005_ID = 4;
		public static final int FLOWERS_FLOWER0006_ID = 5;
		public static final int FLOWERS_FLOWER0007_ID = 6;
		public static final int FLOWERS_FLOWER0008_ID = 7;
		public static final int FLOWERS_FLOWER0009_ID = 8;
		public static final int FLOWERS_FLOWER0010_ID = 9;
		public static final int FLOWERS_FLOWER0011_ID = 10;
		public static final int FLOWERS_FLOWER0012_ID = 11;
		public static final int FLOWERS_FLOWER0013_ID = 12;
		public static final int FLOWERS_FLOWER0014_ID = 13;
		public static final int FLOWERS_FLOWER0015_ID = 14;
		public static final int FLOWERS_FLOWER0016_ID = 15;
		public static final int ICONS_ADD_ID = 16;
		public static final int ICONS_BACK_ID = 17;
		public static final int ICONS_BRIEFCASE_ID = 18;
		public static final int ICONS_CASH_ID = 19;
		public static final int ICONS_CHECK_MARK_ID = 20;
		public static final int ICONS_CLOCK_ID = 21;
		public static final int ICONS_COMMENT_ID = 22;
		public static final int ICONS_DELETE_ID = 23;
		public static final int ICONS_DOWN_ID = 24;
		public static final int ICONS_FLOWERS_ID = 25;
		public static final int ICONS_HOME_ID = 26;
		public static final int ICONS_INFO_ID = 27;
		public static final int ICONS_LOCK_ID = 28;
		public static final int ICONS_MUSIC_ID = 29;
		public static final int ICONS_NEW_ID = 30;
		public static final int ICONS_NEXT_ID = 31;
		public static final int ICONS_NOTIFICATION_ID = 32;
		public static final int ICONS_QUESTION_ID = 33;
		public static final int ICONS_REFRESH_ID = 34;
		public static final int ICONS_SALE_ID = 35;
		public static final int ICONS_SETTINGS_ID = 36;
		public static final int ICONS_SHOP_ID = 37;
		public static final int ICONS_STAR_ID = 38;
		public static final int ICONS_STATS_ID = 39;
		public static final int ICONS_UNLOCK_ID = 40;
		public static final int ICONS_UP_ID = 41;
		public static final int ICONS_WARNING_ID = 42;
		public static final int SEEDS_001_ID = 43;
	}
}
