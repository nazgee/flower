package eu.nazgee.flower.pool.watersplash;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.adt.pool.Pool;

import eu.nazgee.flower.pool.PooledEntityItem;
import eu.nazgee.flower.pool.watersplash.WaterSplashPool.WaterSplashItem;

public class WaterSplashPool extends Pool<WaterSplashItem> {

	private final ITiledTextureRegion mWaterSplashTextureRegions;
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;

	public WaterSplashPool(final ITiledTextureRegion pWaterSplashTextureRegions, final EntityDetachRunnablePoolUpdateHandler pDetacher, final VertexBufferObjectManager pVertexBufferObjectManager) {
		mWaterSplashTextureRegions = pWaterSplashTextureRegions;
		mVertexBufferObjectManager = pVertexBufferObjectManager;
		mDetacher = pDetacher;
	}

	@Override
	protected WaterSplashItem onAllocatePoolItem() {
		return new WaterSplashItem(mWaterSplashTextureRegions, mDetacher, mVertexBufferObjectManager);
	}

	public static class WaterSplashItem extends PooledEntityItem<WaterSplash> {
		public WaterSplashItem(final ITiledTextureRegion pTextureWaterDrop, final EntityDetachRunnablePoolUpdateHandler pDetacher, final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pDetacher);
			mEntity = new WaterSplash(0, 0, pTextureWaterDrop, pVertexBufferObjectManager, this);
		}
	}
}
