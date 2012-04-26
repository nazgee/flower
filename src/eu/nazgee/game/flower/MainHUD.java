package eu.nazgee.game.flower;

import java.text.DecimalFormat;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSCounter;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.content.Context;
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;
import eu.nazgee.game.utils.scene.HUDLoadable;

public class MainHUD extends HUDLoadable {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	MyResources mResource = new MyResources();
	Text mTextFPS;
	// ===========================================================
	// Constructors
	// ===========================================================
	public MainHUD(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pVertexBufferObjectManager);
		getLoader().install(mResource);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onLoadResources(Engine e, Context c) {
	}

	@Override
	public void onLoad(Engine e, Context c) {
		mTextFPS = new Text(0, 0, mResource.FONT_MENU, "This is a placeholder" , getVertexBufferObjectManager());
		attachChild(mTextFPS);
		
		// prepare FPS display
		final FPSCounter fpsCounter = new FPSCounter();
		registerUpdateHandler(fpsCounter);
		registerUpdateHandler(new TimerHandler(1 / 2.0f, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mTextFPS.setText("fps:"
								+ new DecimalFormat("#.##").format(fpsCounter
										.getFPS()));
						fpsCounter.reset();
					}
				}));
	}

	@Override
	public void onUnload() {
		/*
		 *  We do not need anything of theese anymore- kill all children and
		 *  get rid of anything else that might want to run without any reason 
		 */
		detachChildren();
		clearEntityModifiers();
		clearUpdateHandlers();
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private static class MyResources extends SimpleLoadableResource {
		public Font FONT_MENU;

		@Override
		public void onLoadResources(Engine e, Context c) {
			final TextureManager textureManager = e.getTextureManager();
			final FontManager fontManager = e.getFontManager();

			final ITexture font_texture = new BitmapTextureAtlas(textureManager, 512, 256, TextureOptions.BILINEAR);
			FONT_MENU = FontFactory.createFromAsset(fontManager, font_texture, c.getAssets(), Consts.MENU_FONT, Consts.CAMERA_HEIGHT*0.1f, true, Color.WHITE.getARGBPackedInt());
		}

		@Override
		public void onLoad(Engine e, Context c) {
			FONT_MENU.load();
		}

		@Override
		public void onUnload() {
			FONT_MENU.unload();
		}
	}
}
