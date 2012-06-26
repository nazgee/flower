package eu.nazgee.flower.pool.rainbow;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.adt.pool.Pool;

import eu.nazgee.flower.pool.PooledEntityItem;
import eu.nazgee.flower.pool.rainbow.RainbowPool.RainbowItem;

public class RainbowPool extends Pool<RainbowItem> {
	private final ITextureRegion mTextureRegion;
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;

	public RainbowPool(final ITextureRegion pButterflyTextureRegion, final EntityDetachRunnablePoolUpdateHandler pDetacher, final VertexBufferObjectManager pVertexBufferObjectManager) {
		mTextureRegion = pButterflyTextureRegion;
		mVertexBufferObjectManager = pVertexBufferObjectManager;
		mDetacher = pDetacher;
	}

	@Override
	protected RainbowItem onAllocatePoolItem() {
		return new RainbowItem(mTextureRegion, mDetacher, mVertexBufferObjectManager);
	}

	public static class RainbowItem extends PooledEntityItem<Rainbow> {
		public RainbowItem(final ITextureRegion pTextureRegion, final EntityDetachRunnablePoolUpdateHandler pDetacher, final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pDetacher);
			mEntity = new Rainbow(0, 0, pTextureRegion, pVertexBufferObjectManager, this);
		}
	}
}
