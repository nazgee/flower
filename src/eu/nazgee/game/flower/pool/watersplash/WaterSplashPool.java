package eu.nazgee.game.flower.pool.watersplash;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.Pool;

public class WaterSplashPool extends Pool<WaterSplashItem> {

	private final ITiledTextureRegion mWaterSplashTextureRegions;
	private final VertexBufferObjectManager mVertexBufferObjectManager;

	public WaterSplashPool(ITiledTextureRegion pWaterSplashTextureRegions, VertexBufferObjectManager pVertexBufferObjectManager) {
		super();
		mWaterSplashTextureRegions = pWaterSplashTextureRegions;
		mVertexBufferObjectManager = pVertexBufferObjectManager;
	}

	@Override
	protected WaterSplashItem onAllocatePoolItem() {
		return new WaterSplashItem(mWaterSplashTextureRegions, mVertexBufferObjectManager);
	}

}
