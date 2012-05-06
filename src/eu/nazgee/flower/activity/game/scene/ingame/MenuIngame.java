package eu.nazgee.flower.activity.game.scene.ingame;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.flower.Consts;
import eu.nazgee.game.utils.scene.menu.SceneMenu;

public class MenuIngame extends SceneMenu {
	public static final int MENU_RESET = 0;
	public static final int MENU_GO_MAIN = 1;

	public MenuIngame(float W, float H, Camera pCamera, Font pFont,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pCamera, pFont, pVertexBufferObjectManager);

		addMenuEntry("Reset", MENU_RESET, Consts.COLOR_TEXT_SELECTED,
				Consts.COLOR_TEXT_UNSELECTED, getVertexBufferObjectManager());
		addMenuEntry("Main Menu", MENU_GO_MAIN, Consts.COLOR_TEXT_SELECTED,
				Consts.COLOR_TEXT_UNSELECTED, getVertexBufferObjectManager());
		buildAnimations();
		setBackgroundEnabled(false);
	}

	@Override
	public void onLoadResources(Engine e, Context c) {
	}

	@Override
	public void onUnload() {
	}
}