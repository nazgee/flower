package eu.nazgee.flower.base.questionscene;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;


public class SceneInfo extends SceneQuestion {

	public SceneInfo(float W, float H, Camera pCamera,
			VertexBufferObjectManager pVertexBufferObjectManager,
			CharSequence pCharSequence, HorizontalAlign pHorizontalAlign,
			String[] pButtons) {
		super(W, H, pCamera, pVertexBufferObjectManager, pCharSequence,
				pHorizontalAlign, pButtons);
	}

//	private class MyResources extends SimpleLoadableResource {
//		private static final int TEXSIZE_MAX = 1024;
//		private static final int FRAMESIZE_MAX = TEXSIZE_MAX - 5;
//		private BuildableBitmapTextureAtlas[] mAtlases;
//		public Font FONT;
//		public ITextureRegion TEX_FRAME;
//		public Text QUESTION_TEXT;
//		private final CharSequence mQuestionText;
//		private final HorizontalAlign mHorizontalAlign;
//
//		public MyResources(CharSequence pQuestionText, HorizontalAlign pHorizontalAlign) {
//			this.mQuestionText = pQuestionText;
//			this.mHorizontalAlign = pHorizontalAlign;
//			
//		}
//
//		@Override
//		public void onLoadResources(Engine e, Context c) {
//
//		}
//
//		@Override
//		public void onLoad(Engine e, Context c) {
//			final TextureManager textureManager = e.getTextureManager();
//			final FontManager fontManager = e.getFontManager();
//
//			final ITexture font_texture = new BitmapTextureAtlas(textureManager, 512, 256, TextureOptions.BILINEAR);
//			FONT = FontFactory.createFromAsset(fontManager, font_texture, c.getAssets(), Consts.MENU_FONT, Consts.CAMERA_HEIGHT*0.1f, true, Color.WHITE.getARGBPackedInt());
//			FONT.load();
//			QUESTION_TEXT = new Text(0, 0, FONT, mQuestionText,
//					new TextOptions(AutoWrap.WORDS, getW() * 0.9f, mHorizontalAlign,Text.LEADING_DEFAULT), getVertexBufferObjectManager());
//
//			mAtlases = new BuildableBitmapTextureAtlas[1];
//			mAtlases[0] = new BuildableBitmapTextureAtlas(e.getTextureManager(), getTexW(QUESTION_TEXT), getTexH(QUESTION_TEXT), TextureOptions.REPEATING_BILINEAR);
//			BuildableBitmapTextureAtlas frameAtlas = mAtlases[0];
//			TEX_FRAME = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(frameAtlas, c, "hud/frame.svg", getFrameW(frameAtlas), getFrameH(frameAtlas));
//			AtlasLoader.buildAndLoad(mAtlases);
//		}
//
//		@Override
//		public void onUnload() {
//			for (BuildableBitmapTextureAtlas atlas : mAtlases) {
//				atlas.unload();
//				atlas = null;
//			}
//			FONT.unload();
//		}
//
//		protected int getTexW(Text pQuestionText) {
//			return (int) Math.min(pQuestionText.getWidth() + getW() * 0.1f + 5, TEXSIZE_MAX);
//		}
//
//		protected int getTexH(Text pQuestionText) {
//			return (int) Math.min(pQuestionText.getHeight() + FONT.getLineHeight() + 5, TEXSIZE_MAX);
//		}
//
//		protected int getFrameW(BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas) {
//			return (int) Math.min(pBuildableBitmapTextureAtlas.getWidth() - 5, FRAMESIZE_MAX);
//		}
//
//		protected int getFrameH(BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas) {
//			return (int) Math.min(pBuildableBitmapTextureAtlas.getHeight() - 5, FRAMESIZE_MAX);
//		}
//	}
}
