package eu.nazgee.flower.pool.cloud;

import java.util.Random;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolItem;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.adt.pool.Pool;

public class CloudPool extends Pool<CloudItem> {

	private final Random rand = new Random();
	private final ITiledTextureRegion mCloudTextureRegions;
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;

	public CloudPool(ITiledTextureRegion pCloudTextureRegions, EntityDetachRunnablePoolUpdateHandler pDetacher, VertexBufferObjectManager pVertexBufferObjectManager) {
		mCloudTextureRegions = pCloudTextureRegions;
		mDetacher = pDetacher;
		mVertexBufferObjectManager = pVertexBufferObjectManager;
	}

	@Override
	protected CloudItem onAllocatePoolItem() {
		ITextureRegion cloudtex = mCloudTextureRegions.getTextureRegion(rand.nextInt(mCloudTextureRegions.getTileCount()));
		return new CloudItem(cloudtex, mDetacher, mVertexBufferObjectManager);
	}

}
