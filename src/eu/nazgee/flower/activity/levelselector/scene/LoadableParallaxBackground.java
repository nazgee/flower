package eu.nazgee.flower.activity.levelselector.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.activity.game.scene.game.CameraParallaxBackground;
import eu.nazgee.game.utils.loadable.LoadableResourceSimple;
import eu.nazgee.util.LayoutBase;

public class LoadableParallaxBackground extends LoadableResourceSimple {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final VertexBufferObjectManager mVBOM;

	protected final ITextureRegion TEX_BG4;
	protected final ITextureRegion TEX_BG3;
	protected final ITextureRegion TEX_BG2;
	protected final ITextureRegion TEX_BG1;
	protected final ITextureRegion TEX_GROUND;
	protected final ITextureRegion TEX_SKY;

	private CameraParallaxBackground mLoadedBackground;

	private final TexturesLibrary mTexturesLibrary;

	private Sprite bgGround;

	// ===========================================================
	// Constructors
	// ===========================================================
	public LoadableParallaxBackground(TexturesLibrary pTexturesLibrary, VertexBufferObjectManager pVBOM) {
		super();
		mTexturesLibrary = pTexturesLibrary;
		mVBOM = pVBOM;
		TEX_BG4 = mTexturesLibrary.getParalaxBack4();
		TEX_BG3 = mTexturesLibrary.getParalaxBack3();
		TEX_BG2 = mTexturesLibrary.getParalaxBack2();
		TEX_BG1 = mTexturesLibrary.getParalaxBack1();
		TEX_GROUND = mTexturesLibrary.getParalaxGround();
		TEX_SKY = mTexturesLibrary.getSky();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public CameraParallaxBackground getLoadedBacground() {
		return mLoadedBackground;
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

		final VertexBufferObjectManager vertexBufferObjectManager = mVBOM;
		final Sprite bgSky = new Sprite(0, 0, TEX_SKY, vertexBufferObjectManager);

		final float y_bottom =	mCamera.getHeight();
		final float y_gnd =	y_bottom - TEX_GROUND.getHeight();
		final float y_bg1 =	y_bottom - TEX_BG1.getHeight() - 42;
		final float y_bg4 =	y_bottom - TEX_BG4.getHeight() - 88;
		final float y_bg2 =	y_bottom - TEX_BG2.getHeight() - 88;
		final float y_bg3 =	y_bottom - TEX_BG3.getHeight() - 88;

		final Sprite bg4 =	new Sprite(0, y_bg4, TEX_BG4, vertexBufferObjectManager);
		final Sprite bg3 =	new Sprite(0, y_bg3, TEX_BG3, vertexBufferObjectManager);
		final Sprite bg2 =	new Sprite(0, y_bg2, TEX_BG2, vertexBufferObjectManager);
		final Sprite bg1 =	new Sprite(0, y_bg1, TEX_BG1, vertexBufferObjectManager);
		bgGround =			new Sprite(0, y_gnd, TEX_GROUND, vertexBufferObjectManager);


//		final Sprite bgGrass = new Sprite(0, mCamera.getHeight() - TEX_GRASS.getHeight()/2, TEX_GRASS, vertexBufferObjectManager);
		mLoadedBackground = new CameraParallaxBackground(0, 0, 0, mCamera);
		mLoadedBackground.attachParallaxEntity(new ParallaxEntity(-0.1f, bgSky));
		mLoadedBackground.attachParallaxEntity(new ParallaxEntity(-0.2f, bg4));
		mLoadedBackground.attachParallaxEntity(new ParallaxEntity(-0.4f, bg3));
		mLoadedBackground.attachParallaxEntity(new ParallaxEntity(-0.6f, bg2));
		mLoadedBackground.attachParallaxEntity(new ParallaxEntity(-0.8f, bg1));
		mLoadedBackground.attachParallaxEntity(new ParallaxEntity(-1f, bgGround));
//		mLoadedBackground.attachParallaxEntity(new ParallaxEntity(-1.5f, bgGrass));
	}

	@Override
	public void onUnload() {
	}
	// ===========================================================
	// Methods
	// ===========================================================

	public Sprite getGroundSprite() {
		return bgGround;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
