package eu.nazgee.flower.pool.waterdrop;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.flower.pool.PooledEntityItem;

public class WaterDropItem extends PooledEntityItem<WaterDrop> {
	public WaterDropItem(ITextureRegion pTextureWaterDrop, VertexBufferObjectManager pVertexBufferObjectManager) {
		mEntity = new WaterDrop(0, 0, pTextureWaterDrop, pVertexBufferObjectManager, this);
	}
}
