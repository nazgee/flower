package eu.nazgee.flower.activity.game.scene.over;

import org.andengine.engine.camera.Camera;
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
import eu.nazgee.misc.StarRating;
import eu.nazgee.util.Anchor;
import eu.nazgee.util.Anchor.eAnchorPointXY;
import eu.nazgee.util.EmptyMenuAnimator;
import eu.nazgee.util.NineSliceSprite;

public class MenuGameLost extends BaseMenu {
	public static final int MENU_RESET = 0;
	public static final int MENU_GO_MAIN = 1;
	private final Text mDescription;
	private final StarRating mStarRating;

	public MenuGameLost(final float W, final float H, final Camera pCamera, final Font pFont,
			final Font pDescFont, final VertexBufferObjectManager pVertexBufferObjectManager,
			final TexturesLibrary pTexturesLibrary) {
		super(W, H, pCamera, pFont, pVertexBufferObjectManager);

		// make sure that nobody is moving menu items around
		setMenuSceneAnimator(new EmptyMenuAnimator());

		// Prepare background
		final float bgw = pCamera.getWidth() * 0.8f;
		final float bgh = pCamera.getHeight() * 1.0f;
		final NineSliceSprite bg = pTexturesLibrary.getFactory().populateFrameOverMenu(bgw, bgh, pVertexBufferObjectManager);
		attachChild(bg);
		Anchor.setPosCenterAtParent(bg, eAnchorPointXY.CENTERED);

		// Prepare menu items
		final IMenuItem hometxt = addMenuEntry("main\nmenu", MENU_GO_MAIN, Consts.COLOR_MENU_TEXT_SELECTED, Consts.COLOR_MENU_TEXT_UNSELECTED, getVertexBufferObjectManager());
		final IMenuItem homeico = addMenuEntry(pTexturesLibrary.getIconHome(), MENU_GO_MAIN, Color.RED, Color.WHITE, pVertexBufferObjectManager);

		// Prepare menu items
		final IMenuItem resettxt = addMenuEntry("retry\nlevel", MENU_RESET, Consts.COLOR_MENU_TEXT_SELECTED, Consts.COLOR_MENU_TEXT_UNSELECTED, getVertexBufferObjectManager());
		final IMenuItem resetico = addMenuEntry(pTexturesLibrary.getIconRefresh(), MENU_RESET, Color.RED, Color.WHITE, pVertexBufferObjectManager);

		/*
		 *  We will be using layouts, which means we need to reattach menu items
		 *  or we will get an Exception (already has a parent)
		 */
		detachChild(hometxt);
		detachChild(homeico);
		detachChild(resettxt);
		detachChild(resetico);

		// Use layouts for positioning
		final float margin = bg.getHeight() * 0.05f;
		bg.attachChild(homeico);
		bg.attachChild(hometxt);
		Anchor.setPosBottomRight(hometxt, bg.getWidth() - margin, margin);
		Anchor.setPosBottomRightAtSibling(homeico, hometxt, eAnchorPointXY.BOTTOM_LEFT);

		// Use layouts for positioning
		bg.attachChild(resetico);
		bg.attachChild(resettxt);
		Anchor.setPosBottomLeft(resettxt, margin, margin);
		Anchor.setPosBottomLeftAtSibling(resetico, resettxt, eAnchorPointXY.BOTTOM_RIGHT);

		mStarRating = new StarRating(3, 0, 0, pTexturesLibrary.getIconStar(), pVertexBufferObjectManager);
		bg.attachChild(mStarRating);
		Anchor.setPosAtParent(mStarRating, eAnchorPointXY.TOP_MIDDLE, eAnchorPointXY.TOP_MIDDLE);

		// Prepare description
		mDescription = new Text(0, 0, pDescFont, "", 1000,
				new TextOptions(AutoWrap.WORDS, bg.getWidth() - margin, HorizontalAlign.CENTER),
				getVertexBufferObjectManager());
		mDescription.setColor(Consts.COLOR_MENU_TEXT_DESCRIPTION);
		bg.attachChild(mDescription);
	}

	public void setDescription(final String pDescription) {
		mDescription.setText(pDescription);
		Anchor.setPosTopMiddleAtSibling(mDescription, mStarRating, eAnchorPointXY.BOTTOM_MIDDLE);
	}

	public void setStars(final int pScore) {
		mStarRating.setStars(pScore);
	}
}