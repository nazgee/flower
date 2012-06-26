package eu.nazgee.flower.flower;

import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityMatcher;
import org.andengine.entity.IEntityParameterCallable;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.math.MathUtils;

import eu.nazgee.util.Anchor;

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
	public EntityBlossomParent(final float pX, final float pY, final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, final Color pColor) {
		this(pX, pY, pTextureRegion.getWidth() * TEMPORARY_SIZE_RESCALE, pTextureRegion.getHeight() * TEMPORARY_SIZE_RESCALE, pTextureRegion, pVertexBufferObjectManager, pColor);

	}

	public EntityBlossomParent(final float pX, final float pY, final float pWidth,
			final float pHeight, final ITextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, final Color pColor) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, 	pColor);

		final int children = (MathUtils.random(CHILDREN_MIN, CHILDREN_MAX));
		for (int i = 0; i < children; i++) {
			final float scale = MathUtils.random(0.6f, 0.9f);
			final float x = MathUtils.random(1f, 3f) * getWidth();
			final float y = MathUtils.random(0.1f, 0.9f) * getHeight();

			final EntityBlossom child = new EntityBlossom(0, 0, pWidth * scale, pHeight * scale,
					pTextureRegion, pVertexBufferObjectManager, pColor, i+1);
			Anchor.setPosCenter(child, x, y);
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
			public void call(final IEntity pEntity) {
				mChildAnimDelay += MathUtils.random(CHILDREN_DELAY_MIN, CHILDREN_DELAY_MAX);
				final EntityBlossom child = (EntityBlossom) pEntity;
				child.animateBloom(mChildAnimDelay);
			}
		}, new IEntityMatcher() {
			@Override
			public boolean matches(final IEntity pEntity) {
				return (pEntity instanceof EntityBlossom);
			}
		});
	}

	@Override
	public void setBlossomListener(final IBlossomListener pBlossomListener) {
		super.setBlossomListener(pBlossomListener);

		this.callOnChildren(new IEntityParameterCallable() {
			@Override
			public void call(final IEntity pEntity) {
				final EntityBlossom child = (EntityBlossom) pEntity;
				child.setBlossomListener(pBlossomListener);
			}
		}, new IEntityMatcher() {
			@Override
			public boolean matches(final IEntity pEntity) {
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
