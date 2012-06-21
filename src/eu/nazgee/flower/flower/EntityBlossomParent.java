package eu.nazgee.flower.flower;

import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityMatcher;
import org.andengine.entity.IEntityParameterCallable;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
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
	private static final int CHILDREN_MIN = 0;
	private static final int CHILDREN_MAX = 6;
	private static final float CHILDREN_DELAY_MIN = 0.25f;
	private static final float CHILDREN_DELAY_MAX = 0.75f;
	private static final float TEMPORARY_SIZE_RESCALE = 0.7f;
	// ===========================================================
	// Constructors
	// ===========================================================
	public EntityBlossomParent(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, Color pColor) {
		this(pX, pY, pTextureRegion.getWidth() * TEMPORARY_SIZE_RESCALE, pTextureRegion.getHeight() * TEMPORARY_SIZE_RESCALE, pTextureRegion, pVertexBufferObjectManager, pColor);

	}

	public EntityBlossomParent(float pX, float pY, float pWidth,
			float pHeight, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, Color pColor) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, 	pColor);

		final int children = (MathUtils.random(CHILDREN_MIN, CHILDREN_MAX));
		for (int i = 0; i < children; i++) {
			final float scale = MathUtils.random(0.6f, 0.9f);
			final float x = MathUtils.random(1f, 3f) * getWidth();
			final float y = MathUtils.random(0.1f, 0.9f) * getHeight();

			EntityBlossom child = new EntityBlossom(0, 0, pWidth * scale, pHeight * scale,
					pTextureRegion, pVertexBufferObjectManager, pColor, i+1);
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
				mChildAnimDelay += MathUtils.random(CHILDREN_DELAY_MIN, CHILDREN_DELAY_MAX);
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

	@Override
	public void setBlossomListener(final IBlossomListener pBlossomListener) {
		super.setBlossomListener(pBlossomListener);

		this.callOnChildren(new IEntityParameterCallable() {
			@Override
			public void call(IEntity pEntity) {
				EntityBlossom child = (EntityBlossom) pEntity;
				child.setBlossomListener(pBlossomListener);
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