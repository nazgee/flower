package eu.nazgee.flower.activity.game.scene.game;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.flower.TexturesLibrary;

public class GameBackground extends CameraParallaxBackground {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final Sprite mGround;

	// ===========================================================
	// Constructors
	// ===========================================================
	public GameBackground(final Camera pCamera, TexturesLibrary pTexturesLibrary, VertexBufferObjectManager pVBOM) {
		super(0, 0, 0, pCamera);

		final float y_bottom =	pCamera.getHeight();
		final float y_gnd =	y_bottom - pTexturesLibrary.getParalaxGround().getHeight();
		final float y_bg1 =	y_bottom - pTexturesLibrary.getParalaxBack1().getHeight() - 42;
		final float y_bg4 =	y_bottom - pTexturesLibrary.getParalaxBack4().getHeight() - 88;
		final float y_bg2 =	y_bottom - pTexturesLibrary.getParalaxBack2().getHeight() - 88;
		final float y_bg3 =	y_bottom - pTexturesLibrary.getParalaxBack3().getHeight() - 88;

		final Sprite bg4 =	new Sprite(0, y_bg4, pTexturesLibrary.getParalaxBack4(), pVBOM);
		final Sprite bg3 =	new Sprite(0, y_bg3, pTexturesLibrary.getParalaxBack3(), pVBOM);
		final Sprite bg2 =	new Sprite(0, y_bg2, pTexturesLibrary.getParalaxBack2(), pVBOM);
		final Sprite bg1 =	new Sprite(0, y_bg1, pTexturesLibrary.getParalaxBack1(), pVBOM);
		mGround =			new Sprite(0, y_gnd, pTexturesLibrary.getParalaxGround(), pVBOM);
		final Sprite bgSky = new Sprite(0, 0, pTexturesLibrary.getSky(), pVBOM);

		attachParallaxEntity(new ParallaxEntity(-0.1f, bgSky));
		attachParallaxEntity(new ParallaxEntity(-0.2f, bg4));
		attachParallaxEntity(new ParallaxEntity(-0.4f, bg3));
		attachParallaxEntity(new ParallaxEntity(-0.6f, bg2));
		attachParallaxEntity(new ParallaxEntity(-0.8f, bg1));
		attachParallaxEntity(new ParallaxEntity(-1f, mGround));
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public Sprite getGroundSprite() {
		return mGround;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
