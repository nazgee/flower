package eu.nazgee.flower.pool.popup;

import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

import eu.nazgee.flower.pool.PooledEntityItem;

public class PopupItem extends PooledEntityItem<Popup> {
	public PopupItem(Font pFont, VertexBufferObjectManager pVertexBufferObjectManager) {
		mEntity = new Popup(0, 0, pFont, "", 25,
				new TextOptions(AutoWrap.LETTERS, 500, HorizontalAlign.CENTER, Text.LEADING_DEFAULT),
				pVertexBufferObjectManager, this);
	}
}
