package eu.nazgee.util;

import java.util.ArrayList;

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
//		this.updateOverallHeight(pMenuScene);
//
//		final int menuItemCount = pMenuScene.getMenuItemCount();
//
//		for(int i = 0; i < menuItemCount; i++) {
//			final IMenuItem menuItem = pMenuScene.getMenuItem(i);
//
//			final float x = this.getMenuItemX(pMenuScene, i);
//			final float y = this.getMenuItemY(pMenuScene, i);
//			this.onMenuItemPositionBuilt(pMenuScene, i, menuItem, x, y);
//		}
	}

	@Override
	public void resetMenuSceneAnimations(final MenuScene pMenuScene) {
//		final int menuItemCount = pMenuScene.getMenuItemCount();
//
//		for(int i = 0; i < menuItemCount; i++) {
//			final IMenuItem menuItem = pMenuScene.getMenuItem(i);
//
//			final float x = this.getMenuItemX(pMenuScene, i);
//			final float y = this.getMenuItemY(pMenuScene, i);
//			this.onMenuItemPositionReset(pMenuScene, i, menuItem, x, y);
//		}
	}

	protected void onMenuItemPositionBuilt(final MenuScene pMenuScene, final int pIndex, final IMenuItem pMenuItem, final float pX, final float pY) {
	}

	protected void onMenuItemPositionReset(final MenuScene pMenuScene, final int pIndex, final IMenuItem pMenuItem, final float pX, final float pY) {
	}


	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================


}
