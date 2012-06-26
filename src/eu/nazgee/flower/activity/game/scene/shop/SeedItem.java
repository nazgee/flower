package eu.nazgee.flower.activity.game.scene.shop;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityParameterCallable;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import eu.nazgee.flower.EntitiesFactory;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.flower.Seed;
import eu.nazgee.util.Anchor;
import eu.nazgee.util.Anchor.eAnchorPointXY;
import eu.nazgee.util.LinearLayout;
import eu.nazgee.util.LinearLayout.eDirection;
import eu.nazgee.util.ModifiersFactory;
import eu.nazgee.util.NineSliceSprite;

public class SeedItem extends Entity implements ITouchArea{
	// ===========================================================
	// Constants
	// ===========================================================
	public static int ZINDEX_LOCK = 5;
	// ===========================================================
	// Fields
	// ===========================================================
	private final Seed mSeed;
	private final NineSliceSprite mSpriteFrame;
	private Sprite mSpriteBlossoms[];
	private IEntityModifier mModifier;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SeedItem(final Seed pSeed, final float W, final float H, final Font pFont, final EntitiesFactory pFactory, final VertexBufferObjectManager pVBOM, final TexturesLibrary pTexturesLibrary) {
		super(0, 0, W, H);
		this.mSeed = pSeed;

		/*
		 * Prepare background frame
		 */
		this.mSpriteFrame = pFactory.populateFrameSeed(W, H, pVBOM, pSeed.resources.isLocked());
		attachChild(this.mSpriteFrame);
		Anchor.setPosCenterAtParent(mSpriteFrame, eAnchorPointXY.CENTERED);

		/*
		 * Prepare and attach lock icon if item is locked
		 */
		if (mSeed.resources.isLocked()) {
			final Sprite locked = new Sprite(0, 0, pTexturesLibrary.getIconLocked(), pVBOM);
			locked.setZIndex(ZINDEX_LOCK);
			attachChild(locked);
			Anchor.setPosCenterAtParent(locked, eAnchorPointXY.CENTERED);
		} else {
			/*
			 * Prepare blossom Sprites
			 */
			this.mSpriteBlossoms = new Sprite[pSeed.col_plant.length];
			for (int i = 0; i < mSpriteBlossoms.length; i++) {
				mSpriteBlossoms[i] = new Sprite(0, 0, pTexturesLibrary.getFlower(pSeed.blossomID), pVBOM);
				mSpriteBlossoms[i].setColor(pSeed.col_plant[i]);
			}

			/*
			 * Layout and attach blossoms in a line of appropriate width,
			 * and with an anchor pointe set to TOP-MIDDLE
			 */
			final LinearLayout blossoms = new LinearLayout(mSpriteFrame.getWidth(), mSpriteFrame.getHeight()/2, eDirection.DIR_HORIZONTAL);
			blossoms.setItems(false, mSpriteBlossoms);
			mSpriteFrame.attachChild(blossoms);
			Anchor.setPosTopMiddleAtParent(blossoms, eAnchorPointXY.TOP_MIDDLE);

			/*
			 * Prepare text with seed's price
			 */
			final Text text = new Text(0, 0, pFont, "$" + pSeed.cost, pVBOM);
			text.setColor(Color.WHITE);
			mSpriteFrame.attachChild(text);
			Anchor.setPosBottomRight(text, mSpriteFrame.getWidth() - 10, 0);
		}

		setAlpha(1);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public boolean contains(final float pX, final float pY) {
		return mSpriteFrame.contains(pX, pY);
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		return mSpriteFrame.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}

	@Override
	public boolean isCulled(final Camera pCamera) {
		return mSpriteFrame.isCulled(pCamera);
	}

	protected void registerExclusiveModifier(final IEntityModifier pModifier) {
		unregisterEntityModifier(mModifier);
		pModifier.setAutoUnregisterWhenFinished(false);
		registerEntityModifier(pModifier);
	}

	public void animateYes() {
		registerExclusiveModifier(ModifiersFactory.nodYourHead(2, 0.2f, 1, 0.75f));
	}

	public void animateNo() {
		registerExclusiveModifier(ModifiersFactory.shakeYourHead(3, 0.1f, 20));
	}
	// ===========================================================
	// Methods
	// ===========================================================
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
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public Seed getSeed() {
		return mSeed;
	}
}
