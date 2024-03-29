package eu.nazgee.flower.base.pagerscene;

import java.util.LinkedList;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityParameterCallable;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
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

abstract public class ScenePager<T extends IEntity> extends SceneLoadable implements IOnSceneTouchListener, IScrollDetectorListener, IOnAreaTouchListener, IClickDetectorListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float SCROLL_MIN_SCREEN_WIDTH = 0.06f;
	private static final int CLICK_TIME_MAX = 200;
	// ===========================================================
	// Fields
	// ===========================================================
	private IItemClikedListener<T> mItemClikedListener;
	private final ClickDetector mClickDetector;
	private final SurfaceScrollDetector mSurfaceScrollDetector;
	private int mScrollDistanceX;
	private LinkedList<IPage<T>> mPages;
	private IPageMover<T> mPageMover;
	private final int mTurnPageThreshold;

	private int mCurrentPage;
	private T mCurrentlyTouchedItem;

	// ===========================================================
	// Constructors
	// ===========================================================
	public ScenePager(final float W, final float H,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final int pTurnPageThreshold) {
		super(W, H, pVertexBufferObjectManager);
		mSurfaceScrollDetector = new SurfaceScrollDetector(pTurnPageThreshold * SCROLL_MIN_SCREEN_WIDTH, this);
		mClickDetector =  new ClickDetector(CLICK_TIME_MAX, this);
		mTurnPageThreshold = pTurnPageThreshold;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public IPageMover<T> getPageMover() {
		return mPageMover;
	}

	public void setPageMover(final IPageMover<T> mPageMover) {
		this.mPageMover = mPageMover;
		callPageMoverOnCompleteSwipe(mCurrentPage);
	}
	public IItemClikedListener<T> getItemClikedListener() {
		return mItemClikedListener;
	}
	public void setItemClikedListener(final IItemClikedListener<T> mItemClikedListener) {
		this.mItemClikedListener = mItemClikedListener;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	/**
	 * Populate a graphical representation of an item which is to be used with this pager.
	 * All the parameters are helpers only- a hint for you
	 * @param pItem
	 * @param pItemOnPage
	 * @param pPage
	 * @return
	 */
	abstract protected T populateItem(int pItem, int pItemOnPage, int pPage);
	/**
	 * Populate a page with a given page number
	 * @param pPageNumber
	 * @return
	 */
	abstract protected IPage<T> populatePage(int pPageNumber);
	/**
	 * Attach given page (populated via populatePage) to the scene. You can
	 * attach it side-by-side, top-to-bottom, one over another, I do not care,
	 * as long as it matches IPageMover you use (set by setPageMover)
	 * @param pPage
	 * @param pPageNumber
	 */
	abstract protected void attachPage(final IPage<T> pPage, int pPageNumber);
	/**
	 * Return number of items you would like to have on your pager here.
	 * populateItem() will be called that many times.
	 * @return
	 */
	abstract protected int getItemsNumber();

	public void callOnEveryItem(IEntityParameterCallable pCallable) {
		for (IPage<T> page : mPages) {
			for (T item : page.getItems()) {
				pCallable.call(item);
			}
		}
	}

	@Override
	public void onLoadResources(final Engine e, final Context c) {

	}

	@Override
	public void onLoad(final Engine e, final Context c) {
		e.getCamera().reset();
		e.getCamera().setCenter(getW()/2, getH()/2);

		setOnAreaTouchListener(this);
		setOnSceneTouchListener(this);
		setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		mPages = preparePages();
		for (final IPage<T> page : mPages) {
			page.setCullingEnabled(true);
			for (final T item : page.getItems()) {
				registerTouchArea(item);
				item.setCullingEnabled(true);
			}
		}

		mCurrentPage = 0;
		callPageMoverOnCompleteSwipe(mCurrentPage);
	}

	@Override
	public void onUnload() {
		for (final IPage<T> page : mPages) {
			for (final T item : page.getItems()) {
				item.clearEntityModifiers();
				item.clearUpdateHandlers();
				unregisterTouchArea(item);
				item.detachSelf();
			}
			page.clearEntityModifiers();
			page.clearUpdateHandlers();
			detachChild(page);
		}
		mPages.clear();
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			final ITouchArea pTouchArea, final float pTouchAreaLocalX,
			final float pTouchAreaLocalY) {

		if (mPages.get(mCurrentPage).getItems().contains(pTouchArea)) {
			boolean ret = false;

			mCurrentlyTouchedItem = (T) pTouchArea;
			ret = mClickDetector.onTouchEvent(pSceneTouchEvent);

			return ret;
		}
		return false;
	}

	@Override
	public void onClick(final ClickDetector pClickDetector, final int pPointerID,
			final float pSceneX, final float pSceneY) {
		mSurfaceScrollDetector.reset();
		callPageMoverOnCompleteSwipe(mCurrentPage);
		callClickListener(mCurrentlyTouchedItem);
	}


	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		return mSurfaceScrollDetector.onTouchEvent(pSceneTouchEvent);
	}

	@Override
	public void onScrollStarted(final ScrollDetector pScollDetector, final int pPointerID,
			final float pDistanceX, final float pDistanceY) {
		Log.d(getClass().getSimpleName(), "started!");
		mScrollDistanceX = 0;
	}

	@Override
	public void onScroll(final ScrollDetector pScollDetector, final int pPointerID,
			final float pDistanceX, final float pDistanceY) {
		mScrollDistanceX += pDistanceX;
//		Log.d(getClass().getSimpleName(), "scrolling! " + pDistanceX + "/" + mScrollDistanceX);

		if (Math.abs(mScrollDistanceX) > mTurnPageThreshold) {
			mClickDetector.reset();
		}

		if (mPageMover != null) {
			mPageMover.onProgressSwipe(this, mPages.get(mCurrentPage), mScrollDistanceX, pDistanceX);
		}
	}

	@Override
	public void onScrollFinished(final ScrollDetector pScollDetector, final int pPointerID,
			final float pDistanceX, final float pDistanceY) {
		mScrollDistanceX += pDistanceX;
		Log.d(getClass().getSimpleName(), "finished!" + mScrollDistanceX);
		final int oldPage = mCurrentPage;

		if ((mScrollDistanceX > mTurnPageThreshold) && (mCurrentPage > 0)) {
			mCurrentPage--;
		} else if ((mScrollDistanceX < -mTurnPageThreshold)
				&& (mCurrentPage < mPages.size() - 1)) {
			mCurrentPage++;
		}
		mCurrentPage = MathUtils.bringToBounds(0, mPages.size(), mCurrentPage);
		callPageMoverOnCompleteSwipe(oldPage);
	}


	// ===========================================================
	// Methods
	// ===========================================================
	public IPage<T> getPrev(final IPage<T> pPage) {
		final int idx = mPages.indexOf(pPage);
		if (idx <= 0) {
			return null;
		}
		return mPages.get(idx - 1);
	}

	public IPage<T> getNext(final IPage<T> pPage) {
		final int idx = mPages.indexOf(pPage);
		if (idx < 0 || idx+1 == mPages.size()) {
			return null;
		}
		return mPages.get(idx + 1);
	}

	private void callPageMoverOnCompleteSwipe(final int oldPage) {
		if (mPageMover != null) {
			mPageMover.onCompletedSwipe(this, mPages.get(mCurrentPage), mCurrentPage, oldPage);
		}
	}

	protected void callClickListener(final T pItem) {
		if (getItemClikedListener() != null) {
			getItemClikedListener().onItemClicked(pItem);
		}
	}

	private LinkedList<IPage<T>> populatePages(final int pMinCapacity) {
		final LinkedList<IPage<T>> pages = new LinkedList<IPage<T>>();

		int pages_capacity = 0;
		do {
			pages.add(populatePage(pages.size()));
			pages_capacity += pages.getLast().getCapacity();
		} while (pages_capacity < pMinCapacity);

		return pages;
	}

	private LinkedList<IPage<T>> preparePages() {
		final LinkedList<IPage<T>> pages = populatePages(getItemsNumber());

		final int total_to_load = getItemsNumber();
		int total_loaded = 0;
		int current_page = 0;

		for (final IPage<T> page : pages) {
			final int left_to_load = total_to_load - total_loaded;
			final int this_page_to_load = Math.min(page.getCapacity(), left_to_load);

			final LinkedList<T> items = new LinkedList<T>();

			for (int i = 0; i < this_page_to_load; i++) {
				items.add(populateItem(total_loaded, i, current_page));
				total_loaded++;
			}

			page.setItems(items);
			attachPage(page, current_page);

			current_page++;
		}

		return pages;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public interface IItemClikedListener<T> {
		void onItemClicked(T pItem);
	}

}
