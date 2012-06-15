package eu.nazgee.flower;

import org.andengine.engine.Engine;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePack;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackLoader;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackTextureRegionLibrary;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception.TexturePackParseException;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.graphics.Rect;

import eu.nazgee.game.utils.loadable.LoadableResourceSimple;
import eu.nazgee.util.NinePatchTextureRegionBatch;

public class TexturesLibrary extends LoadableResourceSimple{
	private TexturePack mSpritesheetParalax;
	public TexturePack mSpritesheetMisc;
	private TexturePack mSpritesheetUi;

	public TexturesLibrary() {
	}

	@Override
	public void onLoadResources(Engine e, Context c) {

	}

	@Override
	public void onLoad(Engine e, Context c) {
		// Load spritesheets

		try {
			mSpritesheetParalax = new TexturePackLoader(e.getTextureManager(), "gfx/spritesheets/").loadFromAsset(c.getAssets(), "paralax.xml");
			mSpritesheetParalax.loadTexture();

		} catch (final TexturePackParseException ex) {
			Debug.e(ex);
		}

		try {
			mSpritesheetMisc = new TexturePackLoader(e.getTextureManager(), "gfx/spritesheets/").loadFromAsset(c.getAssets(), "misc.xml");
			mSpritesheetMisc.loadTexture();

		} catch (final TexturePackParseException ex) {
			Debug.e(ex);
		}

		try {
			mSpritesheetUi = new TexturePackLoader(e.getTextureManager(), "gfx/spritesheets/").loadFromAsset(c.getAssets(), "ui.xml");
			mSpritesheetUi.loadTexture();

		} catch (final TexturePackParseException ex) {
			Debug.e(ex);
		}
	}

	@Override
	public void onUnload() {
		mSpritesheetParalax.unloadTexture();
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

	public ITextureRegion getParalaxGround() {
		return mSpritesheetParalax.getTexturePackTextureRegionLibrary().get(TexturesParalax.GROUND_ID);
	}

	public ITextureRegion getParalaxFront1() {
		return mSpritesheetParalax.getTexturePackTextureRegionLibrary().get(TexturesParalax.FRONT1_ID);
	}

	public ITextureRegion getFrameLevel() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.FRAME_LEVEL_ID);
	}
	public ITextureRegion getFrameSeed() {
		return mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.FRAME_SEED_ID);
	}
	public NinePatchTextureRegionBatch getFrameLevelCompleted() {
		final ITextureRegion tex = mSpritesheetUi.getTexturePackTextureRegionLibrary().get(TexturesUi.NINE_PATCH_GLASS_ORANGE_ID);
		final int margin = 15;
		return new NinePatchTextureRegionBatch(tex,new Rect(margin, margin, 
				(int)tex.getWidth() - margin, (int)tex.getHeight() - margin));
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

}
