package eu.nazgee.flower.activity.game.scene.game;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;

import eu.nazgee.flower.pool.cloud.Cloud;
import eu.nazgee.flower.pool.cloud.CloudPool;
import eu.nazgee.flower.pool.cloud.CloudPool.CloudItem;
import eu.nazgee.flower.pool.waterdrop.WaterDrop;
import eu.nazgee.flower.pool.waterdrop.WaterDrop.IWaterDropListener;
import eu.nazgee.flower.pool.waterdrop.WaterDropPool;
import eu.nazgee.flower.pool.waterdrop.WaterDropPool.WaterDropItem;
import eu.nazgee.flower.pool.watersplash.WaterSplashPool;
import eu.nazgee.flower.pool.watersplash.WaterSplashPool.WaterSplashItem;

public class CloudLayer extends Entity{
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	final Random rand = new Random();
	LinkedList<Cloud> mClouds = new LinkedList<Cloud>();
	final CloudPool mCloudPool;
	final WaterDropPool mDropPool;
	final WaterSplashPool mSplashPool;
	private final float mW;
	private final float mH;
	private final float mAvgSpeed;
	private final float mAvgTime;
	private final float mAvgDistance;
	private final float mVariationSpeed;
	private final float mVariationTime;
	private final Sky mSky;

	private IWaterDropListener mWaterDropListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CloudLayer(float pX, float pY, final float W, final float H, 
			float pAvgSpeed, float pAvgTime,
			float pVariationSpeed, float pVariationTime, final int pCloudsNumber,
			final Sky pSky,
			ITiledTextureRegion pCloudTexture, ITextureRegion pWaterDropTexture, ITiledTextureRegion pWaterSplashTexture,
			EntityDetachRunnablePoolUpdateHandler pDetacher,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY);
		mW = W;
		mH = H;
		mAvgSpeed = pAvgSpeed;
		mAvgTime = pAvgTime;
		mSky = pSky;
		mAvgDistance = pAvgSpeed * pAvgTime;
		mVariationSpeed = pVariationSpeed;
		mVariationTime = pVariationTime;
		mCloudPool = new CloudPool(pCloudTexture, pDetacher, pVertexBufferObjectManager);
		mDropPool = new WaterDropPool(pWaterDropTexture, pDetacher, pVertexBufferObjectManager);
		mSplashPool = new WaterSplashPool(pWaterSplashTexture, pDetacher, pVertexBufferObjectManager);

		/*
		 * Smoothly ramp up number of clouds on layer
		 */
		TimerHandler starter = new TimerHandler(mAvgTime/pCloudsNumber, new ITimerCallback() {
			int started = 0;
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {

				launchNewCloud();
				started++;
				if (started < pCloudsNumber) {
					pTimerHandler.reset();
				}
			}
		});
		registerUpdateHandler(starter);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public IWaterDropListener getWaterDropListener() {
		return mWaterDropListener;
	}

	public void setWaterDropListener(IWaterDropListener pWaterDropListener) {
		this.mWaterDropListener = pWaterDropListener;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public interface IRainDropListener {
		public void onRainDrop(WaterDrop pWaterDrop);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	private final float randomize(final float avg, final float var) {
		final float variation = var * avg;
		return avg - variation + rand.nextFloat() * 2 * variation; 
	}

	private void launchNewCloud() {
		final CloudItem item = mCloudPool.obtainPoolItem();
		final Cloud cloud = item.getEntity();
		registerCloudAndSortByHeights(cloud);

		final float x = randomize(mW, mAvgDistance/2/mW) - mAvgDistance;
		final float y = rand.nextFloat() * mH;
		final float time = randomize(mAvgTime, mVariationTime);
		final float speed = randomize(mAvgSpeed, mVariationSpeed);

		Cloud.CloudListener listener = cloud.getTravelListener();
		if (listener == null) {
			listener = new CloudListener();
		}

		cloud.registerUpdateHandler(new TimerHandler(time/2, new RainMain(cloud)));
		cloud.travel(x, y, speed*time, time, listener);
	}

	public Cloud getHighestCloudAtX(final float pX, final float pLowestY) {
		for (Cloud cloud : mClouds) {
			final float pos[] = cloud.getSceneCenterCoordinates();
			if (pos[Constants.VERTEX_INDEX_Y] > pLowestY &&
					cloud.contains(pX, pos[Constants.VERTEX_INDEX_Y])) {
				return cloud;
			}
		}
		return null;
	}

	private void registerCloudAndSortByHeights(Cloud cloud) {
		synchronized (mClouds) {
			mClouds.add(cloud);
			Collections.sort(mClouds, new ComparatorHeight());
		}

		// XXX run it in a runnable?
		attachChild(cloud);
	}

	private void unregiterCloud(Cloud cloud) {
		synchronized (mClouds) {
			mClouds.remove(cloud);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	/**
	 * Used to sort clouds by theirs height
	 * @author nazgee
	 *
	 */
	public static class ComparatorHeight implements Comparator<Cloud> {
		@Override
		public int compare(Cloud lhs, Cloud rhs) {
			final float posL[] = lhs.getSceneCenterCoordinates();
			final float posR[] = rhs.getSceneCenterCoordinates();
			return (int) (posL[Constants.VERTEX_INDEX_Y] - posR[Constants.VERTEX_INDEX_Y]);
		}

	}

	/**
	 * This class is used to cause rain to fall. It starts a raind drop and
	 * splash animation
	 * @author nazgee
	 *
	 */
	class RainMain implements ITimerCallback {
		private final Cloud mCloud;
		float mPos[];
		public RainMain(Cloud mCloud) {
			this.mCloud = mCloud;
		}
		@Override
		public void onTimePassed(TimerHandler pTimerHandler) {
			mCloud.unregisterUpdateHandler(pTimerHandler);
			WaterDropItem dropitem = mDropPool.obtainPoolItem();
			mCloud.drop(dropitem.getEntity(), mSky, new WaterDropListener());
		}

		private class WaterDropListener implements IWaterDropListener {
			@Override
			public void onHitTheGround(WaterDrop pWaterDrop) {
				final WaterSplashItem splashitem = mSplashPool.obtainPoolItem();
				CloudLayer.this.attachChild(splashitem.getEntity());
				mPos = pWaterDrop.getSceneCenterCoordinates();
				splashitem.getEntity().splat(mPos[Constants.VERTEX_INDEX_X], mPos[Constants.VERTEX_INDEX_Y]);

				if (getWaterDropListener() != null) {
					getWaterDropListener().onHitTheGround(pWaterDrop);
				}
			}
		}
	}

	/**
	 * This class is used to restart on a new position after it dies
	 * @author nazgee
	 *
	 */
	class CloudListener implements Cloud.CloudListener {
		@Override
		public void onFinished(Cloud pCloud) {
			unregiterCloud(pCloud);
			launchNewCloud();
		}
	}
}
