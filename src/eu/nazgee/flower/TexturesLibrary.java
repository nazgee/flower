package eu.nazgee.flower;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePack;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackLoader;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackerTextureRegion;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception.TexturePackParseException;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.level.constants.LevelConstants;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Log;
import eu.nazgee.game.utils.loadable.LoadableResourceSimple;

public class TexturesLibrary extends LoadableResourceSimple{
	// ===========================================================
	// Constants
	// ===========================================================

	public static String TAG_ATTRIBUTE_REGION_ID = "region_id";
	public static String TAG_ATTRIBUTE_TEXTURE_ID = "texture_id";

	public static final int BLOSSOMS_NUMBER = 15;

	private static int TEXTURES_FIRST_FLOWER = TexturesMisc.FLOWERS_FLOWER0001_ID;
	private static int TEXTURES_COUNT_FLOWER = 16;
	private static int TEXTURES_FIRST_SEED = TexturesMisc.SEEDS_001_ID;
	private static int TEXTURES_COUNT_SEED = 1;
	// ===========================================================
	// Fields
	// ===========================================================
	private Font mFontPopUp;
	private BitmapTextureAtlas mFontAtlas;

	private TexturePack mSpritesheetParalax;
	private TexturePack mSpritesheetMisc;
	private TexturePack mSpritesheetUi;

	private final EntitiesFactory mEntitiesFactory = new EntitiesFactory(this);
	// ===========================================================
	// Constructors
	// ===========================================================
	public TexturesLibrary() {
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public Font getFontPopUp() {
		return mFontPopUp;
	}

	public EntitiesFactory getFactory() {
		return mEntitiesFactory;
	}


	public ITextureRegion getTree(final int pTreeNumber) {
		return mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(TexturesMisc.AMBIENT_TREE_01_ID + pTreeNumber);
	}
	public ITextureRegion getFlower(final int pFlowerNumber) {
		return mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(TexturesMisc.FLOWERS_FLOWER0001_ID + pFlowerNumber);
	}
	public ITextureRegion getSeed(final int pSeedNumber) {
		return mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(TexturesMisc.SEEDS_001_ID + pSeedNumber);
	}

	public ITextureRegion getWateredMarker() {
		return mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(TexturesMisc.MARKER_WATERED_ID);
	}

	public ITextureRegion getRainbow() {
		return mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(TexturesMisc.AMBIENT_RAINBOW_ID);
	}
	public ITiledTextureRegion getButterfly() {
		final ITextureRegion regions[] = new ITextureRegion[8];
		for (int i = 0; i < regions.length; i++) {
			regions[i] = mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(TexturesMisc.AMBIENT_BUTTERFLY_01_ID + i);
		}
		return new TiledTextureRegion(mSpritesheetMisc.getTexture(), regions);
	}

	public ITextureRegion getSky() {
		return mSpritesheetParalax.getTexturePackTextureRegionLibrary().get(TexturesParalax.SKY_ID);
	}
	public ITextureRegion getParalaxBack1() {
		return mSpritesheetParalax.getTexturePackTextureRegionLibrary().get(TexturesParalax.BACK1_ID);
	}
	public ITextureRegion getParalaxBack2() {
		return mSpritesheetParalax.getTexturePackTextureRegionLibrary().get(TexturesParalax.BACK2_ID);
	}
	public ITextureRegion getParalaxBack3() {
		return mSpritesheetParalax.getTexturePackTextureRegionLibrary().get(TexturesParalax.BACK3_ID);
	}
	public ITextureRegion getParalaxBack4() {
		return mSpritesheetParalax.getTexturePackTextureRegionLibrary().get(TexturesParalax.BACK4_ID);
	}
	public ITextureRegion getParalaxGround() {
		return mSpritesheetParalax.getTexturePackTextureRegionLibrary().get(TexturesParalax.GROUND_ID);
	}

	public ITextureRegion getButton() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.NINE_PATCH_BUT3_ID);
	}

	public ITextureRegion getFrameTransparent() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.NINE_PATCH_GLASS_ID);
	}
	public ITextureRegion getFrameRed() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.NINE_PATCH_GLASS_RED_ID);
	}
	public ITextureRegion getFrameGreenDark() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.NINE_PATCH_GLASS_GREEN_DARK_ID);
	}
	public ITextureRegion getFrameGreen() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.NINE_PATCH_GLASS_GREEN_ID);
	}
	public ITextureRegion getFrameBlue() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.NINE_PATCH_GLASS_BLUE_ID);
	}
	public ITextureRegion getFrameGrat() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.NINE_PATCH_GLASS_GRAY_ID);
	}
	public ITextureRegion getFramePink() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.NINE_PATCH_GLASS_PINK_ID);
	}
	public ITextureRegion getFrameOrange() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.NINE_PATCH_GLASS_ORANGE_ID);
	}

	public ITextureRegion getIconLocked() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.ICONS_LOCK_ID);
	}
	public ITextureRegion getIconCheck() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.ICONS_CHECK_MARK_ID);
	}
	public ITextureRegion getIconCash() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.ICONS_CASH_ID);
	}
	public ITextureRegion getIconStar() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.ICONS_STAR_ID);
	}
	public ITextureRegion getIconNew() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.ICONS_NEW_ID);
	}
	public ITextureRegion getIconShop() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.ICONS_SHOP_ID);
	}
	public ITextureRegion getIconHome() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.ICONS_HOME_ID);
	}
	public ITextureRegion getIconRefresh() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.ICONS_REFRESH_ID);
	}

	public ITextureRegion getSun() {
		return mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(TexturesMisc.SUN_SUN_ID);
	}

	public ITiledTextureRegion getSunRays() {
		return new TiledTextureRegion(mSpritesheetMisc.getTexture(),
				mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(TexturesMisc.SUN_RAY_ID),
				mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(TexturesMisc.SUN_RAY_TAIL_ID));
	}

	public ITiledTextureRegion getClouds() {
		return new TiledTextureRegion(mSpritesheetMisc.getTexture(),
				mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(TexturesMisc.AMBIENT_CLOUD_CL03_ID),
				mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(TexturesMisc.AMBIENT_CLOUD_CL03A_ID),
				mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(TexturesMisc.AMBIENT_CLOUD_CL10_ID));
	}

	public ITextureRegion getRainDrop() {
		return mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(TexturesMisc.AMBIENT_RAINDROP_DROP1_ID);
	}

	public ITiledTextureRegion getRainSplash() {
		return new TiledTextureRegion(mSpritesheetMisc.getTexture(),
				mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(TexturesMisc.AMBIENT_RAINSPLASH_SPLASH1_ID),
				mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(TexturesMisc.AMBIENT_RAINSPLASH_SPLASH2_ID),
				mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(TexturesMisc.AMBIENT_RAINSPLASH_SPLASH3_ID));
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onLoadResources(final Engine e, final Context c) {
	}

	@Override
	public void onLoad(final Engine e, final Context c) {
		// Load spritesheets
		try {
			mSpritesheetParalax = new TexturePackLoader(e.getTextureManager(), "gfx/spritesheets/").loadFromAsset(c.getAssets(), "paralax.xml");
			mSpritesheetParalax.loadTexture();

		} catch (final TexturePackParseException ex) {
			Debug.e(ex);
		}
		// Load spritesheets
		try {
			mSpritesheetMisc = new TexturePackLoader(e.getTextureManager(), "gfx/spritesheets/").loadFromAsset(c.getAssets(), "misc.xml");
			mSpritesheetMisc.loadTexture();

		} catch (final TexturePackParseException ex) {
			Debug.e(ex);
		}
		// Load spritesheets
		try {
			mSpritesheetUi = new TexturePackLoader(e.getTextureManager(), "gfx/spritesheets/").loadFromAsset(c.getAssets(), "ui.xml");
			mSpritesheetUi.loadTexture();

		} catch (final TexturePackParseException ex) {
			Debug.e(ex);
		}

		// Load fonts
		final TextureManager textureManager = e.getTextureManager();
		final FontManager fontManager = e.getFontManager();

		mFontAtlas = new BitmapTextureAtlas(textureManager, 512, 256, TextureOptions.BILINEAR);
		mFontPopUp = FontFactory.createFromAsset(fontManager, mFontAtlas, c.getAssets(), Consts.HUD_FONT, Consts.CAMERA_HEIGHT*0.08f, true, Color.WHITE.getARGBPackedInt());
		mFontPopUp.load();
	}

	@Override
	public void onUnload() {
		mSpritesheetParalax.unloadTexture();
		mFontPopUp.unload();
		mFontAtlas.unload();
	}
	// ===========================================================
	// Methods
	// ===========================================================

	static public void dumpAttributeTextureFlower(final XmlSerializer pSerializer, final Sprite pSprite) throws IOException {
		dumpAttributeTexture(pSerializer, pSprite, TEXTURES_FIRST_FLOWER, TEXTURES_COUNT_FLOWER);
	}

	static public void dumpAttributeTextureSeed(final XmlSerializer pSerializer, final Sprite pSprite) throws IOException {
		dumpAttributeTexture(pSerializer, pSprite, TEXTURES_FIRST_SEED, TEXTURES_COUNT_SEED);
	}

	static private void dumpAttributeTexture(final XmlSerializer pSerializer, final Sprite pSprite, final int pFirst, final int pCount) throws IOException {
		pSerializer.attribute("", TAG_ATTRIBUTE_REGION_ID, "" + getIdentityOfRegion(pSprite.getTextureRegion(), pFirst, pCount));
//		pSerializer.attribute("", TAG_ATTRIBUTE_TEXTURE_ID, "" + pEntity.getY());
	}

	static private int getIdentityOfRegion(final ITextureRegion pTexture) {
		if (pTexture instanceof TexturePackerTextureRegion) {
			final TexturePackerTextureRegion tpreg = (TexturePackerTextureRegion) pTexture;
			return tpreg.getID();
		}
		Log.e("TexturesLibrary", "given TextrureRegion does not come from TexturePacker, so it does not have an ID!");
		Thread.dumpStack();
		return -666;
	}

	static private int getIdentityOfRegion(final ITextureRegion pTexture, final int pFirst, final int pCount) {
		final int ret = getIdentityOfRegion(pTexture);
		if ((ret<pFirst) || ret>=(pFirst + pCount)) {
			Log.e("TexturesLibrary", "given TextrureRegion is probably of a different type than the requested one! first=" + pFirst + "; count=" + pCount + "; id=" + ret);
			Thread.dumpStack();
		}
		return ret - pFirst;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
