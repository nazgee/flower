package eu.nazgee.game.flower.pool.popup;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.Pool;

public class PopupPool extends Pool<PopupItem> {

	private final Font mFont;
	private final VertexBufferObjectManager mVertexBufferObjectManager;

	public PopupPool(Font pFont, VertexBufferObjectManager pVertexBufferObjectManager) {
		super();
		mFont = pFont;
		mVertexBufferObjectManager = pVertexBufferObjectManager;
	}

	@Override
	protected PopupItem onAllocatePoolItem() {
		return new PopupItem(mFont, mVertexBufferObjectManager);
	}

}
