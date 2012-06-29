package eu.nazgee.flower.activity.game.scene.game;

import java.util.Comparator;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.util.Constants;

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
	public Sky(final float mGroundLevel) {
		this.mGroundLevel = mGroundLevel;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public float getHeightOnSky(final float pSceneY) {
		return pSceneY - getGroundLevelOnScene();
	}

	public float getHeightOnSky(final IEntity pShape, final eAnchorPointXY pAnchor) {
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
		return (getHeightOnSky(pSceneY)) <= pMaxDistanceFromGround;
	}
	public boolean isCloseToGroundTop(final IEntity pEntity, final float pMaxDistanceFromGround) {
		return Math.abs(getHeightOnSkyTop(pEntity)) <= pMaxDistanceFromGround;
	}
	public boolean isCloseToGroundCenter(final IEntity pEntity, final float pMaxDistanceFromGround) {
		return Math.abs(getHeightOnSkyCenter(pEntity)) <= pMaxDistanceFromGround;
	}
	public boolean isCloseToGroundBottom(final IEntity pEntity, final float pMaxDistanceFromGround) {
//		Log.e("close", "pos=(" + pEntity.getX() + ", " + pEntity.getY() + ")"
//				+ "; size=(" + pEntity.getWidth() + ", " + pEntity.getHeight() + ")"
//				+ "; max=" + pMaxDistanceFromGround + "; h_bottom=" + getHeightOnSkyBottom(pEntity)
//				+ "; gnd_scene=" + getGroundLevelOnScene());
		return Math.abs(getHeightOnSkyBottom(pEntity)) <= Math.abs(pMaxDistanceFromGround);
	}

	public boolean isAboveGround(final float pSceneY) {
		return (getHeightOnSky(pSceneY)) > 0;
	}
	public boolean isAboveGroundTop(final IEntity pEntity) {
		return getHeightOnSkyTop(pEntity) > 0;
	}
	public boolean isAboveGroundCenter(final IEntity pEntity) {
		return getHeightOnSkyCenter(pEntity) > 0;
	}
	public boolean isAboveGroundBottom(final IEntity pEntity) {
//		Log.e("above", "pos=(" + pEntity.getX() + ", " + pEntity.getY() + ")"
//				+ "; size=(" + pEntity.getWidth() + ", " + pEntity.getHeight() + ")"
//				+ "; max=" + pMaxDistanceFromGround + "; h_bottom=" + getHeightOnSkyBottom(pEntity)
//				+ "; gnd_scene=" + getGroundLevelOnScene());
		return getHeightOnSkyBottom(pEntity) > 0;
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
	/**
	 * Used to sort items by theirs height
	 * @author nazgee
	 *
	 */
	public static class SkyHeightComparator<T extends Entity> implements Comparator<T> {
		private final Sky mSky;
		private final eAnchorPointXY mAnchor;

		public SkyHeightComparator(final Sky pSky, final eAnchorPointXY pAnchor) {
			this.mSky = pSky;
			this.mAnchor = pAnchor;
		}

		@Override
		public int compare(final T lhs, final T rhs) {
			final float hL = mSky.getHeightOnSky(lhs, mAnchor);
			final float hR = mSky.getHeightOnSky(lhs, mAnchor);

			return (int) (hL - hR);
		}
	}

}
