package eu.nazgee.flower.pool.watersplash;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;

import eu.nazgee.flower.pool.PooledEntityItem;

public class WaterSplashItem extends PooledEntityItem<WaterSplash> {
	public WaterSplashItem(ITiledTextureRegion pTextureWaterDrop, EntityDetachRunnablePoolUpdateHandler pDetacher, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pDetacher);
		mEntity = new WaterSplash(0, 0, pTextureWaterDrop, pVertexBufferObjectManager, this);
	}
}
