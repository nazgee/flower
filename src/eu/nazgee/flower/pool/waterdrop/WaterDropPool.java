package eu.nazgee.flower.pool.waterdrop;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.adt.pool.Pool;

import eu.nazgee.flower.pool.PooledEntityItem;
import eu.nazgee.flower.pool.waterdrop.WaterDropPool.WaterDropItem;

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

	public static class WaterDropItem extends PooledEntityItem<WaterDrop> {
		public WaterDropItem(ITextureRegion pTextureWaterDrop, EntityDetachRunnablePoolUpdateHandler pDetacher, VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pDetacher);
			mEntity = new WaterDrop(0, 0, pTextureWaterDrop, pVertexBufferObjectManager, this);
		}
	}
}
