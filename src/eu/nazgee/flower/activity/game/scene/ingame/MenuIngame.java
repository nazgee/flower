package eu.nazgee.flower.activity.game.scene.ingame;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.flower.Consts;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.bases.BaseMenu;
import eu.nazgee.game.utils.helpers.Positioner;
import eu.nazgee.util.NineSliceSprite;

public class MenuIngame extends BaseMenu {
	public static final int MENU_RESET = 0;
	public static final int MENU_GO_MAIN = 1;

	public MenuIngame(final TexturesLibrary pTexturesLibrary, float W, float H, Camera pCamera, Font pFont,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pCamera, pFont, pVertexBufferObjectManager);

		// Prepare background
		final float bgw = pCamera.getWidth() * 0.8f;
		final float bgh = pCamera.getHeight() * 1.0f;
		NineSliceSprite bg = pTexturesLibrary.getFactory().populateFrameIngameMenu(bgw, bgh, pVertexBufferObjectManager);
		attachChild(bg);
		Positioner.setCentered(bg, pCamera.getWidth()/2, pCamera.getHeight()/2);

		// Prepare menu entries
		addMenuEntry("Reset", MENU_RESET, Consts.COLOR_MENU_TEXT_SELECTED,
				Consts.COLOR_MENU_TEXT_UNSELECTED, getVertexBufferObjectManager());
		addMenuEntry("Main Menu", MENU_GO_MAIN, Consts.COLOR_MENU_TEXT_SELECTED,
				Consts.COLOR_MENU_TEXT_UNSELECTED, getVertexBufferObjectManager());
		prepareAnimations();
	}
}