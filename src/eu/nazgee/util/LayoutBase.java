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
		MIN(-0.5f),
		MED(0.0f),
		MAX(0.5f);

		public final float ratio;
		public float getValue(final float pSize) {
			return ratio * pSize;
		}

		private eRatio(float align) {
			this.ratio = align;
		}
	}

	public enum eAnchorPointXY {
		TOP_LEFT(eRatio.MIN, eRatio.MAX),
		TOP_MIDDLE(eRatio.MED, eRatio.MAX),
		TOP_RIGHT(eRatio.MAX, eRatio.MAX),
		CENTERED_LEFT(eRatio.MIN, eRatio.MED),
		CENTERED(eRatio.MED, eRatio.MED),
		CENTERED_RIGHT(eRatio.MAX, eRatio.MED),
		BOTTOM_LEFT(eRatio.MIN, eRatio.MIN),
		BOTTOM_MIDDLE(eRatio.MED, eRatio.MIN),
		BOTTOM_RIGHT(eRatio.MAX, eRatio.MIN),
		DEFAULT(eRatio.MED, eRatio.MED);

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

		public synchronized float getLocalX(IEntity pShape) {
			return x.getValue(pShape.getWidth());
		}

		public synchronized float getLocalY(IEntity pShape) {
			return y.getValue(pShape.getHeight());
		}

		public synchronized float getSiblingX(IEntity pShape) {
			return pShape.getX() + getLocalX(pShape);
		}

		public synchronized float getSiblingY(IEntity pShape) {
			return pShape.getY() + getLocalY(pShape);
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
	 * @param pAnchor anchor point of given item
	 */
	public static void setItemPosition(final IEntity pItem, final float x, final float y, final eAnchorPointXY pAnchor) {
		final float offX = (pAnchor.x.getValue(pItem.getWidth()));
		final float offY = (pAnchor.y.getValue(pItem.getHeight()));

		Log.d("setItemPosition", "x,y" + x + "," + y + "=>" + (x - offX) +","+ (y - offY) + "; w=" + pItem.getWidth() + "; h=" + pItem.getHeight() + "; pItem=" + pItem);
		pItem.setPosition(x - offX, y - offY);
	}

	public static void setItemPositionTopLeft(final IEntity pItem, final float x, final float y) {
		setItemPosition(pItem, x, y, eAnchorPointXY.TOP_LEFT);
	}
	public static void setItemPositionTopMiddle(final IEntity pItem, final float x, final float y) {
		setItemPosition(pItem, x, y, eAnchorPointXY.TOP_MIDDLE);
	}
	public static void setItemPositionTopRight(final IEntity pItem, final float x, final float y) {
		setItemPosition(pItem, x, y, eAnchorPointXY.TOP_RIGHT);
	}

	public static void setItemPositionBottomLeft(final IEntity pItem, final float x, final float y) {
		setItemPosition(pItem, x, y, eAnchorPointXY.BOTTOM_LEFT);
	}
	public static void setItemPositionBottomMiddle(final IEntity pItem, final float x, final float y) {
		setItemPosition(pItem, x, y, eAnchorPointXY.BOTTOM_MIDDLE);
	}
	public static void setItemPositionBottomRight(final IEntity pItem, final float x, final float y) {
		setItemPosition(pItem, x, y, eAnchorPointXY.BOTTOM_RIGHT);
	}

	public static void setItemPositionCenterLeft(final IEntity pItem, final float x, final float y) {
		setItemPosition(pItem, x, y, eAnchorPointXY.CENTERED_LEFT);
	}
	public static void setItemPositionCenter(final IEntity pItem, final float x, final float y) {
		setItemPosition(pItem, x, y, eAnchorPointXY.CENTERED);
	}
	public static void setItemPositionCenterRight(final IEntity pItem, final float x, final float y) {
		setItemPosition(pItem, x, y, eAnchorPointXY.CENTERED_RIGHT);
	}


	public static void setSiblingItemPositionTopLeft(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setItemPositionTopLeft(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionTopMiddle(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setItemPositionTopMiddle(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionTopRight(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setItemPositionTopRight(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}

	public static void setSiblingItemPositionBottomLeft(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setItemPositionBottomLeft(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionBottomMiddle(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setItemPositionBottomMiddle(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionBottomRight(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setItemPositionBottomRight(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}

	public static void setSiblingItemPositionCenterLeft(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setItemPositionCenterLeft(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionCenter(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setItemPositionCenter(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionCenterRight(final IEntity pItem, final IEntity pSibling, eAnchorPointXY pAnchor) {
		setItemPositionCenterRight(pItem, pAnchor.getSiblingX(pSibling), pAnchor.getSiblingY(pSibling));
	}

	public static void setSiblingItemPositionTopLeft(final IEntity pItem, final IEntity pSibling) {
		setItemPositionTopLeft(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionTopMiddle(final IEntity pItem, final IEntity pSibling) {
		setItemPositionTopMiddle(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionTopRight(final IEntity pItem, final IEntity pSibling) {
		setItemPositionTopRight(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}

	public static void setSiblingItemPositionBottomLeft(final IEntity pItem, final IEntity pSibling) {
		setItemPositionBottomLeft(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionBottomMiddle(final IEntity pItem, final IEntity pSibling) {
		setItemPositionBottomMiddle(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionBottomRight(final IEntity pItem, final IEntity pSibling) {
		setItemPositionBottomRight(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}

	public static void setSiblingItemPositionCenterLeft(final IEntity pItem, final IEntity pSibling) {
		setItemPositionCenterLeft(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionCenter(final IEntity pItem, final IEntity pSibling) {
		setItemPositionCenter(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}
	public static void setSiblingItemPositionCenterRight(final IEntity pItem, final IEntity pSibling) {
		setItemPositionCenterRight(pItem, eAnchorPointXY.DEFAULT.getSiblingX(pSibling), eAnchorPointXY.DEFAULT.getSiblingY(pSibling));
	}


	public static void setParentItemPositionTopLeft(final IEntity pItem, eAnchorPointXY pAnchor) {
		setItemPositionTopLeft(pItem, pAnchor.getLocalX((IEntity) pItem.getParent()), pAnchor.getLocalY((IEntity)pItem.getParent()));
	}
	public static void setParentItemPositionTopMiddle(final IEntity pItem, eAnchorPointXY pAnchor) {
		setItemPositionTopMiddle(pItem, pAnchor.getLocalX((IEntity) pItem.getParent()), pAnchor.getLocalY((IEntity)pItem.getParent()));
	}
	public static void setParentItemPositionTopRight(final IEntity pItem, final IEntity pParent, eAnchorPointXY pAnchor) {
		setItemPositionTopRight(pItem, pAnchor.getLocalX((IEntity) pItem.getParent()), pAnchor.getLocalY((IEntity)pItem.getParent()));
	}

	public static void setParentItemPositionBottomLeft(final IEntity pItem, eAnchorPointXY pAnchor) {
		setItemPositionBottomLeft(pItem, pAnchor.getLocalX((IEntity) pItem.getParent()), pAnchor.getLocalY((IEntity)pItem.getParent()));
	}
	public static void setParentItemPositionBottomMiddle(final IEntity pItem, eAnchorPointXY pAnchor) {
		setItemPositionBottomMiddle(pItem, pAnchor.getLocalX((IEntity) pItem.getParent()), pAnchor.getLocalY((IEntity)pItem.getParent()));
	}
	public static void setParentItemPositionBottomRight(final IEntity pItem, eAnchorPointXY pAnchor) {
		setItemPositionBottomRight(pItem, pAnchor.getLocalX((IEntity) pItem.getParent()), pAnchor.getLocalY((IEntity)pItem.getParent()));
	}

	public static void setParentItemPositionCenterLeft(final IEntity pItem, eAnchorPointXY pAnchor) {
		setItemPositionCenterLeft(pItem, pAnchor.getLocalX((IEntity) pItem.getParent()), pAnchor.getLocalY((IEntity)pItem.getParent()));
	}
	public static void setParentItemPositionCenter(final IEntity pItem, eAnchorPointXY pAnchor) {
		setItemPositionCenter(pItem, pAnchor.getLocalX((IEntity) pItem.getParent()), pAnchor.getLocalY((IEntity)pItem.getParent()));
	}
	public static void setParentItemPositionCenterRight(final IEntity pItem, eAnchorPointXY pAnchor) {
		setItemPositionCenterRight(pItem, pAnchor.getLocalX((IEntity) pItem.getParent()), pAnchor.getLocalY((IEntity)pItem.getParent()));
	}


	public static void setSceneItemPositionTopLeft(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setItemPositionTopLeft(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
	}
	public static void setSceneItemPositionTopMiddle(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setItemPositionTopMiddle(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
	}
	public static void setSceneItemPositionTopRight(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setItemPositionTopRight(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
	}

	public static void setSceneItemPositionBottomLeft(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setItemPositionBottomLeft(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
	}
	public static void setSceneItemPositionBottomMiddle(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setItemPositionBottomMiddle(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
	}
	public static void setSceneItemPositionBottomRight(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setItemPositionBottomRight(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
	}

	public static void setSceneItemPositionCenterLeft(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setItemPositionCenterLeft(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
	}
	public static void setSceneItemPositionCenter(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setItemPositionCenter(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
	}
	public static void setSceneItemPositionCenterRight(final IEntity pItem, final IEntity pOther, eAnchorPointXY pAnchor) {
		setItemPositionCenterRight(pItem, pAnchor.getSceneX(pOther), pAnchor.getSceneY(pOther));
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