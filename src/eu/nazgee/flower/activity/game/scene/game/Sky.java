package eu.nazgee.flower.activity.game.scene.game;

import org.andengine.entity.IEntity;

import eu.nazgee.util.Anchor.eAnchorPointXY;


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
	public float getHeightOnSky(final IEntity pShape) {
		return getHeightOnSky(eAnchorPointXY.CENTERED.getSceneY(pShape));
	}

	public float getHeightOnSkyTop(final IEntity pShape) {
		return getHeightOnSky(eAnchorPointXY.TOP_MIDDLE.getSceneY(pShape));
	}

	public float getHeightOnSkyBottom(final IEntity pShape) {
		return getHeightOnSky(eAnchorPointXY.BOTTOM_MIDDLE.getSceneY(pShape));
	}

	public float getHeightOnSky(float pSceneY) {
		return pSceneY - getGroundLevel(); 
	}

	public float getGroundLevel() {
		return mGroundLevel;
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
