package eu.nazgee.game.flower.pool.cloud;

import java.util.Random;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.Pool;

public class CloudPool extends Pool<CloudItem> {

	private final Random rand = new Random();
	private final ITiledTextureRegion mCloudTextureRegions;
	private final VertexBufferObjectManager mVertexBufferObjectManager;

	public CloudPool(ITiledTextureRegion pCloudTextureRegions, VertexBufferObjectManager pVertexBufferObjectManager) {
		super();
		mCloudTextureRegions = pCloudTextureRegions;
		mVertexBufferObjectManager = pVertexBufferObjectManager;
	}

	@Override
	protected CloudItem onAllocatePoolItem() {
		ITextureRegion cloudtex = mCloudTextureRegions.getTextureRegion(rand.nextInt(mCloudTextureRegions.getTileCount()));
		return new CloudItem(cloudtex, mVertexBufferObjectManager);
	}

}
