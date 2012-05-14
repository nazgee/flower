package eu.nazgee.flower;

import org.andengine.engine.Engine;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.color.Color;

import android.content.Context;

public class Statics {
	private static Statics mInstance;
	public EntityDetachRunnablePoolUpdateHandler ENTITY_DETACH_HANDLER;
	public Font FONT_DESC;

	private Statics(Engine e, Context c) {
		ENTITY_DETACH_HANDLER = new EntityDetachRunnablePoolUpdateHandler();
		e.registerUpdateHandler(ENTITY_DETACH_HANDLER);

		final TextureManager textureManager = e.getTextureManager();
		final FontManager fontManager = e.getFontManager();

		final ITexture font_texture = new BitmapTextureAtlas(textureManager, 512, 256, TextureOptions.BILINEAR);
		FONT_DESC = FontFactory.createFromAsset(fontManager, font_texture, c.getAssets(), Consts.MENU_FONT, Consts.CAMERA_HEIGHT*0.1f, true, Color.WHITE.getARGBPackedInt());
		FONT_DESC.load();
	}

	static public synchronized Statics getInstanceSafe(Engine e, Context c) {
		if (!isInitialized()) {
			mInstance = new Statics(e, c);
		}
		return mInstance;
	}

	static public synchronized Statics getInstanceUnsafe() {
		if (!isInitialized()) {
			throw new RuntimeException("You have not initialized statics!");
		}
		return mInstance;
	}

	static public synchronized boolean isInitialized() {
		return (mInstance != null);
	}
}
