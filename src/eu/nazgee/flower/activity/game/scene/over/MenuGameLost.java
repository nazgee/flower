package eu.nazgee.flower.activity.game.scene.over;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.animator.InstantMenuSceneAnimator;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

import eu.nazgee.flower.Consts;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.bases.BaseMenu;
import eu.nazgee.util.LayoutBase;
import eu.nazgee.util.LayoutBase.eAnchorPointXY;
import eu.nazgee.util.LayoutLinear;
import eu.nazgee.util.NineSliceSprite;

public class MenuGameLost extends BaseMenu {
	public static final int MENU_RESET = 0;
	public static final int MENU_GO_MAIN = 1;
	private Text mDescription;

	public MenuGameLost(float W, float H, Camera pCamera, Font pFont,
			Font pDescFont, VertexBufferObjectManager pVertexBufferObjectManager,
			TexturesLibrary pTexturesLibrary) {
		super(W, H, pCamera, pFont, pVertexBufferObjectManager);


		// make sure that nobody is moving menu items around
		setMenuSceneAnimator(new InstantMenuSceneAnimator());

		// Prepare background
		final float bgw = pCamera.getWidth() * 0.8f;
		final float bgh = pCamera.getHeight() * 1.0f;
		NineSliceSprite bg = pTexturesLibrary.getFactory().populateFrameOverMenu(bgw, bgh, pVertexBufferObjectManager);
		attachChild(bg);
		LayoutBase.setSiblingItemPositionCenter(bg, this);

		// Prepare menu items
		IMenuItem hometxt = addMenuEntry("main\nmenu", MENU_GO_MAIN, Consts.COLOR_MENU_TEXT_SELECTED, Consts.COLOR_MENU_TEXT_UNSELECTED, getVertexBufferObjectManager());
		IMenuItem homeico = addMenuEntry(pTexturesLibrary.getIconHome(), MENU_GO_MAIN, Color.RED, Color.WHITE, pVertexBufferObjectManager);

		// Prepare menu items
		IMenuItem resettxt = addMenuEntry("retry\nlevel", MENU_RESET, Consts.COLOR_MENU_TEXT_SELECTED, Consts.COLOR_MENU_TEXT_UNSELECTED, getVertexBufferObjectManager());
		IMenuItem resetico = addMenuEntry(pTexturesLibrary.getIconRefresh(), MENU_RESET, Color.RED, Color.WHITE, pVertexBufferObjectManager);

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

		mDescription = new Text(margin/2, 40, pDescFont, "", 1000,
				new TextOptions(AutoWrap.WORDS, bg.getWidth() - margin,
						HorizontalAlign.CENTER, Text.LEADING_DEFAULT),
				getVertexBufferObjectManager());
		mDescription.setColor(Consts.COLOR_MENU_TEXT_DESCRIPTION);
		bg.attachChild(mDescription);
	}

	public void setDescription(String pDescription) {
		mDescription.setText(pDescription);
	}
}