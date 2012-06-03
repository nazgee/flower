package eu.nazgee.flower.pool.cloud;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;

import eu.nazgee.flower.pool.PooledEntityItem;

public class CloudItem extends PooledEntityItem<Cloud> {
	public CloudItem(ITextureRegion pTextureCloud, EntityDetachRunnablePoolUpdateHandler pDetacher, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pDetacher);
		mEntity = new Cloud(0, 0, pTextureCloud, pVertexBufferObjectManager, this);
	}
}
