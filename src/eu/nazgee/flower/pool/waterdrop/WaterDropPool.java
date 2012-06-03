package eu.nazgee.flower.pool.waterdrop;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.adt.pool.Pool;

public class WaterDropPool extends Pool<WaterDropItem> {
	private final ITextureRegion mTextureRegion;
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;

	public WaterDropPool(ITextureRegion pWaterDropTextureRegion, EntityDetachRunnablePoolUpdateHandler pDetacher, VertexBufferObjectManager pVertexBufferObjectManager) {
		mTextureRegion = pWaterDropTextureRegion;
		mVertexBufferObjectManager = pVertexBufferObjectManager;
		mDetacher = pDetacher;
	}

	@Override
	protected WaterDropItem onAllocatePoolItem() {
		return new WaterDropItem(mTextureRegion, mDetacher, mVertexBufferObjectManager);
	}

}
