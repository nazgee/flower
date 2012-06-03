package eu.nazgee.flower.activity.game.scene.main;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.game.utils.helpers.AtlasLoader;
import eu.nazgee.game.utils.loadable.LoadableResourceSimple;

public class LoadableParallaxBackground extends LoadableResourceSimple {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final VertexBufferObjectManager mVBOM;
	private BuildableBitmapTextureAtlas mAtlas;

	public ITextureRegion TEX_BG_FAR;
	public ITextureRegion TEX_BG_CLOSE;
//	public ITextureRegion TEX_GRASS;
	public ITextureRegion TEX_GROUND;
	public ITextureRegion TEX_SKY;

	private Sprite mLoadedGround;
	private CameraParallaxBackground mLoadedBackground;

	// ===========================================================
	// Constructors
	// ===========================================================
	public LoadableParallaxBackground(VertexBufferObjectManager pVBOM) {
		super();
		mVBOM = pVBOM;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public CameraParallaxBackground getLoadedBacground() {
		return mLoadedBackground;
	}

	public Sprite getLoadedGroundSprite() {
		return mLoadedGround;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onLoadResources(Engine e, Context c) {
	}

	@Override
	public void onLoad(Engine e, Context c) {
		Camera mCamera = e.getCamera();

		mAtlas = new BuildableBitmapTextureAtlas(e.getTextureManager(), 1024, 1024, TextureOptions.REPEATING_BILINEAR);
		TEX_SKY = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mAtlas, c, "scene/skies/azure.jpeg");
		TEX_BG_FAR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mAtlas, c, "scene/bg-far.png");
		TEX_BG_CLOSE = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mAtlas, c, "scene/bg-close.png");
		TEX_GROUND = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mAtlas, c, "scene/ground.png");
//		TEX_GRASS = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
//				mAtlas, c, "scene/grass.png");

		AtlasLoader.buildAndLoad(mAtlas);

		final VertexBufferObjectManager vertexBufferObjectManager = mVBOM;
		final Sprite bgSky = new Sprite(0, 0, TEX_SKY, vertexBufferObjectManager);
		final Sprite bgFar = new Sprite(0, mCamera.getHeight() - TEX_GROUND.getHeight() - TEX_BG_FAR.getHeight(), TEX_BG_FAR, vertexBufferObjectManager);
		final Sprite bgClose = new Sprite(0, mCamera.getHeight() - TEX_GROUND.getHeight() - TEX_BG_CLOSE.getHeight(), TEX_BG_CLOSE, vertexBufferObjectManager);
		mLoadedGround = new Sprite(0, mCamera.getHeight() - TEX_GROUND.getHeight(), TEX_GROUND, vertexBufferObjectManager);
		mLoadedBackground = new CameraParallaxBackground(0, 0, 0, mCamera);
		mLoadedBackground.attachParallaxEntity(new ParallaxEntity(-0.1f, bgSky));
		mLoadedBackground.attachParallaxEntity(new ParallaxEntity(-0.25f, bgFar));
		mLoadedBackground.attachParallaxEntity(new ParallaxEntity(-0.5f, bgClose));
		mLoadedBackground.attachParallaxEntity(new ParallaxEntity(-1f, mLoadedGround));
//		paralaxBG.attachParallaxEntity(new ParallaxEntity(-1.5f, bgGrass));
	}

	@Override
	public void onUnload() {
		mAtlas.unload();
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
