package eu.nazgee.flower.pagerscene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;


public class PageMoverCamera<T extends IEntity> implements IPageMover<T> {
	protected final Camera mCamera;
	protected final float mStepPerPage;

	public PageMoverCamera(Camera mCamera, float pStepPerPage) {
		super();
		this.mCamera = mCamera;
		this.mStepPerPage = pStepPerPage;
	}

	@Override
	public void onProgressSwipe(final ScenePager<T> pScenePager, final IPage<T> pCurrentPage, float pSwipeDistanceTotal,
			float pSwipeDistanceDelta) {
		mCamera.offsetCenter(-pSwipeDistanceDelta, 0);
	}

	@Override
	public void onCompletedSwipe(final ScenePager<T> pScenePager, final IPage<T> pCurrentPage, int pNewPageIndex, int pOldPageIndex) {
		mCamera.setCenter(pNewPageIndex * mStepPerPage + mStepPerPage/2, mCamera.getCenterY());
	}

}
