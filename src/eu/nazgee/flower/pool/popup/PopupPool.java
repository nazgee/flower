package eu.nazgee.flower.pool.popup;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.adt.pool.Pool;

public class PopupPool extends Pool<PopupItem> {

	private final Font mFont;
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;

	public PopupPool(Font pFont, EntityDetachRunnablePoolUpdateHandler pDetacher, VertexBufferObjectManager pVertexBufferObjectManager) {
		mFont = pFont;
		mVertexBufferObjectManager = pVertexBufferObjectManager;
		mDetacher = pDetacher;
	}

	@Override
	protected PopupItem onAllocatePoolItem() {
		return new PopupItem(mFont, mDetacher, mVertexBufferObjectManager);
	}

}
