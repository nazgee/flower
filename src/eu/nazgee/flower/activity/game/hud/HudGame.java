package eu.nazgee.flower.activity.game.hud;

import java.text.DecimalFormat;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSCounter;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.content.Context;
import eu.nazgee.flower.Consts;
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;
import eu.nazgee.game.utils.scene.HUDLoadable;

public class HudGame extends HUDLoadable {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int SCORE_LINES = 3;
	// ===========================================================
	// Fields
	// ===========================================================

	MyResources mResource = new MyResources();
	Text mTextFPS;
	Text mTextScore[] = new Text[SCORE_LINES];
	// ===========================================================
	// Constructors
	// ===========================================================
	public HudGame(float W, float H,
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
		mTextFPS = new Text(0, 0, mResource.FONT_HUD, "", 20, getVertexBufferObjectManager());
		attachChild(mTextFPS);

		for (int i = 0; i < mTextScore.length; i++) {
			mTextScore[i] = new Text(0, i * mResource.FONT_HUD.getLineHeight(), mResource.FONT_HUD, "0", 50,
					new TextOptions(AutoWrap.LETTERS, getW() * 0.95f, HorizontalAlign.RIGHT, Text.LEADING_DEFAULT),
					getVertexBufferObjectManager());
			attachChild(mTextScore[i]);
		}


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
		 *  We do not need anything of these anymore- kill all children and
		 *  get rid of anything else that might want to run without any reason 
		 */
		detachChildren();
		clearEntityModifiers();
		clearUpdateHandlers();
	}
	// ===========================================================
	// Methods
	// ===========================================================
	public void setTextScore0(CharSequence pText) {
		mTextScore[0].setText(pText);
	}
	public void setTextScore1(CharSequence pText) {
		mTextScore[1].setText(pText);
	}
	public void setTextScore2(CharSequence pText) {
		mTextScore[2].setText(pText);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private static class MyResources extends SimpleLoadableResource {
		public volatile Font FONT_HUD;

		@Override
		public void onLoadResources(Engine e, Context c) {
		}

		@Override
		public void onLoad(Engine e, Context c) {
			final TextureManager textureManager = e.getTextureManager();
			final FontManager fontManager = e.getFontManager();

			final ITexture font_texture = new BitmapTextureAtlas(textureManager, 512, 256, TextureOptions.BILINEAR);
			FONT_HUD = FontFactory.createFromAsset(fontManager, font_texture, c.getAssets(), Consts.HUD_FONT, Consts.CAMERA_HEIGHT*0.1f, true, Color.WHITE.getARGBPackedInt());
			FONT_HUD.load();
		}

		@Override
		public void onUnload() {
			FONT_HUD.unload();
		}
	}
}
