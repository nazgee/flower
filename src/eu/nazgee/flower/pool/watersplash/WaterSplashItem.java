package eu.nazgee.flower.pool.watersplash;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.flower.pool.PooledEntityItem;

public class WaterSplashItem extends PooledEntityItem<WaterSplash> {
	public WaterSplashItem(ITiledTextureRegion pTextureWaterDrop, VertexBufferObjectManager pVertexBufferObjectManager) {
		mEntity = new WaterSplash(0, 0, pTextureWaterDrop, pVertexBufferObjectManager, this);
	}
}
