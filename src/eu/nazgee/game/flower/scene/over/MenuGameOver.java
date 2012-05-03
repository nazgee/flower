package eu.nazgee.game.flower.scene.over;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

import android.content.Context;
import eu.nazgee.game.flower.Consts;
import eu.nazgee.game.utils.scene.menu.SceneMenu;

public class MenuGameOver extends SceneMenu {
	public static final int MENU_RESET = 0;
	public static final int MENU_GO_MAIN = 1;
	private Text mDescription;

	public MenuGameOver(float W, float H, Camera pCamera, Font pFont,
			Font pDescFont, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pCamera, pFont, pVertexBufferObjectManager);

		addMenuEntry("Again!", MENU_RESET, Consts.COLOR_TEXT_SELECTED,
				Consts.COLOR_TEXT_UNSELECTED, getVertexBufferObjectManager());
		addMenuEntry("Main Menu", MENU_GO_MAIN, Consts.COLOR_TEXT_SELECTED,
				Consts.COLOR_TEXT_UNSELECTED, getVertexBufferObjectManager());
		buildAnimations();
		setBackgroundEnabled(false);

		mDescription = new Text(getW() * 0.025f, 40, pDescFont, "", 1000,
				new TextOptions(AutoWrap.WORDS, getW() * 0.95f,
						HorizontalAlign.CENTER, Text.LEADING_DEFAULT),
				getVertexBufferObjectManager());
		mDescription.setColor(Consts.COLOR_TEXT_DESCRIPTION);
		attachChild(mDescription);
	}

	void setDescription(String pDescription) {
		mDescription.setText(pDescription);
	}

	@Override
	public void onLoadResources(Engine e, Context c) {
	}

	@Override
	public void onUnload() {
	}

}