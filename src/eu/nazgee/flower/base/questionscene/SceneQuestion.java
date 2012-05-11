package eu.nazgee.flower.base.questionscene;

import java.util.Iterator;
import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
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
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.color.Color;

import android.content.Context;
import eu.nazgee.flower.Consts;
import eu.nazgee.game.utils.helpers.AtlasLoader;
import eu.nazgee.game.utils.helpers.Positioner;
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;
import eu.nazgee.game.utils.scene.menu.MenuLoadable;

public class SceneQuestion extends MenuLoadable {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	protected final MyResources mResources;
	private final String[] mButtons;
	private ISceneQuestionListener mSceneQuestionListener;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneQuestion(float W, float H, final Camera pCamera,
			VertexBufferObjectManager pVertexBufferObjectManager,
			CharSequence pCharSequence, HorizontalAlign pHorizontalAlign, String... pButtons) {
		super(W, H, pCamera, pVertexBufferObjectManager);
		mButtons = pButtons;
		mResources = new MyResources(pCharSequence, pHorizontalAlign);
		getLoader().install(mResources);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public ISceneQuestionListener getSceneQuestionListener() {
		return mSceneQuestionListener;
	}

	public void setSceneQuestionListener(ISceneQuestionListener mSceneQuestionListener) {
		this.mSceneQuestionListener = mSceneQuestionListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public interface ISceneQuestionListener {
		void onSceneClosed(String pResult);
	}

	@Override
	public void onLoadResources(Engine e, Context c) {
	}

	@Override
	public void onLoad(final Engine e, final Context c) {
		prepareContent(e, c);

		this.setMenuAnimator(new HorizontalMenuAnimator());

		float texts_width = 0;
		SmartList<Text> texts = new SmartList<Text>(mButtons.length);
		for (String button : mButtons) {
			texts.add(new Text(0, 0, mResources.FONT, button, getVertexBufferObjectManager()));
			texts_width += texts.getLast().getWidth();
		}

		for (Iterator<Text> iterator = texts.iterator(); iterator.hasNext();) {
			Text text = (Text) iterator.next();

			IMenuItem item = addMenuEntry(getW() * text.getWidth() / texts_width, getH() * 0.2f, mResources.TEX_FRAME, 666, Color.WHITE, Color.RED, getVertexBufferObjectManager());
			item.attachChild(text);
			Positioner.setCentered(text, item);
		}

		this.buildAnimations();
		setBackgroundEnabled(false);
		reset();
	}

	@Override
	public void onUnload() {
		clearEntityModifiers();
		clearUpdateHandlers();
		clearTouchAreas();
		clearMenuItems();
		detachChildren();
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
	public IMenuItem addMenuEntry(float w, float h,
			ITextureRegion pTextureRegion, int pID, Color pSelected,
			Color pUnselected, VertexBufferObjectManager pVBOM) {
		final IMenuItem menuItem;
		menuItem = new ColorMenuItemDecorator(new SpriteMenuItem(pID, w, h, pTextureRegion, pVBOM), pSelected, pUnselected);
		menuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		addMenuItem(menuItem);
		return menuItem;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private class MyResources extends SimpleLoadableResource {
		private static final int TEXSIZE_MAX = 1024;
		private static final int FRAMESIZE_MAX = TEXSIZE_MAX - 5;
		private BuildableBitmapTextureAtlas[] mAtlases;
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
