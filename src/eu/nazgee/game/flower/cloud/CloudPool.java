package eu.nazgee.game.flower.cloud;

import java.util.Random;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.Pool;

public class CloudPool extends Pool<CloudItem> {

	private final Random rand = new Random();
	private final ITiledTextureRegion mCloudTextureRegions;
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	private final ITextureRegion mWaterDropTexture;

	public CloudPool(ITiledTextureRegion pCloudTextureRegions, ITextureRegion pWaterDropTexture, VertexBufferObjectManager pVertexBufferObjectManager) {
		super();
		mCloudTextureRegions = pCloudTextureRegions;
		mWaterDropTexture = pWaterDropTexture;
		mVertexBufferObjectManager = pVertexBufferObjectManager;
	}

	@Override
	protected CloudItem onAllocatePoolItem() {
		ITextureRegion cloudtex = mCloudTextureRegions.getTextureRegion(rand.nextInt(mCloudTextureRegions.getTileCount()));
		return new CloudItem(cloudtex, mWaterDropTexture, mVertexBufferObjectManager);
	}

}
