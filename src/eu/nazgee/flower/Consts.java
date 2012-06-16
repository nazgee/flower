package eu.nazgee.flower;

import org.andengine.util.color.Color;

public @interface Consts {
	// ===========================================================
	// Constants
	// ===========================================================
	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;

	/*
	 * How high above finger are flowers while dragging them around
	 */
	public static final int TOUCH_OFFSET_Y = (int) (- CAMERA_HEIGHT * 0.1f);
	public static final String LEVEL_PREFS_FILE = "levelprefs";
	public static final String SEEDS_PREFS_FILE = "seedsprefs";

	public static final String MENU_FONT = "UNDO.ttf";
	public static final String HUD_FONT = "UNDO.ttf";

	public static final Color COLOR_MENU_TEXT_SELECTED = Color.RED;
	public static final Color COLOR_MENU_TEXT_UNSELECTED = Color.WHITE;
	public static final Color COLOR_MENU_TEXT_DESCRIPTION = Color.WHITE;
}
