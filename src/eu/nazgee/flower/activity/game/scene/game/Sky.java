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
	public float getHeightOnSky(float pSceneY) {
		return pSceneY - getGroundLevelOnScene(); 
	}

	public float getHeightOnSky(final IEntity pShape, eAnchorPointXY pAnchor) {
		return getHeightOnSky(pAnchor.getSceneY(pShape));
	}
	public float getHeightOnSkyTop(final IEntity pShape) {
		return getHeightOnSky(pShape, eAnchorPointXY.TOP_MIDDLE);
	}
	public float getHeightOnSkyCenter(final IEntity pShape) {
		return getHeightOnSky(pShape, eAnchorPointXY.CENTERED);
	}
	public float getHeightOnSkyBottom(final IEntity pShape) {
		return getHeightOnSky(pShape, eAnchorPointXY.BOTTOM_MIDDLE);
	}


	public boolean isCloseToGround(final float pSceneY, final float pMaxDistanceFromGround) {
		return (pSceneY - getGroundLevelOnScene()) <= pMaxDistanceFromGround;
	}
	public boolean isCloseToGroundTop(final IEntity pEntity, final float pMaxDistanceFromGround) {
		return Math.abs(getHeightOnSkyTop(pEntity)) <= pMaxDistanceFromGround;
	}
	public boolean isCloseToGroundCenter(final IEntity pEntity, final float pMaxDistanceFromGround) {
		return Math.abs(getHeightOnSkyCenter(pEntity)) <= pMaxDistanceFromGround;
	}
	public boolean isCloseToGroundBottom(final IEntity pEntity, final float pMaxDistanceFromGround) {
		return Math.abs(getHeightOnSkyBottom(pEntity)) <= pMaxDistanceFromGround;
	}

	public float getGroundLevelOnScene() {
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
