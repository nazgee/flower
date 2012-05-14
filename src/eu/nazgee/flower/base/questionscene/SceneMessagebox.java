package eu.nazgee.flower.base.questionscene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

import android.content.Context;
import eu.nazgee.game.utils.helpers.AtlasLoader;
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;

public class SceneMessagebox extends SceneMenuButtons {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final Font mFontButton;
	private final Font mFontText;
	private final MyResources mResources = new MyResources();
	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneMessagebox(float W, float H, Camera pCamera,
			VertexBufferObjectManager pVertexBufferObjectManager,
			Font pFontText, Font pFontButton, CharSequence pText,
			String... pButtons) {
		super(W, H, pCamera, pVertexBufferObjectManager, pButtons);
		this.mFontButton = pFontButton;
		this.mFontText = pFontText;
		this.getLoader().install(mResources);
		setText(pText);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected Font getButtonFont() {
		return this.mFontButton;
	}

	@Override
	protected ITextureRegion getButtonFace(Text pText) {
		return this.mResources.TEX_FRAME;
	}

	@Override
	protected HorizontalMenuAnimator getMenuAnimator(Engine e, Context c) {
		return new HorizontalMenuAnimator();
	}

	@Override
	public void onLoad(Engine e, Context c) {
		super.onLoad(e, c);
		prepareContent(e, c);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	private void prepareContent(final Engine e, final Context c) {
		Rectangle rect = new Rectangle(0, 0, getW(), getH(), getVertexBufferObjectManager());
		attachChild(rect);
		rect.setColor(0, 0, 0, 0.6f);
		rect.setZIndex(-1);

		Text text = new Text(0, 0, this.mFontText, getText(),
				new TextOptions(AutoWrap.WORDS, getW() * 0.9f, HorizontalAlign.CENTER,Text.LEADING_DEFAULT),
				getVertexBufferObjectManager());
		attachChild(text);
		text.setPosition((getW() - text.getWidth())/2, 
				(getH()	- this.mResources.TEX_FRAME.getHeight() - text.getHeight()) / 2);

		sortChildren(false);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private class MyResources extends SimpleLoadableResource {
		protected BuildableBitmapTextureAtlas[] mAtlases;
		public ITextureRegion TEX_FRAME;

		@Override
		public void onLoadResources(Engine e, Context c) {
		}

		@Override
		public void onLoad(Engine e, Context c) {
			final Camera camera = e.getCamera();

			final int frameW = (int) camera.getWidth();
			final int frameH = (int) (getButtonFont().getLineHeight() * 1.5f);
			this.mAtlases = new BuildableBitmapTextureAtlas[1];
			this.mAtlases[0] = new BuildableBitmapTextureAtlas(e.getTextureManager(), frameW + 5, frameH + 5, TextureOptions.REPEATING_BILINEAR);

			BuildableBitmapTextureAtlas frameAtlas = this.mAtlases[0];
			TEX_FRAME = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(frameAtlas, c, "hud/frame.svg", frameW, frameH);
			AtlasLoader.buildAndLoad(this.mAtlases);
		}

		@Override
		public void onUnload() {
			for (BuildableBitmapTextureAtlas atlas : this.mAtlases) {
				atlas.unload();
				atlas = null;
			}
		}
	}
}
