package eu.nazgee.game.flower.cloud;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.PoolItem;

public class CloudItem extends PoolItem {
	private final Cloud mCloud;
	public CloudItem(ITextureRegion pTexture, VertexBufferObjectManager pVertexBufferObjectManager) {
		mCloud = new Cloud(0, 0, pTexture, pVertexBufferObjectManager);
	}

	public Cloud getCloud() {
		return mCloud;
	}
}
