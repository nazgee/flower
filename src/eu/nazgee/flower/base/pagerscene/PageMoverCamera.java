package eu.nazgee.flower.base.pagerscene;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.IEntity;

import android.util.Log;


public class PageMoverCamera<T extends IEntity> implements IPageMover<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	protected final SmoothCamera mCamera;
	protected final float mStepPerPage;
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
		Log.d(getClass().getSimpleName(), "onCompletedSwipe(); pNewPageIndex=" + pNewPageIndex + "; mStepPerPage=" + mStepPerPage);
		mCamera.setCenter(pNewPageIndex * mStepPerPage + mStepPerPage/2, mCamera.getCenterY());
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
