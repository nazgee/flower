package eu.nazgee.game.flower.pool.waterdrop;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.Pool;

public class WaterDropPool extends Pool<WaterDropItem> {
	private final ITextureRegion mTextureRegion;
	private final VertexBufferObjectManager mVertexBufferObjectManager;

	public WaterDropPool(ITextureRegion pWaterDropTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
		super();
		mTextureRegion = pWaterDropTextureRegion;
		mVertexBufferObjectManager = pVertexBufferObjectManager;
	}

	@Override
	protected WaterDropItem onAllocatePoolItem() {
		return new WaterDropItem(mTextureRegion, mVertexBufferObjectManager);
	}

}
