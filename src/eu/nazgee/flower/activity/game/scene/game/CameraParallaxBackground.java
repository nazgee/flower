package eu.nazgee.flower.activity.game.scene.game;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.ParallaxBackground;

public class CameraParallaxBackground extends ParallaxBackground {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final Camera mCamera;
	private final float mCameraFactor;

	// ===========================================================
	// Constructors
	// ===========================================================
	public CameraParallaxBackground(float pRed, float pGreen, float pBlue,
			Camera pCamera) {
		this(pRed, pGreen, pBlue, pCamera, 1);
	}

	public CameraParallaxBackground(float pRed, float pGreen, float pBlue,
			Camera pCamera, final float pCameraFactor) {
		super(pRed, pGreen, pBlue);
		mCamera = pCamera;
		mCameraFactor = pCameraFactor;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		final float camx = mCamera.getCenterX();
		setParallaxValue(camx * mCameraFactor);

		super.onUpdate(pSecondsElapsed);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
