package eu.nazgee.flower.pool.cloud;

import java.util.Random;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.adt.pool.Pool;

import eu.nazgee.flower.pool.PooledEntityItem;
import eu.nazgee.flower.pool.cloud.CloudPool.CloudItem;

public class CloudPool extends Pool<CloudItem> {

	private final Random rand = new Random();
	private final ITiledTextureRegion mCloudTextureRegions;
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;

	public CloudPool(final ITiledTextureRegion pCloudTextureRegions, final EntityDetachRunnablePoolUpdateHandler pDetacher, final VertexBufferObjectManager pVertexBufferObjectManager) {
		mCloudTextureRegions = pCloudTextureRegions;
		mDetacher = pDetacher;
		mVertexBufferObjectManager = pVertexBufferObjectManager;
	}

	@Override
	protected CloudItem onAllocatePoolItem() {
		final ITextureRegion cloudtex = mCloudTextureRegions.getTextureRegion(rand.nextInt(mCloudTextureRegions.getTileCount()));
		return new CloudItem(cloudtex, mDetacher, mVertexBufferObjectManager);
	}

	public static class CloudItem extends PooledEntityItem<Cloud> {
		public CloudItem(final ITextureRegion pTextureCloud, final EntityDetachRunnablePoolUpdateHandler pDetacher, final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pDetacher);
			mEntity = new Cloud(0, 0, pTextureCloud, pVertexBufferObjectManager, this);
		}
	}

}
