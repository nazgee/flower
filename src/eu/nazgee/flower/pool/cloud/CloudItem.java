package eu.nazgee.flower.pool.cloud;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.flower.pool.PooledEntityItem;

public class CloudItem extends PooledEntityItem<Cloud> {
	public CloudItem(ITextureRegion pTextureCloud, VertexBufferObjectManager pVertexBufferObjectManager) {
		mEntity = new Cloud(0, 0, pTextureCloud, pVertexBufferObjectManager, this);
	}
}
