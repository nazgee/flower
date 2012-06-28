package eu.nazgee.flower;


import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.util.NineSliceMenuItem;
import eu.nazgee.util.NineSliceSprite;


public class EntitiesFactory {

	// ===========================================================
	// Constants
	// ===========================================================
	public static final int FRAME_MARGIN = 15;
	// ===========================================================
	// Fields
	// ===========================================================
	private final TexturesLibrary mTexturesLibrary;
	// ===========================================================
	// Constructors
	// ===========================================================
	public EntitiesFactory(final TexturesLibrary mTexturesLibrary) {
		super();
		this.mTexturesLibrary = mTexturesLibrary;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public NineSliceSprite populateFrameIngameMenu(final float pWidth, final float pHeight, final VertexBufferObjectManager pVBO) {
		return new NineSliceSprite(pWidth, pHeight, mTexturesLibrary.getFramePink(), FRAME_MARGIN, pVBO);
	}
	public NineSliceSprite populateFrameOverMenu(final float pWidth, final float pHeight, final VertexBufferObjectManager pVBO) {
		return new NineSliceSprite(pWidth, pHeight, mTexturesLibrary.getFrameOrange(), FRAME_MARGIN, pVBO);
	}
	public NineSliceSprite populateFrameHudShop(final float pWidth, final float pHeight, final VertexBufferObjectManager pVBO) {
		return new NineSliceSprite(pWidth, pHeight, mTexturesLibrary.getFrameGreenDark(), FRAME_MARGIN, pVBO);
	}
	public NineSliceSprite populateFrameMessageBox(final float pWidth, final float pHeight, final VertexBufferObjectManager pVBO) {
		return new NineSliceSprite(pWidth, pHeight, mTexturesLibrary.getFrameBlue(), FRAME_MARGIN, pVBO);
	}
	public NineSliceMenuItem populateFrameButton(final float pWidth, final float pHeight, final VertexBufferObjectManager pVBO, final int pID) {
		return new NineSliceMenuItem(pID, 0, 0, pWidth, pHeight, mTexturesLibrary.getButton(), FRAME_MARGIN, FRAME_MARGIN, FRAME_MARGIN, FRAME_MARGIN, pVBO);
	}
	public NineSliceSprite populateFrameLevel(final float pWidth, final float pHeight, final VertexBufferObjectManager pVBO, final boolean pLocked) {
		return new NineSliceSprite(pWidth, pHeight, !pLocked ? mTexturesLibrary.getFrameTransparent() : mTexturesLibrary.getFrameRed(), FRAME_MARGIN, pVBO);
	}
	public NineSliceSprite populateFrameSeed(final float pWidth, final float pHeight, final VertexBufferObjectManager pVBO, final boolean pLocked) {
		return new NineSliceSprite(pWidth, pHeight, !pLocked ? mTexturesLibrary.getFrameGreenDark() : mTexturesLibrary.getFrameGreen(), FRAME_MARGIN, pVBO);
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================



}
