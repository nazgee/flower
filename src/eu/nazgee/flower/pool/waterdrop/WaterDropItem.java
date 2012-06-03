package eu.nazgee.flower.pool.waterdrop;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;

import eu.nazgee.flower.pool.PooledEntityItem;

public class WaterDropItem extends PooledEntityItem<WaterDrop> {
	public WaterDropItem(ITextureRegion pTextureWaterDrop, EntityDetachRunnablePoolUpdateHandler pDetacher, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pDetacher);
		mEntity = new WaterDrop(0, 0, pTextureWaterDrop, pVertexBufferObjectManager, this);
	}
}
