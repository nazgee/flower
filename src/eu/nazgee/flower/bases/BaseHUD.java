package eu.nazgee.flower.bases;

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
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

import android.content.Context;
import eu.nazgee.flower.Consts;
import eu.nazgee.game.utils.loadable.LoadableResourceSimple;
import eu.nazgee.game.utils.scene.HUDLoadable;
import eu.nazgee.util.LayoutBase;

public class BaseHUD extends HUDLoadable {
	// ===========================================================
	// Constants
	// ===========================================================
	// ===========================================================
	// Fields
	// ===========================================================

	protected HudResources mResources = new HudResources();
	private Text mTextFPS;
	private Text mTextLines[];
	// ===========================================================
	// Constructors
	// ===========================================================
	public BaseHUD(float W, float H, int pTexLinesCount,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pVertexBufferObjectManager);
		this.mTextLines = new Text[pTexLinesCount];
		getLoader().install(mResources);
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
		mTextFPS = new Text(0, 0, mResources.FONT_HUD, "fps:59,08", 20, getVertexBufferObjectManager());
		attachChild(mTextFPS);
		LayoutBase.setItemPositionTopLeft(mTextFPS, 100, e.getCamera().getHeight());
//		mTextFPS.setPosition(100, e.getCamera().getHeight());

		for (int i = 0; i < mTextLines.length; i++) {
			mTextLines[i] = new Text(0, i * mResources.FONT_HUD.getLineHeight(), mResources.FONT_HUD, "0", 50,
					new TextOptions(AutoWrap.LETTERS, getW() * 0.95f, HorizontalAlign.RIGHT, Text.LEADING_DEFAULT),
					getVertexBufferObjectManager());
			attachChild(mTextLines[i]);
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
	protected void setTextLine(int pLine, CharSequence pText) {
		mTextLines[pLine].setText(pText);
	}

	protected Text getTextLine(int pLine) {
		return mTextLines[pLine];
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	protected static class HudResources extends LoadableResourceSimple {
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
