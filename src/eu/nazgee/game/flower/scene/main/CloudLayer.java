package eu.nazgee.game.flower.scene.main;

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

import eu.nazgee.game.flower.pool.cloud.Cloud;
import eu.nazgee.game.flower.pool.cloud.CloudItem;
import eu.nazgee.game.flower.pool.cloud.CloudPool;
import eu.nazgee.game.flower.pool.waterdrop.WaterDrop;
import eu.nazgee.game.flower.pool.waterdrop.WaterDrop.IWaterDropListener;
import eu.nazgee.game.flower.pool.waterdrop.WaterDropItem;
import eu.nazgee.game.flower.pool.waterdrop.WaterDropPool;
import eu.nazgee.game.flower.pool.watersplash.WaterSplash;
import eu.nazgee.game.flower.pool.watersplash.WaterSplash.IWaterSplashListener;
import eu.nazgee.game.flower.pool.watersplash.WaterSplashItem;
import eu.nazgee.game.flower.pool.watersplash.WaterSplashPool;

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

	private IRainDropListener mRainDropListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CloudLayer(float pX, float pY, final float W, final float H, 
			float pAvgSpeed, float pAvgTime,
			float pVariationSpeed, float pVariationTime, final int pCloudsNumber,
			final Sky pSky,
			ITiledTextureRegion pCloudTexture, ITextureRegion pWaterDropTexture, ITiledTextureRegion pWaterSplashTexture, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY);
		mW = W;
		mH = H;
		mAvgSpeed = pAvgSpeed;
		mAvgTime = pAvgTime;
		mSky = pSky;
		mAvgDistance = pAvgSpeed * pAvgTime;
		mVariationSpeed = pVariationSpeed;
		mVariationTime = pVariationTime;
		mCloudPool = new CloudPool(pCloudTexture, pVertexBufferObjectManager);
		mDropPool = new WaterDropPool(pWaterDropTexture, pVertexBufferObjectManager);
		mSplashPool = new WaterSplashPool(pWaterSplashTexture, pVertexBufferObjectManager);

		/*
		 * Smoothly ramp up number of clouds on layer
		 */
		TimerHandler starter = new TimerHandler(mAvgTime/pCloudsNumber, new ITimerCallback() {
			int started = 0;
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				CloudItem item = mCloudPool.obtainPoolItem();
				Cloud cloud = item.getEntity();
				attachChild(cloud);
				lunchCloudItem(item);
				started++;
				if (started < pCloudsNumber) {
					pTimerHandler.reset();
				}
				mClouds.add(cloud);
			}
		});
		registerUpdateHandler(starter);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public IRainDropListener getRainDropListener() {
		return mRainDropListener;
	}

	public void setRainDropListener(IRainDropListener mRainDropListener) {
		this.mRainDropListener = mRainDropListener;
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

	private void lunchCloudItem(CloudItem pCloudItem) {
		final float x = randomize(mW, mAvgDistance/2/mW) - mAvgDistance;
		final float y = rand.nextFloat() * mH;

		final float time = randomize(mAvgTime, mVariationTime);
		final float speed = randomize(mAvgSpeed, mVariationSpeed);

		final Cloud cloud = pCloudItem.getEntity();
		Cloud.CloudListener listener = cloud.getTravelListener();
		if (listener == null) {
			listener = new CloudListener(pCloudItem);
		}

		cloud.travel(x, y, speed*time, time, listener);
		sortCloudsByHeight();

		TimerHandler rainman = new TimerHandler(time/2, new RainMain(cloud));
		cloud.registerUpdateHandler(rainman);
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

	private void sortCloudsByHeight() {
		synchronized (mClouds) {
			Collections.sort(mClouds, new ComparatorHeight());
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
			mCloud.drop(dropitem.getEntity(), mSky, new WaterDropListener(dropitem));
		}

		private class WaterDropListener implements IWaterDropListener {
			private final WaterDropItem mWaterDropItem;
			public WaterDropListener(WaterDropItem mWaterDropItem) {
				this.mWaterDropItem = mWaterDropItem;
			}
			@Override
			public void onHitTheGround(WaterDrop pWaterDrop) {
				mWaterDropItem.scheduleDetachAndRecycle();

				final WaterSplashItem splashitem = mSplashPool.obtainPoolItem();
				CloudLayer.this.attachChild(splashitem.getEntity());
				mPos = pWaterDrop.getSceneCenterCoordinates();
				splashitem.getEntity().splat(mPos[Constants.VERTEX_INDEX_X], mPos[Constants.VERTEX_INDEX_Y],
						new WaterSplashListener(splashitem));
				if (getRainDropListener() != null) {
					getRainDropListener().onRainDrop(pWaterDrop);
				}
			}
		}

		private class WaterSplashListener implements IWaterSplashListener {
			private final WaterSplashItem mWaterSplashItem;
			public WaterSplashListener(WaterSplashItem mWaterSplashItem) {
				this.mWaterSplashItem = mWaterSplashItem;
			}
			@Override
			public void onSplashFinished(WaterSplash pWaterSplash) {
				mWaterSplashItem.scheduleDetachAndRecycle();
			}
		}
	}

	/**
	 * This class is used to restart on a new position after it dies
	 * @author nazgee
	 *
	 */
	class CloudListener implements Cloud.CloudListener {
		CloudItem mItem;

		public CloudListener(CloudItem mItem) {
			this.mItem = mItem;
		}

		@Override
		public void onStarted(Cloud pCloud) {
		}

		@Override
		public void onFinished(Cloud pCloud) {
			lunchCloudItem(mItem);
		}
	}
}
