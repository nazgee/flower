package eu.nazgee.game.flower.activity.pager;

import org.andengine.engine.camera.Camera;


public class PageMoverCamera implements IPageMover {
	private final Camera mCamera;
	private final float mStepPerPage;

	public PageMoverCamera(Camera mCamera, float pStepPerPage) {
		super();
		this.mCamera = mCamera;
		this.mStepPerPage = pStepPerPage;
	}

	@Override
	public void onProgressSwipe(final IPage pCurrentPage, float pSwipeDistanceTotal,
			float pSwipeDistanceDelta) {
		mCamera.offsetCenter(-pSwipeDistanceDelta, 0);
	}

	@Override
	public void onCompletedSwipe(final IPage pCurrentPage, int pNewPageIndex, int pOldPageIndex) {
		mCamera.setCenter(pNewPageIndex * mStepPerPage + mStepPerPage/2, mCamera.getCenterY());
	}

}
