package eu.nazgee.flower.pagerscene;

import org.andengine.engine.camera.Camera;


public class PageMoverCamera implements IPageMover {
	protected final Camera mCamera;
	protected final float mStepPerPage;

	public PageMoverCamera(Camera mCamera, float pStepPerPage) {
		super();
		this.mCamera = mCamera;
		this.mStepPerPage = pStepPerPage;
	}

	@Override
	public void onProgressSwipe(final ScenePager pScenePager, final IPage pCurrentPage, float pSwipeDistanceTotal,
			float pSwipeDistanceDelta) {
		mCamera.offsetCenter(-pSwipeDistanceDelta, 0);
	}

	@Override
	public void onCompletedSwipe(final ScenePager pScenePager, final IPage pCurrentPage, int pNewPageIndex, int pOldPageIndex) {
		mCamera.setCenter(pNewPageIndex * mStepPerPage + mStepPerPage/2, mCamera.getCenterY());
	}

}
