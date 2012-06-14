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

public class LoadableParallaxBackground extends LoadableResourceSimple {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final VertexBufferObjectManager mVBOM;

	protected final ITextureRegion TEX_BG_FAR;
	protected final ITextureRegion TEX_BG_CLOSE;
	protected final ITextureRegion TEX_GRASS;
	protected final ITextureRegion TEX_GROUND;
	protected final ITextureRegion TEX_SKY;

	private CameraParallaxBackground mLoadedBackground;

	private final TexturesLibrary mTexturesLibrary;

	// ===========================================================
	// Constructors
	// ===========================================================
	public LoadableParallaxBackground(TexturesLibrary pTexturesLibrary, VertexBufferObjectManager pVBOM) {
		super();
		mTexturesLibrary = pTexturesLibrary;
		mVBOM = pVBOM;
		TEX_BG_FAR = mTexturesLibrary.getParalaxBack2();
		TEX_BG_CLOSE = mTexturesLibrary.getParalaxBack1();
		TEX_GROUND = mTexturesLibrary.getParalaxGround();
		TEX_GRASS = mTexturesLibrary.getParalaxFront1();
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
		final Sprite bgFar = new Sprite(0, mCamera.getHeight() - TEX_GROUND.getHeight() - TEX_BG_FAR.getHeight(), TEX_BG_FAR, vertexBufferObjectManager);
		final Sprite bgClose = new Sprite(0, mCamera.getHeight() - TEX_GROUND.getHeight() - TEX_BG_CLOSE.getHeight(), TEX_BG_CLOSE, vertexBufferObjectManager);
		final Sprite bgGrass = new Sprite(0, mCamera.getHeight() - TEX_GRASS.getHeight()/2, TEX_GRASS, vertexBufferObjectManager);
		final Sprite bgGround = new Sprite(0, mCamera.getHeight() - TEX_GROUND.getHeight(), TEX_GROUND, vertexBufferObjectManager);
		mLoadedBackground = new CameraParallaxBackground(0, 0, 0, mCamera);
		mLoadedBackground.attachParallaxEntity(new ParallaxEntity(-0.1f, bgSky));
		mLoadedBackground.attachParallaxEntity(new ParallaxEntity(-0.25f, bgFar));
		mLoadedBackground.attachParallaxEntity(new ParallaxEntity(-0.5f, bgClose));
		mLoadedBackground.attachParallaxEntity(new ParallaxEntity(-1f, bgGround));
		mLoadedBackground.attachParallaxEntity(new ParallaxEntity(-1.5f, bgGrass));
	}

	@Override
	public void onUnload() {
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
