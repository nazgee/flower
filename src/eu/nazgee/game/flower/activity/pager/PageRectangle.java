package eu.nazgee.game.flower.activity.pager;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class PageRectangle extends Rectangle implements IPage {

	public PageRectangle(float pX, float pY, float pWidth, float pHeight,
			VertexBufferObjectManager pVertexBufferObjectManager,
			DrawType pDrawType) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager, pDrawType);
	}
}
