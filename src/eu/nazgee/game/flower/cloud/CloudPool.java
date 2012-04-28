package eu.nazgee.game.flower.cloud;

import java.util.Random;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.Pool;

public class CloudPool extends Pool<CloudItem> {

	private final Random rand = new Random();
	private final ITiledTextureRegion mTextureRegions;
	private final VertexBufferObjectManager mVertexBufferObjectManager;

	public CloudPool(ITiledTextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
		super();
		mTextureRegions = pTextureRegion;
		mVertexBufferObjectManager = pVertexBufferObjectManager;
	}

	@Override
	protected CloudItem onAllocatePoolItem() {
		ITextureRegion tex = mTextureRegions.getTextureRegion(rand.nextInt(mTextureRegions.getTileCount()));
		return new CloudItem(tex, mVertexBufferObjectManager);
	}

}
