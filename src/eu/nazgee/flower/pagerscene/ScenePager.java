package eu.nazgee.flower.pagerscene;

import java.util.LinkedList;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import android.content.Context;
import android.util.Log;
import eu.nazgee.game.utils.scene.SceneLoadable;

abstract public class ScenePager extends SceneLoadable implements IOnSceneTouchListener, IScrollDetectorListener, IOnAreaTouchListener, IClickDetectorListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private final static int TURN_PAGE_DISTANCE = 150;
	// ===========================================================
	// Fields
	// ===========================================================
	private IItemClikedListener mItemClikedListener;
	private final ClickDetector mClickDetector = new ClickDetector(this);
	private final SurfaceScrollDetector mSurfaceScrollDetector = new SurfaceScrollDetector(this);
	private int mScrollDistanceX;
	private LinkedList<IPage> mPages;
	private IPageMover mPageMover;

	private int mCurrentPage;

	// ===========================================================
	// Constructors
	// ===========================================================
	public ScenePager(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pVertexBufferObjectManager);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public IPageMover getPageMover() {
		return mPageMover;
	}

	public void setPageMover(IPageMover mPageMover) {
		this.mPageMover = mPageMover;
	}
	public IItemClikedListener getItemClikedListener() {
		return mItemClikedListener;
	}
	public void setItemClikedListener(IItemClikedListener mItemClikedListener) {
		this.mItemClikedListener = mItemClikedListener;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	abstract protected LinkedList<IPage> populatePages();
	public interface IItemClikedListener {
		void onItemClicked(IEntity pItem);
	}

	@Override
	public void onLoadResources(Engine e, Context c) {

	}

	@Override
	public void onLoad(Engine e, Context c) {
		e.registerUpdateHandler(new FPSLogger());

		setBackground(new Background(0.9f, 0f, 0f));

		setOnAreaTouchListener(this);
		setOnSceneTouchListener(this);
		setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		mPages = populatePages();
		for (IPage page : mPages) {
			for (IEntity item : page.getItems()) {
				registerTouchArea((ITouchArea) item);
			}
		}
	}

	@Override
	public void onUnload() {
	}

	IEntity mCurrentlyTouchedItem;
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		if (mPages.get(mCurrentPage).getItems().contains(pTouchArea)) {
			mCurrentlyTouchedItem = (IEntity) pTouchArea;
			return mClickDetector.onTouchEvent(pSceneTouchEvent);
//			mClickDetector.onTouchEvent(pSceneTouchEvent);
		}
		return false;
	}

	@Override
	public void onClick(ClickDetector pClickDetector, int pPointerID,
			float pSceneX, float pSceneY) {
		mCurrentlyTouchedItem.registerEntityModifier(new RotationByModifier(1, 180));
		if (getItemClikedListener() != null) {
			getItemClikedListener().onItemClicked(mCurrentlyTouchedItem);
		}
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
//		if (mClickDetector.onSceneTouchEvent(this, pSceneTouchEvent))
//			return true;

		return mSurfaceScrollDetector.onTouchEvent(pSceneTouchEvent);
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		mScrollDistanceX = 0;
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		mScrollDistanceX += pDistanceX;
		if (mPageMover != null) {
			mPageMover.onProgressSwipe(mPages.get(mCurrentPage), mScrollDistanceX, pDistanceX);
		}
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		Log.d(getClass().getSimpleName(), "page: " + mCurrentPage);

		int oldPage = mCurrentPage;

		if ((mScrollDistanceX > TURN_PAGE_DISTANCE) && (mCurrentPage > 0)) {
			Log.d(getClass().getSimpleName(), "page dec");
			mCurrentPage--;
		} else if ((mScrollDistanceX < -TURN_PAGE_DISTANCE)
				&& (mCurrentPage < mPages.size() - 1)) {
			Log.d(getClass().getSimpleName(), "page inc");
			mCurrentPage++;
		}
		mCurrentPage = MathUtils.bringToBounds(0, mPages.size(), mCurrentPage);
		if (mPageMover != null) {
			mPageMover.onCompletedSwipe(mPages.get(mCurrentPage), mCurrentPage, oldPage);
		}
	}
	// ===========================================================
	// Methods
	// ===========================================================

}
