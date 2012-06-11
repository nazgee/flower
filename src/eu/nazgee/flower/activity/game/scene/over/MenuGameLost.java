package eu.nazgee.flower.activity.game.scene.over;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.content.Context;
import eu.nazgee.flower.Consts;
import eu.nazgee.flower.EmptyMenuAnimator;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.TexturesLibrary.TexturesMain;
import eu.nazgee.game.utils.helpers.Positioner;
import eu.nazgee.game.utils.scene.menu.SceneMenu;

public class MenuGameLost extends SceneMenu {
	public static final int MENU_RESET = 0;
	public static final int MENU_GO_MAIN = 1;
	private Text mDescription;

	public MenuGameLost(float W, float H, Camera pCamera, Font pFont,
			Font pDescFont, VertexBufferObjectManager pVertexBufferObjectManager,
			TexturesLibrary pTexturesLibrary) {
		super(W, H, pCamera, pFont, pVertexBufferObjectManager);
		
		setMenuAnimator(new EmptyMenuAnimator());
		float[] reuse = new float[2];

		Sprite bg = new org.andengine.entity.sprite.Sprite(0, 0, pTexturesLibrary.getMain().get(TexturesMain.BG_BG_ID), pVertexBufferObjectManager);
		bg.setAlpha(0.7f);
		attachChild(bg);
		Positioner.setCentered(bg, pCamera.getWidth()/2, pCamera.getHeight()/2);

		IMenuItem homeico = addMenuEntry(pTexturesLibrary.getMain().get(TexturesMain.ICONS_HOME_ID), MENU_GO_MAIN, Color.RED, Color.WHITE, pVertexBufferObjectManager);
		reuse = bg.convertLocalToSceneCoordinates(bg.getWidth()/2 - homeico.getWidth(), bg.getHeight() - homeico.getHeight(), reuse);
		homeico.setPosition(reuse[Constants.VERTEX_INDEX_X], reuse[Constants.VERTEX_INDEX_Y]);

		IMenuItem resetico = addMenuEntry(pTexturesLibrary.getMain().get(TexturesMain.ICONS_REFRESH_ID), MENU_RESET, Color.RED, Color.WHITE, pVertexBufferObjectManager);
		reuse = bg.convertLocalToSceneCoordinates(bg.getWidth()/2, bg.getHeight() - resetico.getHeight(), reuse);
		resetico.setPosition(reuse[Constants.VERTEX_INDEX_X], reuse[Constants.VERTEX_INDEX_Y]);

		IMenuItem hometxt = addMenuEntry("main\nmenu", MENU_GO_MAIN, Consts.COLOR_TEXT_SELECTED, Consts.COLOR_TEXT_UNSELECTED, getVertexBufferObjectManager());
		IMenuItem resettxt = addMenuEntry("retry\nlevel", MENU_RESET, Consts.COLOR_TEXT_SELECTED, Consts.COLOR_TEXT_UNSELECTED, getVertexBufferObjectManager());

		hometxt.setPosition(homeico.getX() - hometxt.getWidth(), homeico.getY());
		resettxt.setPosition(resetico.getX() + resetico.getWidth(), resetico.getY());

		buildAnimations();
		setBackgroundEnabled(false);

		final float margin = 0.1f;
		mDescription = new Text(bg.getWidth() * margin/2, 40, pDescFont, "", 1000,
				new TextOptions(AutoWrap.WORDS, bg.getWidth() * (1-margin),
						HorizontalAlign.CENTER, Text.LEADING_DEFAULT),
				getVertexBufferObjectManager());
		mDescription.setColor(Consts.COLOR_TEXT_DESCRIPTION);
		bg.attachChild(mDescription);
	}

	public void setDescription(String pDescription) {
		mDescription.setText(pDescription);
	}

	@Override
	public void onLoadResources(Engine e, Context c) {
	}

	@Override
	public void onUnload() {
	}

	@Override
	public void onLoad(Engine e, Context c) {
		super.onLoad(e, c);
		e.getCamera().setHUD(null);
	}

	

}