package eu.nazgee.util;

import org.andengine.entity.IEntity;
import org.andengine.util.Constants;

import android.util.Log;



public class Anchor {
	// ===========================================================
	// Constants
	// ===========================================================
	public enum eRatio {
		ZERO(0.0f),
		HALF(0.5f),
		ONE(1.0f);

		public final float ratio;
		public float getValue(final float pSize) {
			return ratio * pSize;
		}

		private eRatio(float align) {
			this.ratio = align;
		}
	}

	public enum eAnchorPointXY {
		TOP_LEFT(eRatio.ZERO, eRatio.ONE),
		TOP_MIDDLE(eRatio.HALF, eRatio.ONE),
		TOP_RIGHT(eRatio.ONE, eRatio.ONE),
		CENTERED_LEFT(eRatio.ZERO, eRatio.HALF),
		CENTERED(eRatio.HALF, eRatio.HALF),
		CENTERED_RIGHT(eRatio.ONE, eRatio.HALF),
		BOTTOM_LEFT(eRatio.ZERO, eRatio.ZERO),
		BOTTOM_MIDDLE(eRatio.HALF, eRatio.ZERO),
		BOTTOM_RIGHT(eRatio.ONE, eRatio.ZERO),
		DEFAULT(eRatio.HALF, eRatio.HALF);

		public final eRatio x;
		public final eRatio y;
		private final float[] mReuse = new float[2];

		private eAnchorPointXY(eRatio pAnchorPointX, eRatio pAnchorPointY) {
			x = pAnchorPointX;
			y = pAnchorPointY;
		}

		public synchronized float getObjectX(final IEntity pEntity) {
			return x.getValue(pEntity.getWidth());
		}

		public synchronized float getObjectY(final IEntity pEntity) {
			return y.getValue(pEntity.getHeight());
		}

		public synchronized float getParentX(final IEntity pEntity) {
			return pEntity.getX() - getOffsetXFromDefault(pEntity);
		}

		public synchronized float getParentY(final IEntity pEntity) {
			return pEntity.getY() - getOffsetYFromDefault(pEntity);
		}

		public synchronized float getOffsetXFromDefault(final IEntity pEntity) {
			return eAnchorPointXY.DEFAULT.getObjectX(pEntity) - getObjectX(pEntity);
		}

		public synchronized float getOffsetYFromDefault(final IEntity pEntity) {
			return eAnchorPointXY.DEFAULT.getObjectY(pEntity) - getObjectY(pEntity);
		}

		public synchronized float getSceneX(IEntity pShape) {
			pShape.convertLocalCoordinatesToSceneCoordinates(getObjectX(pShape), getObjectY(pShape), mReuse);
			return mReuse[Constants.VERTEX_INDEX_X];
		}

		public synchronized float getSceneY(IEntity pShape) {
			pShape.convertLocalCoordinatesToSceneCoordinates(getObjectX(pShape), getObjectY(pShape), mReuse);
			return mReuse[Constants.VERTEX_INDEX_Y];
		}

		public synchronized float[] getSceneXY(IEntity pShape, float[] pReuse) {
			pShape.convertLocalCoordinatesToSceneCoordinates(getObjectX(pShape), getObjectY(pShape), pReuse);
			return pReuse;
		}
	}
	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * Sets a position of a given item taking into account given anchor point
	 * 
	 * @param pItem item to be placed
	 * @param x position in a TOP-LEFT coorfinates
	 * @param y position in a TOP-LEFT coorfinates
	 * @param pRequestedAnchor anchor point of given item
	 * @param will NOT work correctly for rotated items!
	 */
	public static void setPos(final IEntity pItem, final float x, final float y, final eAnchorPointXY pRequestedAnchor) {
		final float offsetX = pRequestedAnchor.getOffsetXFromDefault(pItem);
		final float offsetY = pRequestedAnchor.getOffsetYFromDefault(pItem);

		Log.d("setItemPosition", x + "," + y + "->" + (x - offsetX) +","+ (y - offsetY) + "; w=" + pItem.getWidth() + "; h=" + pItem.getHeight() + "; pItem=" + pItem);
		pItem.setPosition(x + offsetX, y + offsetY);
	}

	/*
	 * some shortcuts fo setting a position without suppling AnchorPoint- appropriate anchor point
	 * is used depending on the name of the function called
	 */
	public static void setPosTopLeft(final IEntity pItem, final float x, final float y) {
		setPos(pItem, x, y, eAnchorPointXY.TOP_LEFT);
	}
	public static void setPosTopMiddle(final IEntity pItem, final float x, final float y) {
		setPos(pItem, x, y, eAnchorPointXY.TOP_MIDDLE);
	}
	public static void setPosTopRight(final IEntity pItem, final float x, final float y) {
		setPos(pItem, x, y, eAnchorPointXY.TOP_RIGHT);
	}
	public static void setPosBottomLeft(final IEntity pItem, final float x, final float y) {
		setPos(pItem, x, y, eAnchorPointXY.BOTTOM_LEFT);
	}
	public static void setPosBottomMiddle(final IEntity pItem, final float x, final float y) {
		setPos(pItem, x, y, eAnchorPointXY.BOTTOM_MIDDLE);
	}
	public static void setPosBottomRight(final IEntity pItem, final float x, final float y) {
		setPos(pItem, x, y, eAnchorPointXY.BOTTOM_RIGHT);
	}
	public static void setPosCenterLeft(final IEntity pItem, final float x, final float y) {
		setPos(pItem, x, y, eAnchorPointXY.CENTERED_LEFT);
	}
	public static void setPosCenter(final IEntity pItem, final float x, final float y) {
		setPos(pItem, x, y, eAnchorPointXY.CENTERED);
	}
	public static void setPosCenterRight(final IEntity pItem, final float x, final float y) {
		setPos(pItem, x, y, eAnchorPointXY.CENTERED_RIGHT);
	}

	/*
	 * some shortcuts for setting a position relative to parent of the given entity
	 */
	public static void setPosAtParent(final IEntity pItem, final eAnchorPointXY pAnchorInEntity, final eAnchorPointXY pAnchorInParent) {
		final IEntity parent = pItem.getParent();
		final float x = pAnchorInParent.getObjectX(parent);
		final float y = pAnchorInParent.getObjectY(parent);
		Log.d("setPositionAtParent", x + "," + y + "; pAnchorInEntity=" + pAnchorInEntity.toString() + "; pAnchorInParent=" + pAnchorInParent.toString());
		setPos(pItem, x, y, pAnchorInEntity);
	}
	public static void setPosTopLeftAtParent(final IEntity pItem, final eAnchorPointXY pAnchorInParent) {
		setPosAtParent(pItem, eAnchorPointXY.TOP_LEFT, pAnchorInParent);
	}
	public static void setPosTopMiddleAtParent(final IEntity pItem, final eAnchorPointXY pAnchorInParent) {
		setPosAtParent(pItem, eAnchorPointXY.TOP_MIDDLE, pAnchorInParent);
	}
	public static void setPosTopRightAtParent(final IEntity pItem, final eAnchorPointXY pAnchorInParent) {
		setPosAtParent(pItem, eAnchorPointXY.TOP_RIGHT, pAnchorInParent);
	}
	public static void setPosBottomLeftAtParent(final IEntity pItem, final eAnchorPointXY pAnchorInParent) {
		setPosAtParent(pItem, eAnchorPointXY.BOTTOM_LEFT, pAnchorInParent);
	}
	public static void setPosBottomMiddleAtParent(final IEntity pItem, final eAnchorPointXY pAnchorInParent) {
		setPosAtParent(pItem, eAnchorPointXY.BOTTOM_MIDDLE, pAnchorInParent);
	}
	public static void setPosBottomRightAtParent(final IEntity pItem, final eAnchorPointXY pAnchorInParent) {
		setPosAtParent(pItem, eAnchorPointXY.BOTTOM_RIGHT, pAnchorInParent);
	}
	public static void setPosCenterLeftAtParent(final IEntity pItem, final eAnchorPointXY pAnchorInParent) {
		setPosAtParent(pItem, eAnchorPointXY.CENTERED_LEFT, pAnchorInParent);
	}
	public static void setPosCenterAtParent(final IEntity pItem, final eAnchorPointXY pAnchorInParent) {
		setPosAtParent(pItem, eAnchorPointXY.CENTERED, pAnchorInParent);
	}
	public static void setPosCenterRightAtParent(final IEntity pItem, final eAnchorPointXY pAnchorInParent) {
		setPosAtParent(pItem, eAnchorPointXY.CENTERED_RIGHT, pAnchorInParent);
	}

	/*
	 * some shortcuts for setting a position relative to the sibling of the given entity
	 */
	public static void setPosAtSibling(final IEntity pEntity, final eAnchorPointXY pAnchorInEntity, final IEntity pSibling, final eAnchorPointXY pAnchorInSibling) {
		// Check whether developer knows what he's doing
		if (pEntity.getParent() != pSibling.getParent()) {
			throw new RuntimeException("Given entities are not Siblings! Check you code, please!");
		}

		final float parentX = pAnchorInSibling.getParentX(pSibling);
		final float parentY = pAnchorInSibling.getParentY(pSibling);
		setPos(pEntity, parentX, parentY, pAnchorInEntity);
	}
	public static void setPosTopLeftAtSibling(final IEntity pItem, final IEntity pSibling, final eAnchorPointXY pAnchorInSibling) {
		setPosAtSibling(pItem, eAnchorPointXY.TOP_LEFT, pSibling, pAnchorInSibling);
	}
	public static void setPosTopMiddleAtSibling(final IEntity pItem, final IEntity pSibling, final eAnchorPointXY pAnchorInSibling) {
		setPosAtSibling(pItem, eAnchorPointXY.TOP_MIDDLE, pSibling, pAnchorInSibling);
	}
	public static void setPosTopRightAtSibling(final IEntity pItem, final IEntity pSibling, final eAnchorPointXY pAnchorInSibling) {
		setPosAtSibling(pItem, eAnchorPointXY.TOP_RIGHT, pSibling, pAnchorInSibling);
	}
	public static void setPosBottomLeftAtSibling(final IEntity pItem, final IEntity pSibling, final eAnchorPointXY pAnchorInSibling) {
		setPosAtSibling(pItem, eAnchorPointXY.BOTTOM_LEFT, pSibling, pAnchorInSibling);
	}
	public static void setPosBottomMiddleAtSibling(final IEntity pItem, final IEntity pSibling, final eAnchorPointXY pAnchorInSibling) {
		setPosAtSibling(pItem, eAnchorPointXY.BOTTOM_MIDDLE, pSibling, pAnchorInSibling);
	}
	public static void setPosBottomRightAtSibling(final IEntity pItem, final IEntity pSibling, final eAnchorPointXY pAnchorInSibling) {
		setPosAtSibling(pItem, eAnchorPointXY.BOTTOM_RIGHT, pSibling, pAnchorInSibling);
	}
	public static void setPosCenterLeftAtSibling(final IEntity pItem, final IEntity pSibling, final eAnchorPointXY pAnchorInSibling) {
		setPosAtSibling(pItem, eAnchorPointXY.CENTERED_LEFT, pSibling, pAnchorInSibling);
	}
	public static void setPosCenterAtSibling(final IEntity pItem, final IEntity pSibling, final eAnchorPointXY pAnchorInSibling) {
		setPosAtSibling(pItem, eAnchorPointXY.CENTERED, pSibling, pAnchorInSibling);
	}
	public static void setPosCenterRightAtSibling(final IEntity pItem, final IEntity pSibling, final eAnchorPointXY pAnchorInSibling) {
		setPosAtSibling(pItem, eAnchorPointXY.CENTERED_RIGHT, pSibling, pAnchorInSibling);
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