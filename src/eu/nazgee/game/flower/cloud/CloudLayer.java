package eu.nazgee.game.flower.cloud;

import java.util.Random;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

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
			ITiledTextureRegion pCloudTexture, ITextureRegion pWaterDropTexture, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY);
		mW = W;
		mH = H;
		mAvgSpeed = pAvgSpeed;
		mAvgTime = pAvgTime;
		mAvgDistance = pAvgSpeed * pAvgTime;
		mVariationSpeed = pVariationSpeed;
		mVariationTime = pVariationTime;
		mCloudPool = new CloudPool(pCloudTexture, pWaterDropTexture, pVertexBufferObjectManager);

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

		final Cloud cloud = pCloudItem.getCloud();
		Cloud.CloudListener listener = cloud.getTravelListener();
		if (listener == null) {
			listener = new CloudListener(pCloudItem);
		}

		cloud.travel(x, y, speed*time, time, listener);

		TimerHandler rainman = new TimerHandler(time/2, new RainMain(cloud));
		cloud.registerUpdateHandler(rainman);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	class RainMain implements ITimerCallback {
		private final Cloud mCloud;
		public RainMain(Cloud mCloud) {
			this.mCloud = mCloud;
		}
		@Override
		public void onTimePassed(TimerHandler pTimerHandler) {
			mCloud.unregisterUpdateHandler(pTimerHandler);
			mCloud.drop(null);
		}
	}
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