package eu.nazgee.flower.base.questionscene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.content.Context;
import eu.nazgee.flower.Consts;
import eu.nazgee.game.utils.helpers.AtlasLoader;
import eu.nazgee.game.utils.helpers.Positioner;
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;


public class SceneInfo extends SceneQuestion {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	protected final MyResources mResources;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneInfo(float W, float H, Camera pCamera,
			VertexBufferObjectManager pVertexBufferObjectManager,
			CharSequence pCharSequence, String ... pButtons) {
		super(W, H, pCamera, pVertexBufferObjectManager, pButtons);
		mResources = new MyResources(pCharSequence, HorizontalAlign.CENTER);
		getLoader().install(mResources);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	@Override
	protected Font getButtonFont() {
		return mResources.FONT;
	}

	@Override
	protected ITextureRegion getButtonFace(Text pText) {
		return mResources.TEX_FRAME;
	}

	@Override
	protected HorizontalMenuAnimator getMenuAnimator(Engine e, Context c) {
		return new HorizontalMenuAnimator();
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onLoad(Engine e, Context c) {
		super.onLoad(e, c);
		prepareContent(e, c);
	}

	private void prepareContent(final Engine e, final Context c) {
		final Sprite mFrame = new Sprite(0, 0, mResources.TEX_FRAME, getVertexBufferObjectManager());

		attachChild(mFrame);
		attachChild(mResources.QUESTION_TEXT);

		Positioner.setCentered(mResources.QUESTION_TEXT, mFrame);

		mFrame.setAlpha(0.5f);
		mFrame.setColor(Color.BLACK);
	}
	// ===========================================================
	// Methods
	// ===========================================================


	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private class MyResources extends SimpleLoadableResource {
		private static final int TEXSIZE_MAX = 1024;
		private static final int FRAMESIZE_MAX = TEXSIZE_MAX - 5;
		protected BuildableBitmapTextureAtlas[] mAtlases;
		public Font FONT;
		public ITextureRegion TEX_FRAME;
		public Text QUESTION_TEXT;
		private final CharSequence mQuestionText;
		private final HorizontalAlign mHorizontalAlign;

		public MyResources(CharSequence pQuestionText, HorizontalAlign pHorizontalAlign) {
			this.mQuestionText = pQuestionText;
			this.mHorizontalAlign = pHorizontalAlign;
			
		}

		@Override
		public void onLoadResources(Engine e, Context c) {

		}

		@Override
		public void onLoad(Engine e, Context c) {
			final TextureManager textureManager = e.getTextureManager();
			final FontManager fontManager = e.getFontManager();

			final ITexture font_texture = new BitmapTextureAtlas(textureManager, 512, 256, TextureOptions.BILINEAR);
			FONT = FontFactory.createFromAsset(fontManager, font_texture, c.getAssets(), Consts.MENU_FONT, Consts.CAMERA_HEIGHT*0.1f, true, Color.WHITE.getARGBPackedInt());
			FONT.load();
			QUESTION_TEXT = new Text(0, 0, FONT, mQuestionText,
					new TextOptions(AutoWrap.WORDS, getW() * 0.9f, mHorizontalAlign,Text.LEADING_DEFAULT), getVertexBufferObjectManager());

			mAtlases = new BuildableBitmapTextureAtlas[1];
			mAtlases[0] = new BuildableBitmapTextureAtlas(e.getTextureManager(), getTexW(QUESTION_TEXT), getTexH(QUESTION_TEXT), TextureOptions.REPEATING_BILINEAR);
			BuildableBitmapTextureAtlas frameAtlas = mAtlases[0];
			TEX_FRAME = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(frameAtlas, c, "hud/frame.svg", getFrameW(frameAtlas), getFrameH(frameAtlas));
			AtlasLoader.buildAndLoad(mAtlases);
		}

		@Override
		public void onUnload() {
			for (BuildableBitmapTextureAtlas atlas : mAtlases) {
				atlas.unload();
				atlas = null;
			}
			FONT.unload();
		}

		protected int getTexW(Text pQuestionText) {
			return (int) Math.min(pQuestionText.getWidth() + getW() * 0.1f + 5, TEXSIZE_MAX);
		}

		protected int getTexH(Text pQuestionText) {
			return (int) Math.min(pQuestionText.getHeight() + FONT.getLineHeight() + 5, TEXSIZE_MAX);
		}

		protected int getFrameW(BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas) {
			return (int) Math.min(pBuildableBitmapTextureAtlas.getWidth() - 5, FRAMESIZE_MAX);
		}

		protected int getFrameH(BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas) {
			return (int) Math.min(pBuildableBitmapTextureAtlas.getHeight() - 5, FRAMESIZE_MAX);
		}
	}
}
