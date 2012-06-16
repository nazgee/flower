package eu.nazgee.flower.base.pagerscene;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.IEntity;

import android.util.Log;


public class PageMoverCamera<T extends IEntity> implements IPageMover<T> {
	// ===========================================================
	// Constants
	// ===========================================================
	public enum ePageAlignment {
		PAGE_ALIGN_LEFT,
		PAGE_ALIGN_CENTER,
		PAGE_ALIGN_RIGHT
	}
	// ===========================================================
	// Fields
	// ===========================================================
	protected final SmoothCamera mCamera;
	private final float mStepPerPage;
	private ePageAlignment mPageAlignment = ePageAlignment.PAGE_ALIGN_LEFT;
	// ===========================================================
	// Constructors
	// ===========================================================
	public PageMoverCamera(SmoothCamera mCamera, float pStepPerPage) {
		super();
		this.mCamera = mCamera;
		this.mStepPerPage = pStepPerPage;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public ePageAlignment getPageAlignment() {
		return mPageAlignment;
	}

	public void setPageAlignment(ePageAlignment pPageAlignment) {
		this.mPageAlignment = pPageAlignment;
	}

	public float getStepPerPage() {
		return mStepPerPage;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onProgressSwipe(final ScenePager<T> pScenePager, final IPage<T> pCurrentPage, float pSwipeDistanceTotal,
			float pSwipeDistanceDelta) {
		Log.d(getClass().getSimpleName(), "onProgressSwipe(); pSwipeDistanceDelta=" + pSwipeDistanceDelta);
		mCamera.setCenterDirect(mCamera.getCenterX() - pSwipeDistanceDelta, mCamera.getCenterY());
	}

	@Override
	public void onCompletedSwipe(final ScenePager<T> pScenePager, final IPage<T> pCurrentPage, int pNewPageIndex, int pOldPageIndex) {
		Log.d(getClass().getSimpleName(), "onCompletedSwipe(); pNewPageIndex=" + pNewPageIndex + "; mStepPerPage=" + getStepPerPage());

		float offset = 0;
		switch (mPageAlignment) {
		case PAGE_ALIGN_LEFT:
			offset = mCamera.getWidth()/2;
			break;
		case PAGE_ALIGN_CENTER:
			offset = getStepPerPage()/2;
			break;
		case PAGE_ALIGN_RIGHT:
			offset = (mCamera.getWidth() - getStepPerPage())/2;
			break;
		}
		mCamera.setCenter(pNewPageIndex * getStepPerPage() + offset, mCamera.getCenterY());
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
