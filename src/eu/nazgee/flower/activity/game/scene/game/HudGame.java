package eu.nazgee.flower.activity.game.scene.game;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.modifier.IModifier;

import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.bases.BaseHUD;
import eu.nazgee.flower.flower.Flower;
import eu.nazgee.misc.Animator;
import eu.nazgee.util.Anchor;

public class HudGame extends BaseHUD {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final float FLOWER_TRANSITION_TIME = 0.3f;
	// ===========================================================
	// Fields
	// ===========================================================
	private final Sprite mNewFlowerIcon;
	private final SmartList<Sprite> mFlowers = new SmartList<Sprite>();
	private final Animator mFlowerAnimator = new Animator();
	// ===========================================================
	// Constructors
	// ===========================================================
	public HudGame(float W, float H, final TexturesLibrary pTexturesLibrary,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, 3, pVertexBufferObjectManager);
		for (int i = 0; i < TexturesLibrary.BLOSSOMS_NUMBER; i++) {
			final Sprite flower = new Sprite(0, 0, pTexturesLibrary.getFlower(i), pVertexBufferObjectManager);
			attachChild(flower);
			flower.setVisible(false);
			mFlowers.add(flower);
			Anchor.setPosTopLeft(flower, 0, H);
		}
		final float w = mFlowers.getFirst().getWidth();
		final float h = mFlowers.getFirst().getHeight();
		mNewFlowerIcon = new Sprite(0, 0, w, h, pTexturesLibrary.getIconNew(), pVertexBufferObjectManager);
		attachChild(mNewFlowerIcon);
		mNewFlowerIcon.setVisible(false);
		Anchor.setPosTopLeft(mNewFlowerIcon, 0, H);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public void setTextScore(CharSequence pText) {
		setTextLine(0, pText);
	}

	public void setTextSeeds(CharSequence pText) {
		setTextLine(1, pText);
	}

	public void setTextFlowers(CharSequence pText) {
		setTextLine(2, pText);
	}

	public void hideActiveFlower() {
		mFlowerAnimator.runModifier(new FadeOutModifier(FLOWER_TRANSITION_TIME, new IEntityModifierListener() {
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				pItem.setVisible(false);
			}
		}));
	}

	public void setActiveFlower(final Flower pFlower) {
		final IEntity oldFlower = mFlowerAnimator.getEntity();
		if (oldFlower != null) {
			oldFlower.setVisible(false);
		}

		final Sprite currentFlower;
		if (pFlower.getSeed().isLocked()) {
			currentFlower = mNewFlowerIcon;
		} else {
			currentFlower = mFlowers.get(pFlower.getSeed().blossomID);
		}

		currentFlower.setVisible(true);
		currentFlower.setAlpha(0);
		currentFlower.setColor(pFlower.getBlossomColor());
		mFlowerAnimator.setEntity(currentFlower);
		mFlowerAnimator.runModifier(new FadeInModifier(FLOWER_TRANSITION_TIME));
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
