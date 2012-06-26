package eu.nazgee.flower.pool.popup;

import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.adt.pool.Pool;

import eu.nazgee.flower.pool.PooledEntityItem;
import eu.nazgee.flower.pool.popup.PopupPool.PopupItem;

public class PopupPool extends Pool<PopupItem> {

	private final Font mFont;
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;

	public PopupPool(final Font pFont, final EntityDetachRunnablePoolUpdateHandler pDetacher, final VertexBufferObjectManager pVertexBufferObjectManager) {
		mFont = pFont;
		mVertexBufferObjectManager = pVertexBufferObjectManager;
		mDetacher = pDetacher;
	}

	@Override
	protected PopupItem onAllocatePoolItem() {
		return new PopupItem(mFont, mDetacher, mVertexBufferObjectManager);
	}

	public static class PopupItem extends PooledEntityItem<Popup> {
		public PopupItem(final Font pFont, final EntityDetachRunnablePoolUpdateHandler pDetacher, final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pDetacher);
			mEntity = new Popup(0, 0, pFont, "", 25,
					new TextOptions(AutoWrap.LETTERS, 500, HorizontalAlign.CENTER, Text.LEADING_DEFAULT),
					pVertexBufferObjectManager, this);
		}
	}
}
