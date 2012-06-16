package eu.nazgee.util;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) Zynga 2012
 * 
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:50:42 - 25.04.2012
 */
public class NineSliceMenuItem extends NineSliceSprite implements IMenuItem {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final int mID;
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getID() {
		return this.mID;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================


	public NineSliceMenuItem(final int pID, float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion, float pInsetLeft, float pInsetTop,
			float pInsetRight, float pInsetBottom,
			VertexBufferObjectManager pVertexBufferObjectManager) {
			this(pID, pX, pY, pWidth, pHeight, pTextureRegion, pInsetLeft, pInsetTop,
				pInsetRight, pInsetBottom, pVertexBufferObjectManager, PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	public NineSliceMenuItem(final int pID, float pX, float pY,
			ITextureRegion pTextureRegion, float pInsetLeft, float pInsetTop,
			float pInsetRight, float pInsetBottom,
			VertexBufferObjectManager pVertexBufferObjectManager,
			ShaderProgram pShaderProgram) {
			this(pID, pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pInsetLeft, pInsetTop, pInsetRight, pInsetBottom,
				pVertexBufferObjectManager, pShaderProgram);
	}

	public NineSliceMenuItem(final int pID, float pX, float pY,
			ITextureRegion pTextureRegion, float pInsetLeft, float pInsetTop,
			float pInsetRight, float pInsetBottom,
			VertexBufferObjectManager pVertexBufferObjectManager) {
			this(pID, pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pInsetLeft, pInsetTop, pInsetRight, pInsetBottom, pVertexBufferObjectManager, PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	public NineSliceMenuItem(final int pID, final float pX, final float pY, final float pWidth,
			final float pHeight, final ITextureRegion pTextureRegion,
			final float pInsetLeft, final float pInsetTop,
			final float pInsetRight, final float pInsetBottom,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pInsetLeft, pInsetTop, pInsetRight, pInsetBottom,
				pVertexBufferObjectManager);
		mID = pID;
		
	}

	@Override
	public void onSelected() {
		/* Nothing. */
	}

	@Override
	public void onUnselected() {
		/* Nothing. */
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}