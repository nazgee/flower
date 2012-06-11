package eu.nazgee.flower;

import org.andengine.util.color.Color;

public @interface Consts {
	// ===========================================================
	// Constants
	// ===========================================================
	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;

	public static final int FLOWER_TEX_WIDTH = 80;
	public static final int FLOWER_TEX_HEIGHT = 80;
	/*
	 * How high above finger are flowers while dragging them around
	 */
	public static final int TOUCH_OFFSET_Y = (int) (- CAMERA_HEIGHT * 0.1f);
	public static final String LEVEL_PREFS_FILE = "levelprefs";
	public static final String SEEDS_PREFS_FILE = "seedsprefs";

	public static final String MENU_FONT = "Creepshow.ttf";
	public static final String HUD_FONT = "Creepshow.ttf";

	public static final Color COLOR_TEXT_SELECTED = Color.RED;
	public static final Color COLOR_TEXT_UNSELECTED = Color.WHITE;
	public static final Color COLOR_TEXT_DESCRIPTION = Color.YELLOW;

	public static final String FILE_BASE_HUD_FRAME = "basic/frame.svg";
	public static final String FILE_LEVELSELECTOR_ITEM_FRAME = FILE_BASE_HUD_FRAME;
	public static final String FILE_MESSAGEBOX_BUTTON = FILE_BASE_HUD_FRAME;

	public static final String FILE_SHOP_ITEM_FRAME = FILE_BASE_HUD_FRAME;
}
