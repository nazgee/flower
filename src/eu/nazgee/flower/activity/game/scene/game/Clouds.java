package eu.nazgee.flower.activity.game.scene.game;

import java.util.Collections;
import java.util.LinkedList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.math.MathUtils;

import eu.nazgee.flower.activity.game.scene.game.Sky.SkyHeightComparator;
import eu.nazgee.flower.pool.cloud.Cloud;
import eu.nazgee.flower.pool.cloud.CloudPool;
import eu.nazgee.flower.pool.cloud.CloudPool.CloudItem;
import eu.nazgee.util.Anchor.eAnchorPointXY;

public class Clouds extends Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
//	private final float mAvgSpeed;
//	private final float mAvgTime;
//	private final float mAvgDistance;
//	private final float mVariationSpeed;
//	private final float mVariationTime;

	private final LinkedList<Cloud> mClouds = new LinkedList<Cloud>();
	private final CloudPool mCloudPool;
	private final Camera mCamera;
	private final SkyHeightComparator<Cloud> mHeightComparator;
	// ===========================================================
	// Constructors
	// ===========================================================
	private Clouds(float pX, float pY, float pWidth, float pHeight,
			final Camera pCamera, final Sky pSky,
			final ITiledTextureRegion pCloudTexture,
			final EntityDetachRunnablePoolUpdateHandler pDetacher,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight);

		mCamera = pCamera;
		mCloudPool = new CloudPool(pCloudTexture, pDetacher, pVertexBufferObjectManager);
		mHeightComparator = new SkyHeightComparator<Cloud>(pSky, eAnchorPointXY.TOP_MIDDLE);
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
	private void launchNewCloud() {
		final CloudItem item = mCloudPool.obtainPoolItem();
		final Cloud cloud = item.getEntity();
		attachAndSort(cloud);

		final float camWidth = mCamera.getWidth();
		final float activeMargin = 0.9f * cloud.getWidth();
		final float activeWidth = camWidth + 2 * activeMargin;
		final float activeXmin = -activeMargin;
		final float activeXmax = activeXmin + activeWidth;

		final float x = MathUtils.random(activeXmin, activeXmax);

//		final float x = randomize(mW, mAvgDistance/2/mW) - mAvgDistance;
//		final float y = rand.nextFloat() * mH;
//		final float time = randomize(mAvgTime, mVariationTime);
//		final float speed = randomize(mAvgSpeed, mVariationSpeed);
//
//		Cloud.CloudListener listener = cloud.getTravelListener();
//		if (listener == null) {
//			listener = new CloudListener();
//		}
//
//		cloud.registerUpdateHandler(new TimerHandler(time/2, new RainMain(cloud)));
//		cloud.travel(x, y, speed*time, time, listener);
	}

	private void attachAndSort(final Cloud cloud) {
		synchronized (mClouds) {
			mClouds.add(cloud);
			Collections.sort(mClouds, mHeightComparator);
		}
		attachChild(cloud);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
