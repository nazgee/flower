package eu.nazgee.flower.activity.game.scene.main;

import org.andengine.entity.Entity;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.util.Constants;


public class Sky {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final float mGroundLevel;
	// ===========================================================
	// Constructors
	// ===========================================================
	public Sky(float mGroundLevel) {
		this.mGroundLevel = mGroundLevel;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public float getHeightOnSky(final Entity pEntity) {
		final float pos[] = pEntity.getSceneCenterCoordinates();
		return getHeightOnSky(pos[Constants.VERTEX_INDEX_Y]);
	}

	public float getHeightOnSkyTop(final IAreaShape pShape) {
		final float pos[] = pShape.getSceneCenterCoordinates();
		return getHeightOnSky(pos[Constants.VERTEX_INDEX_Y] - pShape.getHeight()/2);
	}

	public float getHeightOnSkyBottom(final IAreaShape pShape) {
		final float pos[] = pShape.getSceneCenterCoordinates();
		return getHeightOnSky(pos[Constants.VERTEX_INDEX_Y] + pShape.getHeight()/2);
	}

	public float getHeightOnSky(float pSceneY) {
		return mGroundLevel - pSceneY; 
	}

	public float getHeightOnScene(float pSkyY) {
		return mGroundLevel - pSkyY; 
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
