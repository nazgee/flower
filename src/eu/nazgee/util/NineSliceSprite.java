package eu.nazgee.util;

import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class NineSliceSprite extends org.andengine.entity.sprite.NineSliceSprite {
	public NineSliceSprite(final float pWidth, final float pHeight,
			final ITextureRegion pTextureRegion, final float pInset,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, pWidth, pHeight, pTextureRegion, pInset, pInset, pInset,
				pInset, pVertexBufferObjectManager,
				PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	public NineSliceSprite(final float pX, final float pY, final float pWidth,
			final float pHeight, final ITextureRegion pTextureRegion,
			final float pInset,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pInset, pInset, pInset,
				pInset, pVertexBufferObjectManager,
				PositionColorTextureCoordinatesShaderProgram.getInstance());
	}
}