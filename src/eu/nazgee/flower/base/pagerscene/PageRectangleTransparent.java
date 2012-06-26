package eu.nazgee.flower.base.pagerscene;

import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityParameterCallable;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


public class PageRectangleTransparent<T extends IEntity> extends PageRectangle<T> {
	public PageRectangleTransparent(final float pX, final float pY, final float pWidth, final float pHeight,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final ILayout pLayout) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager, pLayout);
		setAlpha(0);
	}

	@Override
	public void setAlpha(final float pAlpha) {
		super.setAlpha(0);
		super.callOnChildren(new IEntityParameterCallable() {
			@Override
			public void call(final IEntity pEntity) {
				pEntity.setAlpha(pAlpha);
			}
		});
	}

}
