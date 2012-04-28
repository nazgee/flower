package eu.nazgee.game.flower.cloud;

import java.util.Random;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.game.flower.cloud.Cloud.TravelListener;

public class CloudLayer extends Entity{
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	final Random rand = new Random();
	final CloudPool mCloudPool;
	private final float mW;
	private final float mH;
	private final float mAvgSpeed;
	private final float mAvgTime;
	private final float mAvgDistance;
	private final float mVariationSpeed;
	private final float mVariationTime;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CloudLayer(float pX, float pY, final float W, final float H, 
			float pAvgSpeed, float pAvgTime,
			float pVariationSpeed, float pVariationTime, final int pCloudsNumber,
			ITiledTextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY);
		mW = W;
		mH = H;
		mAvgSpeed = pAvgSpeed;
		mAvgTime = pAvgTime;
		mAvgDistance = pAvgSpeed * pAvgTime;
		mVariationSpeed = pVariationSpeed;
		mVariationTime = pVariationTime;
		mCloudPool = new CloudPool(pTextureRegion, pVertexBufferObjectManager);

		/*
		 * Smoothly ramp up number of clouds on layer
		 */
		TimerHandler starter = new TimerHandler(mAvgTime/pCloudsNumber, new ITimerCallback() {
			int started = 0;
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				CloudItem item = mCloudPool.obtainPoolItem();
				Cloud cloud = item.getCloud();
				attachChild(cloud);
				lunchCloudItem(item);
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

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

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

		Cloud cloud = pCloudItem.getCloud();
		TravelListener listener = cloud.getTravelListener();
		if (listener == null) {
			listener = new CloudListener(pCloudItem);
		}

		cloud.travel(x, y, speed*time, time, listener);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	class CloudListener implements Cloud.TravelListener {
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