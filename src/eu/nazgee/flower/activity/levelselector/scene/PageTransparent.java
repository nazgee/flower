package eu.nazgee.flower.activity.levelselector.scene;

import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityParameterCallable;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.flower.pagerscene.ILayout;
import eu.nazgee.flower.pagerscene.PageRectangle;

public class PageTransparent extends PageRectangle {
	public PageTransparent(float pX, float pY, float pWidth, float pHeight,
			VertexBufferObjectManager pVertexBufferObjectManager,
			ILayout pLayout) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager, pLayout);
		setAlpha(0);
	}

	@Override
	public void setAlpha(final float pAlpha) {
		super.setAlpha(0);
		super.callOnChildren(new IEntityParameterCallable() {
			@Override
			public void call(IEntity pEntity) {
				pEntity.setAlpha(pAlpha);
			}
		});
	}

}
