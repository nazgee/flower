package eu.nazgee.util;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.animator.MenuSceneAnimator;
import org.andengine.entity.scene.menu.item.IMenuItem;

public class EmptyMenuAnimator extends MenuSceneAnimator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================


	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void buildMenuSceneAnimations(final MenuScene pMenuScene) {
	}

	@Override
	public void resetMenuSceneAnimations(final MenuScene pMenuScene) {
	}

	@Override
	protected void onMenuItemPositionBuilt(final MenuScene pMenuScene, final int pIndex, final IMenuItem pMenuItem, final float pX, final float pY) {
	}

	@Override
	protected void onMenuItemPositionReset(final MenuScene pMenuScene, final int pIndex, final IMenuItem pMenuItem, final float pX, final float pY) {
	}


	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================


}
