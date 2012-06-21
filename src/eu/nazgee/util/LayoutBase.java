package eu.nazgee.util;

import java.util.LinkedList;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.util.Constants;

import android.util.Log;



public class LayoutBase extends Entity {
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

		public synchronized float getSceneX(IEntity pShape) {
			pShape.convertLocalCoordinatesToSceneCoordinates(x.getValue(pShape.getWidth()), x.getValue(pShape.getHeight()), mReuse);
			return mReuse[Constants.VERTEX_INDEX_X];
		}

		public synchronized float getSceneY(IEntity pShape) {
			pShape.convertLocalCoordinatesToSceneCoordinates(x.getValue(pShape.getWidth()), y.getValue(pShape.getHeight()), mReuse);
			return mReuse[Constants.VERTEX_INDEX_Y];
		}

		public synchronized float getEntityAnchorX(IEntity pShape) {
			return x.getValue(pShape.getWidth());
		}

		public synchronized float getEntityAnchorY(IEntity pShape) {
			return y.getValue(pShape.getHeight());
		}

		public synchronized float getSiblingX(IEntity pShape) {
			return pShape.getX() + getEntityAnchorX(pShape);
		}

		public synchronized float getSiblingY(IEntity pShape) {
			return pShape.getY() + getEntityAnchorY(pShape);
		}
	}
	// ===========================================================
	// Fields
	// ===========================================================
	protected LinkedList<IEntity> mItems = new LinkedList<IEntity>();
	private final eAnchorPointXY mAnchorPoint;
	private final eAnchorPointXY mItemsAnchor;
	// ===========================================================
	// Constructors
	// ===========================================================
	public LayoutBase(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor) {
		this.mAnchorPoint = pLayoutAnchor;
		this.mItemsAnchor = pItemsAnchor;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public eAnchorPointXY getAnchor() {
		return mAnchorPoint;
	}

	public eAnchorPointXY getItemsAnchor() {
		return mItemsAnchor;
	}

	public void setItems(IEntity ... pItems) {
		mItems.clear();
		detachChildren();

		for (IEntity item : pItems) {
			mItems.add(item);
			attachChild(item);
		}
	}

	/**
	 * Sets a position of a given item taking into account given anchor point
	 * 
	 * @param pItem item to be placed
	 * @param x position in a TOP-LEFT coorfinates
	 * @param y position in a TOP-LEFT coorfinates
	 * @param pRequestedAnchor anchor point of given item
	 * @param will NOT work correctly for rotated items!
	 */
	public static void setPosition(final IEntity pItem, final float x, final float y, final eAnchorPointXY pRequestedAnchor) {
		final float requestedAnchorX = pRequestedAnchor.getEntityAnchorX(pItem);
		final float requestedAnchorY = pRequestedAnchor.getEntityAnchorY(pItem);

		final float defaultAnchorX = eAnchorPointXY.DEFAULT.getEntityAnchorX(pItem);
		final float defaultAnchorY = eAnchorPointXY.DEFAULT.getEntityAnchorY(pItem);

		final float offsetX = requestedAnchorX - defaultAnchorX;
		final float offsetY = requestedAnchorY - defaultAnchorY;

		Log.d("setItemPosition", x + "," + y + "->" + (x - offsetX) +","+ (y - offsetY) + "; w=" + pItem.getWidth() + "; h=" + pItem.getHeight() + "; pItem=" + pItem);
		pItem.setPosition(x + offsetX, y + offsetY);
	}

	/*
	 * some shortcuts fo setting a position without suppling AnchorPoint- appropriate anchor point
	 * is used depending on the name of the function called
	 */
	public static void setPositionTopLeft(final IEntity pItem, final float x, final float y) {
		setPosition(pItem, x, y, eAnchorPointXY.TOP_LEFT);
	}
	public static void setPositionTopMiddle(final IEntity pItem, final float x, final float y) {
		setPosition(pItem, x, y, eAnchorPointXY.TOP_MIDDLE);
	}
	public static void setPositionTopRight(final IEntity pItem, final float x, final float y) {
		setPosition(pItem, x, y, eAnchorPointXY.TOP_RIGHT);
	}
	public static void setPositionBottomLeft(final IEntity pItem, final float x, final float y) {
		setPosition(pItem, x, y, eAnchorPointXY.BOTTOM_LEFT);
	}
	public static void setPositionBottomMiddle(final IEntity pItem, final float x, final float y) {
		setPosition(pItem, x, y, eAnchorPointXY.BOTTOM_MIDDLE);
	}
	public static void setPositionBottomRight(final IEntity pItem, final float x, final float y) {
		setPosition(pItem, x, y, eAnchorPointXY.BOTTOM_RIGHT);
	}
	public static void setPositionCenterLeft(final IEntity pItem, final float x, final float y) {
		setPosition(pItem, x, y, eAnchorPointXY.CENTERED_LEFT);
	}
	public static void setPositionCenter(final IEntity pItem, final float x, final float y) {
		setPosition(pItem, x, y, eAnchorPointXY.CENTERED);
	}
	public static void setPositionCenterRight(final IEntity pItem, final float x, final float y) {
		setPosition(pItem, x, y, eAnchorPointXY.CENTERED_RIGHT);
	}


	public static void setSiblingItemPositionTopLeft(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setPositionTopLeft(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionTopMiddle(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setPositionTopMiddle(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionTopRight(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setPositionTopRight(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}

	public static void setSiblingItemPositionBottomLeft(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setPositionBottomLeft(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionBottomMiddle(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setPositionBottomMiddle(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionBottomRight(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setPositionBottomRight(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}

	public static void setSiblingItemPositionCenterLeft(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setPositionCenterLeft(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionCenter(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setPositionCenter(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionCenterRight(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setPositionCenterRight(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}

	public static void setSiblingItemPositionTopLeft(final IEntity pItem, final IEntity pSibling) {
		setPositionTopLeft(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionTopMiddle(final IEntity pItem, final IEntity pSibling) {
		setPositionTopMiddle(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionTopRight(final IEntity pItem, final IEntity pSibling) {
		setPositionTopRight(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}

	public static void setSiblingItemPositionBottomLeft(final IEntity pItem, final IEntity pSibling) {
		setPositionBottomLeft(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionBottomMiddle(final IEntity pItem, final IEntity pSibling) {
		setPositionBottomMiddle(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionBottomRight(final IEntity pItem, final IEntity pSibling) {
		setPositionBottomRight(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}

	public static void setSiblingItemPositionCenterLeft(final IEntity pItem, final IEntity pSibling) {
		setPositionCenterLeft(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionCenter(final IEntity pItem, final IEntity pSibling) {
		setPositionCenter(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionCenterRight(final IEntity pItem, final IEntity pSibling) {
		setPositionCenterRight(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}


	public static void setParentItemPositionTopLeft(final IEntity pItem, eAnchorPointXY pAnchor) {
		setPositionTopLeft(pItem, pAnchor.getEntityAnchorX((IEntity) pItem.getParent()), pAnchor.getEntityAnchorY((IEntity)pItem.getParent()));
	}
	public static void setParentItemPositionTopMiddle(final IEntity pItem, eAnchorPointXY pAnchor) {
		setPositionTopMiddle(pItem, pAnchor.getEntityAnchorX((IEntity) pItem.getParent()), pAnchor.getEntityAnchorY((IEntity)pItem.getParent()));
	}
	public static void setParentItemPositionTopRight(final IEntity pItem, final IEntity pParent, eAnchorPointXY pAnchor) {
		setPositionTopRight(pItem, pAnchor.getEntityAnchorX((IEntity) pItem.getParent()), pAnchor.getEntityAnchorY((IEntity)pItem.getParent()));
	}

	public static void setParentItemPositionBottomLeft(final IEntity pItem, eAnchorPointXY pAnchor) {
		setPositionBottomLeft(pItem, pAnchor.getEntityAnchorX((IEntity) pItem.getParent()), pAnchor.getEntityAnchorY((IEntity)pItem.getParent()));
	}
	public static void setParentItemPositionBottomMiddle(final IEntity pItem, eAnchorPointXY pAnchor) {
		setPositionBottomMiddle(pItem, pAnchor.getEntityAnchorX((IEntity) pItem.getParent()), pAnchor.getEntityAnchorY((IEntity)pItem.getParent()));
	}
	public static void setParentItemPositionBottomRight(final IEntity pItem, eAnchorPointXY pAnchor) {
		setPositionBottomRight(pItem, pAnchor.getEntityAnchorX((IEntity) pItem.getParent()), pAnchor.getEntityAnchorY((IEntity)pItem.getParent()));
	}

	public static void setParentItemPositionCenterLeft(final IEntity pItem, eAnchorPointXY pAnchor) {
		setPositionCenterLeft(pItem, pAnchor.getEntityAnchorX((IEntity) pItem.getParent()), pAnchor.getEntityAnchorY((IEntity)pItem.getParent()));
	}
	public static void setParentItemPositionCenter(final IEntity pItem, eAnchorPointXY pAnchor) {
		setPositionCenter(pItem, pAnchor.getEntityAnchorX((IEntity) pItem.getParent()), pAnchor.getEntityAnchorY((IEntity)pItem.getParent()));
	}
	public static void setParentItemPositionCenterRight(final IEntity pItem, eAnchorPointXY pAnchor) {
		setPositionCenterRight(pItem, pAnchor.getEntityAnchorX((IEntity) pItem.getParent()), pAnchor.getEntityAnchorY((IEntity)pItem.getParent()));
	}


	public static void setSceneItemPositionTopLeft(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setPositionTopLeft(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
	}
	public static void setSceneItemPositionTopMiddle(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setPositionTopMiddle(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
	}
	public static void setSceneItemPositionTopRight(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setPositionTopRight(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
	}

	public static void setSceneItemPositionBottomLeft(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setPositionBottomLeft(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
	}
	public static void setSceneItemPositionBottomMiddle(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setPositionBottomMiddle(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
	}
	public static void setSceneItemPositionBottomRight(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setPositionBottomRight(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
	}

	public static void setSceneItemPositionCenterLeft(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setPositionCenterLeft(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
	}
	public static void setSceneItemPositionCenter(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setPositionCenter(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
	}
	public static void setSceneItemPositionCenterRight(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setPositionCenterRight(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
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