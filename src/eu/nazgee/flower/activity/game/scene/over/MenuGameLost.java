package eu.nazgee.flower.activity.game.scene.over;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.content.Context;
import eu.nazgee.flower.Consts;
import eu.nazgee.flower.EmptyMenuAnimator;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.game.utils.helpers.Positioner;
import eu.nazgee.game.utils.scene.menu.SceneMenu;
import eu.nazgee.util.LayoutBase.eAnchorPointXY;
import eu.nazgee.util.LayoutLinear;
import eu.nazgee.util.NineSliceSprite;

public class MenuGameLost extends SceneMenu {
	public static final int MENU_RESET = 0;
	public static final int MENU_GO_MAIN = 1;
	private Text mDescription;

	public MenuGameLost(float W, float H, Camera pCamera, Font pFont,
			Font pDescFont, VertexBufferObjectManager pVertexBufferObjectManager,
			TexturesLibrary pTexturesLibrary) {
		super(W, H, pCamera, pFont, pVertexBufferObjectManager);
		
		float[] reuse = new float[2];

		// make sure that nobody is moving menu items around
		setMenuAnimator(new EmptyMenuAnimator());

		// Prepare background
		NineSliceSprite bg = new NineSliceSprite(0, 0, pCamera.getWidth() * 0.8f, pCamera.getHeight()*0.8f, pTexturesLibrary.getFrameLevelCompleted(), 15, 15, 15, 15, pVertexBufferObjectManager);
		attachChild(bg);
		Positioner.setCentered(bg, pCamera.getWidth()/2, pCamera.getHeight()/2);

		// Prepare menu items
		IMenuItem hometxt = addMenuEntry("main\nmenu", MENU_GO_MAIN, Consts.COLOR_TEXT_SELECTED, Consts.COLOR_TEXT_UNSELECTED, getVertexBufferObjectManager());
		IMenuItem homeico = addMenuEntry(pTexturesLibrary.getIconHome(), MENU_GO_MAIN, Color.RED, Color.WHITE, pVertexBufferObjectManager);
		reuse = bg.convertLocalToSceneCoordinates(bg.getWidth()/2 - homeico.getWidth(), bg.getHeight() - homeico.getHeight(), reuse);

		// Prepare menu items
		IMenuItem resettxt = addMenuEntry("retry\nlevel", MENU_RESET, Consts.COLOR_TEXT_SELECTED, Consts.COLOR_TEXT_UNSELECTED, getVertexBufferObjectManager());
		IMenuItem resetico = addMenuEntry(pTexturesLibrary.getIconRefresh(), MENU_RESET, Color.RED, Color.WHITE, pVertexBufferObjectManager);
		reuse = bg.convertLocalToSceneCoordinates(bg.getWidth()/2, bg.getHeight() - resetico.getHeight(), reuse);

		/*
		 *  We will be using layouts, which means we need to reattach menu items
		 *  or we will get an Exception (already has a parent)
		 */
		detachChild(hometxt);
		detachChild(homeico);
		detachChild(resettxt);
		detachChild(resetico);

		final float margin = bg.getHeight() * 0.1f;

		// Use layouts for positioning
		LayoutLinear homelayout = LayoutLinear.populateHorizontalAlignedCenter(eAnchorPointXY.BOTTOM_LEFT, eAnchorPointXY.TOP_LEFT);
		homelayout.setItems(hometxt, homeico);
		homelayout.setPosition(margin, bg.getHeight() - margin);
		bg.attachChild(homelayout);

		// Use layouts for positioning
		LayoutLinear resetlayout = LayoutLinear.populateHorizontalAlignedCenter(eAnchorPointXY.BOTTOM_RIGHT, eAnchorPointXY.TOP_LEFT);
		resetlayout.setItems(resetico, resettxt);
		resetlayout.setPosition(bg.getWidth() - margin, bg.getHeight() - margin);
		bg.attachChild(resetlayout);

		Rectangle rect = new Rectangle(0, 0, 10, 10, getVertexBufferObjectManager());
		rect.setColor(Color.PINK);
		resetlayout.attachChild(rect);

		buildAnimations();
		setBackgroundEnabled(false);

		mDescription = new Text(margin/2, 40, pDescFont, "", 1000,
				new TextOptions(AutoWrap.WORDS, bg.getWidth() - margin,
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