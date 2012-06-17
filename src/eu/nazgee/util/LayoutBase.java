package eu.nazgee.util;

import java.util.LinkedList;

import org.andengine.entity.Entity;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.util.Constants;



public class LayoutBase extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================
	public enum eRatio {
		MIN(0),
		MED(0.5f),
		MAX(1);

		public final float ratio;
		public float getValue(final float pSize) {
			return ratio * pSize;
		}

		private eRatio(float align) {
			this.ratio = align;
		}
	}

	public enum eAnchorPointXY {
		TOP_LEFT(eRatio.MIN, eRatio.MIN),
		TOP_MIDDLE(eRatio.MED, eRatio.MIN),
		TOP_RIGHT(eRatio.MAX, eRatio.MIN),
		CENTERED_LEFT(eRatio.MIN, eRatio.MED),
		CENTERED(eRatio.MED, eRatio.MED),
		CENTERED_RIGHT(eRatio.MAX, eRatio.MED),
		BOTTOM_LEFT(eRatio.MIN, eRatio.MAX),
		BOTTOM_MIDDLE(eRatio.MED, eRatio.MAX),
		BOTTOM_RIGHT(eRatio.MAX, eRatio.MAX);

		public final eRatio x;
		public final eRatio y;
		private final float[] mReuse = new float[2];

		private eAnchorPointXY(eRatio pAnchorPointX, eRatio pAnchorPointY) {
			x = pAnchorPointX;
			y = pAnchorPointY;
		}

		public synchronized float getSceneX(IAreaShape pShape) {
			pShape.convertLocalToSceneCoordinates(x.getValue(pShape.getWidth()), x.getValue(pShape.getHeight()), mReuse);
			return mReuse[Constants.VERTEX_INDEX_X];
		}

		public synchronized float getSceneY(IAreaShape pShape) {
			pShape.convertLocalToSceneCoordinates(x.getValue(pShape.getWidth()), y.getValue(pShape.getHeight()), mReuse);
			return mReuse[Constants.VERTEX_INDEX_Y];
		}

		public synchronized float getLocalX(IAreaShape pShape) {
			return x.getValue(pShape.getWidth());
		}

		public synchronized float getLocalY(IAreaShape pShape) {
			return y.getValue(pShape.getHeight());
		}

		public synchronized float getSiblingX(IAreaShape pShape) {
			return pShape.getX() + getLocalX(pShape);
		}

		public synchronized float getSiblingY(IAreaShape pShape) {
			return pShape.getY() + getLocalY(pShape);
		}
	}
	// ===========================================================
	// Fields
	// ===========================================================
	protected LinkedList<IAreaShape> mItems = new LinkedList<IAreaShape>();
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

	public void setItems(IAreaShape ... pItems) {
		mItems.clear();
		detachChildren();

		for (IAreaShape item : pItems) {
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
	 * @param pAnchor anchor point of given item
	 */
	public static void setItemPositionTopLeft(final IAreaShape pItem, final float x, final float y, final eAnchorPointXY pAnchor) {
		setItemPosition(pItem, x, y, pAnchor, eRatio.MIN, eRatio.MIN);
	}
	public static void setItemPositionTopMiddle(final IAreaShape pItem, final float x, final float y, final eAnchorPointXY pAnchor) {
		setItemPosition(pItem, x, y, pAnchor, eRatio.MED, eRatio.MIN);
	}
	public static void setItemPositionTopRight(final IAreaShape pItem, final float x, final float y, final eAnchorPointXY pAnchor) {
		setItemPosition(pItem, x, y, pAnchor, eRatio.MAX, eRatio.MIN);
	}

	public static void setItemPositionBottomLeft(final IAreaShape pItem, final float x, final float y, final eAnchorPointXY pAnchor) {
		setItemPosition(pItem, x, y, pAnchor, eRatio.MIN, eRatio.MAX);
	}
	public static void setItemPositionBottomMiddle(final IAreaShape pItem, final float x, final float y, final eAnchorPointXY pAnchor) {
		setItemPosition(pItem, x, y, pAnchor, eRatio.MED, eRatio.MAX);
	}
	public static void setItemPositionBottomRight(final IAreaShape pItem, final float x, final float y, final eAnchorPointXY pAnchor) {
		setItemPosition(pItem, x, y, pAnchor, eRatio.MAX, eRatio.MAX);
	}

	public static void setItemPositionCenterLeft(final IAreaShape pItem, final float x, final float y, final eAnchorPointXY pAnchor) {
		setItemPosition(pItem, x, y, pAnchor, eRatio.MIN, eRatio.MED);
	}
	public static void setItemPositionCenter(final IAreaShape pItem, final float x, final float y, final eAnchorPointXY pAnchor) {
		setItemPosition(pItem, x, y, pAnchor, eRatio.MED, eRatio.MED);
	}
	public static void setItemPositionCenterRight(final IAreaShape pItem, final float x, final float y, final eAnchorPointXY pAnchor) {
		setItemPosition(pItem, x, y, pAnchor, eRatio.MAX, eRatio.MED);
	}

	public static void setItemPosition(final IAreaShape pItem, final float x, final float y, final eAnchorPointXY pAnchor, eRatio pHorizontal, eRatio pVertical) {
		final float offX = pAnchor.x.getValue(pItem.getWidth()) - pHorizontal.getValue(pItem.getWidth());
		final float offY = pAnchor.y.getValue(pItem.getHeight()) - pVertical.getValue(pItem.getHeight());

		pItem.setPosition(x + offX, y + offY);
	}

	public static void setItemPositionTopLeft(final IAreaShape pItem, final float x, final float y) {
		setItemPositionTopLeft(pItem, x, y, eAnchorPointXY.TOP_LEFT);
	}
	public static void setItemPositionTopMiddle(final IAreaShape pItem, final float x, final float y) {
		setItemPositionTopMiddle(pItem, x, y, eAnchorPointXY.TOP_LEFT);
	}
	public static void setItemPositionTopRight(final IAreaShape pItem, final float x, final float y) {
		setItemPositionTopRight(pItem, x, y, eAnchorPointXY.TOP_LEFT);
	}

	public static void setItemPositionBottomLeft(final IAreaShape pItem, final float x, final float y) {
		setItemPositionBottomLeft(pItem, x, y, eAnchorPointXY.TOP_LEFT);
	}
	public static void setItemPositionBottomMiddle(final IAreaShape pItem, final float x, final float y) {
		setItemPositionBottomMiddle(pItem, x, y, eAnchorPointXY.TOP_LEFT);
	}
	public static void setItemPositionBottomRight(final IAreaShape pItem, final float x, final float y) {
		setItemPositionBottomRight(pItem, x, y, eAnchorPointXY.TOP_LEFT);
	}

	public static void setItemPositionCenterLeft(final IAreaShape pItem, final float x, final float y) {
		setItemPositionCenterLeft(pItem, x, y, eAnchorPointXY.TOP_LEFT);
	}
	public static void setItemPositionCenter(final IAreaShape pItem, final float x, final float y) {
		setItemPositionCenter(pItem, x, y, eAnchorPointXY.TOP_LEFT);
	}
	public static void setItemPositionCenterRight(final IAreaShape pItem, final float x, final float y) {
		setItemPositionCenterRight(pItem, x, y, eAnchorPointXY.TOP_LEFT);
	}


	public static void setSiblingItemPositionTopLeft(final IAreaShape pItem, final IAreaShape pSibling, eAnchorPointXY pAnchor) {
		setItemPositionTopLeft(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling), eAnchorPointXY.TOP_LEFT);
	}
	public static void setSiblingItemPositionTopMiddle(final IAreaShape pItem, final IAreaShape pSibling, eAnchorPointXY pAnchor) {
		setItemPositionTopMiddle(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling), eAnchorPointXY.TOP_LEFT);
	}
	public static void setSiblingItemPositionTopRight(final IAreaShape pItem, final IAreaShape pSibling, eAnchorPointXY pAnchor) {
		setItemPositionTopRight(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling), eAnchorPointXY.TOP_LEFT);
	}

	public static void setSiblingItemPositionBottomLeft(final IAreaShape pItem, final IAreaShape pSibling, eAnchorPointXY pAnchor) {
		setItemPositionBottomLeft(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling), eAnchorPointXY.TOP_LEFT);
	}
	public static void setSiblingItemPositionBottomMiddle(final IAreaShape pItem, final IAreaShape pSibling, eAnchorPointXY pAnchor) {
		setItemPositionBottomMiddle(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling), eAnchorPointXY.TOP_LEFT);
	}
	public static void setSiblingItemPositionBottomRight(final IAreaShape pItem, final IAreaShape pSibling, eAnchorPointXY pAnchor) {
		setItemPositionBottomRight(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling), eAnchorPointXY.TOP_LEFT);
	}

	public static void setSiblingItemPositionCenterLeft(final IAreaShape pItem, final IAreaShape pSibling, eAnchorPointXY pAnchor) {
		setItemPositionCenterLeft(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling), eAnchorPointXY.TOP_LEFT);
	}
	public static void setSiblingItemPositionCenter(final IAreaShape pItem, final IAreaShape pSibling, eAnchorPointXY pAnchor) {
		setItemPositionCenter(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling), eAnchorPointXY.TOP_LEFT);
	}
	public static void setSiblingItemPositionCenterRight(final IAreaShape pItem, final IAreaShape pSibling, eAnchorPointXY pAnchor) {
		setItemPositionCenterRight(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling), eAnchorPointXY.TOP_LEFT);
	}


	public static void setParentItemPositionTopLeft(final IAreaShape pItem, eAnchorPointXY pAnchor) {
		setItemPositionTopLeft(pItem, pAnchor.getLocalX((IAreaShape) pItem.getParent()), pAnchor.getLocalY((IAreaShape)pItem.getParent()), eAnchorPointXY.TOP_LEFT);
	}
	public static void setParentItemPositionTopMiddle(final IAreaShape pItem, eAnchorPointXY pAnchor) {
		setItemPositionTopMiddle(pItem, pAnchor.getLocalX((IAreaShape) pItem.getParent()), pAnchor.getLocalY((IAreaShape)pItem.getParent()), eAnchorPointXY.TOP_LEFT);
	}
	public static void setParentItemPositionTopRight(final IAreaShape pItem, final IAreaShape pParent, eAnchorPointXY pAnchor) {
		setItemPositionTopRight(pItem, pAnchor.getLocalX((IAreaShape) pItem.getParent()), pAnchor.getLocalY((IAreaShape)pItem.getParent()), eAnchorPointXY.TOP_LEFT);
	}

	public static void setParentItemPositionBottomLeft(final IAreaShape pItem, eAnchorPointXY pAnchor) {
		setItemPositionBottomLeft(pItem, pAnchor.getLocalX((IAreaShape) pItem.getParent()), pAnchor.getLocalY((IAreaShape)pItem.getParent()), eAnchorPointXY.TOP_LEFT);
	}
	public static void setParentItemPositionBottomMiddle(final IAreaShape pItem, eAnchorPointXY pAnchor) {
		setItemPositionBottomMiddle(pItem, pAnchor.getLocalX((IAreaShape) pItem.getParent()), pAnchor.getLocalY((IAreaShape)pItem.getParent()), eAnchorPointXY.TOP_LEFT);
	}
	public static void setParentItemPositionBottomRight(final IAreaShape pItem, eAnchorPointXY pAnchor) {
		setItemPositionBottomRight(pItem, pAnchor.getLocalX((IAreaShape) pItem.getParent()), pAnchor.getLocalY((IAreaShape)pItem.getParent()), eAnchorPointXY.TOP_LEFT);
	}

	public static void setParentItemPositionCenterLeft(final IAreaShape pItem, eAnchorPointXY pAnchor) {
		setItemPositionCenterLeft(pItem, pAnchor.getLocalX((IAreaShape) pItem.getParent()), pAnchor.getLocalY((IAreaShape)pItem.getParent()), eAnchorPointXY.TOP_LEFT);
	}
	public static void setParentItemPositionCenter(final IAreaShape pItem, eAnchorPointXY pAnchor) {
		setItemPositionCenter(pItem, pAnchor.getLocalX((IAreaShape) pItem.getParent()), pAnchor.getLocalY((IAreaShape)pItem.getParent()), eAnchorPointXY.TOP_LEFT);
	}
	public static void setParentItemPositionCenterRight(final IAreaShape pItem, eAnchorPointXY pAnchor) {
		setItemPositionCenterRight(pItem, pAnchor.getLocalX((IAreaShape) pItem.getParent()), pAnchor.getLocalY((IAreaShape)pItem.getParent()), eAnchorPointXY.TOP_LEFT);
	}


	public static void setSceneItemPositionTopLeft(final IAreaShape pItem, final IAreaShape pOther, eAnchorPointXY pAnchor) {
		setItemPositionTopLeft(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther), eAnchorPointXY.TOP_LEFT);
	}
	public static void setSceneItemPositionTopMiddle(final IAreaShape pItem, final IAreaShape pOther, eAnchorPointXY pAnchor) {
		setItemPositionTopMiddle(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther), eAnchorPointXY.TOP_LEFT);
	}
	public static void setSceneItemPositionTopRight(final IAreaShape pItem, final IAreaShape pOther, eAnchorPointXY pAnchor) {
		setItemPositionTopRight(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther), eAnchorPointXY.TOP_LEFT);
	}

	public static void setSceneItemPositionBottomLeft(final IAreaShape pItem, final IAreaShape pOther, eAnchorPointXY pAnchor) {
		setItemPositionBottomLeft(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther), eAnchorPointXY.TOP_LEFT);
	}
	public static void setSceneItemPositionBottomMiddle(final IAreaShape pItem, final IAreaShape pOther, eAnchorPointXY pAnchor) {
		setItemPositionBottomMiddle(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther), eAnchorPointXY.TOP_LEFT);
	}
	public static void setSceneItemPositionBottomRight(final IAreaShape pItem, final IAreaShape pOther, eAnchorPointXY pAnchor) {
		setItemPositionBottomRight(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther), eAnchorPointXY.TOP_LEFT);
	}

	public static void setSceneItemPositionCenterLeft(final IAreaShape pItem, final IAreaShape pOther, eAnchorPointXY pAnchor) {
		setItemPositionCenterLeft(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther), eAnchorPointXY.TOP_LEFT);
	}
	public static void setSceneItemPositionCenter(final IAreaShape pItem, final IAreaShape pOther, eAnchorPointXY pAnchor) {
		setItemPositionCenter(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther), eAnchorPointXY.TOP_LEFT);
	}
	public static void setSceneItemPositionCenterRight(final IAreaShape pItem, final IAreaShape pOther, eAnchorPointXY pAnchor) {
		setItemPositionCenterRight(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther), eAnchorPointXY.TOP_LEFT);
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