package eu.nazgee.flower.flower;

import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityMatcher;
import org.andengine.entity.IEntityParameterCallable;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.math.MathUtils;

import eu.nazgee.util.LayoutBase;

public class EntityBlossomParent extends EntityBlossom {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private float mChildAnimDelay = 0;
	private static final float CHILDREN_DELAY = 0.5f;
	// ===========================================================
	// Constructors
	// ===========================================================
	public EntityBlossomParent(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, Color pColor) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, pColor);

	}

	public EntityBlossomParent(float pX, float pY, float pWidth,
			float pHeight, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, Color pColor) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, 	pColor);

		final int children = (MathUtils.random(0, 6));
		for (int i = 0; i < children; i++) {
			final float scale = MathUtils.random(0.5f, 0.8f);
			final float x = MathUtils.random(1f, 1.5f) * getWidth();
			final float y = MathUtils.random(0.25f, 0.75f) * getHeight();

			EntityBlossom child = new EntityBlossom(0, 0, pWidth * scale, pHeight * scale,
					pTextureRegion, pVertexBufferObjectManager, pColor);
			LayoutBase.setItemPositionCenter(child, x, y);
			attachChild(child);
		}
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	@Override
	public synchronized void animateBloom() {
		super.animateBloom();

		mChildAnimDelay = 0;
		this.callOnChildren(new IEntityParameterCallable() {
			@Override
			public void call(IEntity pEntity) {
				mChildAnimDelay += CHILDREN_DELAY;
				EntityBlossom child = (EntityBlossom) pEntity;
				child.animateBloom(mChildAnimDelay);
			}
		}, new IEntityMatcher() {
			@Override
			public boolean matches(IEntity pEntity) {
				return (pEntity instanceof EntityBlossom);
			}
		});
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================



}
