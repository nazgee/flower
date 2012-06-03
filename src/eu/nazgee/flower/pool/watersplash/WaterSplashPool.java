package eu.nazgee.flower.pool.watersplash;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.adt.pool.Pool;

public class WaterSplashPool extends Pool<WaterSplashItem> {

	private final ITiledTextureRegion mWaterSplashTextureRegions;
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;

	public WaterSplashPool(ITiledTextureRegion pWaterSplashTextureRegions, EntityDetachRunnablePoolUpdateHandler pDetacher, VertexBufferObjectManager pVertexBufferObjectManager) {
		mWaterSplashTextureRegions = pWaterSplashTextureRegions;
		mVertexBufferObjectManager = pVertexBufferObjectManager;
		mDetacher = pDetacher;
	}

	@Override
	protected WaterSplashItem onAllocatePoolItem() {
		return new WaterSplashItem(mWaterSplashTextureRegions, mDetacher, mVertexBufferObjectManager);
	}

}
