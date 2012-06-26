package eu.nazgee.flower.pool.butterfly;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.adt.pool.Pool;

import eu.nazgee.flower.pool.PooledEntityItem;
import eu.nazgee.flower.pool.butterfly.ButterflyPool.ButterflyItem;

public class ButterflyPool extends Pool<ButterflyItem> {
	private final ITiledTextureRegion mTextureRegion;
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;

	public ButterflyPool(final ITiledTextureRegion pButterflyTextureRegion, final EntityDetachRunnablePoolUpdateHandler pDetacher, final VertexBufferObjectManager pVertexBufferObjectManager) {
		mTextureRegion = pButterflyTextureRegion;
		mVertexBufferObjectManager = pVertexBufferObjectManager;
		mDetacher = pDetacher;
	}

	@Override
	protected ButterflyItem onAllocatePoolItem() {
		return new ButterflyItem(mTextureRegion, mDetacher, mVertexBufferObjectManager);
	}

	public static class ButterflyItem extends PooledEntityItem<Butterfly> {
		public ButterflyItem(final ITiledTextureRegion pTextureRegion, final EntityDetachRunnablePoolUpdateHandler pDetacher, final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pDetacher);
			mEntity = new Butterfly(0, 0, pTextureRegion, pVertexBufferObjectManager, this);
		}
	}
}
