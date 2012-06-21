package eu.nazgee.flower.activity.game.scene.game;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.util.Anchor;
import eu.nazgee.util.Anchor.eAnchorPointXY;

public class GameBackground extends CameraParallaxBackground {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final Sprite mGround;
	public static int SOLID_GND_HEIGHT = 42;

	// ===========================================================
	// Constructors
	// ===========================================================
	public GameBackground(final Camera pCamera, TexturesLibrary pTexturesLibrary, VertexBufferObjectManager pVBOM) {
		super(0, 0, 0, pCamera);

		final float y_bottom =	0;
		final float h_soild_bg1 = 46;

		final Sprite tree0 =	new Sprite(0, 0, pTexturesLibrary.getTree(0), pVBOM);
		final Sprite tree1 =	new Sprite(0, 0, pTexturesLibrary.getTree(1), pVBOM);
		final Sprite tree2 =	new Sprite(0, 0, pTexturesLibrary.getTree(2), pVBOM);
		final Sprite bg4 =	new Sprite(0, 0, pTexturesLibrary.getParalaxBack4(), pVBOM);
		final Sprite bg3 =	new Sprite(0, 0, pTexturesLibrary.getParalaxBack3(), pVBOM);
		final Sprite bg2 =	new Sprite(0, 0, pTexturesLibrary.getParalaxBack2(), pVBOM);
		final Sprite bg1 =	new Sprite(0, 0, pTexturesLibrary.getParalaxBack1(), pVBOM);
		mGround =			new Sprite(0, 0, pTexturesLibrary.getParalaxGround(), pVBOM);
		final Sprite bgSky = new Sprite(pCamera.getWidth()/2, pCamera.getHeight()/2, pTexturesLibrary.getSky(), pVBOM);

		Anchor.setPosBottomLeft(mGround,	0, y_bottom);
		Anchor.setPosBottomLeft(bg1,		0, y_bottom + SOLID_GND_HEIGHT);
		Anchor.setPosBottomLeft(bg2,		0, y_bottom + SOLID_GND_HEIGHT + h_soild_bg1);
		Anchor.setPosBottomLeft(bg3,		0, eAnchorPointXY.BOTTOM_LEFT.getParentY(bg2) - 10);
		Anchor.setPosBottomLeft(bg4,		0, eAnchorPointXY.BOTTOM_LEFT.getParentY(bg2) - 20);
		Anchor.setPosBottomLeft(tree2,		0, y_bottom + SOLID_GND_HEIGHT);
		Anchor.setPosBottomLeft(tree1,		100, y_bottom + SOLID_GND_HEIGHT);
		Anchor.setPosBottomLeft(tree0,		200, y_bottom + SOLID_GND_HEIGHT);

		attachParallaxEntity(new ParallaxEntity(-0.1f, bgSky));
		attachParallaxEntity(new ParallaxEntity(-0.2f, bg4));
		attachParallaxEntity(new ParallaxEntity(-0.4f, bg3));
		attachParallaxEntity(new ParallaxEntity(-0.6f, bg2));
		attachParallaxEntity(new ParallaxEntity(-0.8f, bg1));
		attachParallaxEntity(new ParallaxEntityScattered(-0.8f, tree2, 5));
		attachParallaxEntity(new ParallaxEntityScattered(-0.9f, tree1, 5));
		attachParallaxEntity(new ParallaxEntityScattered(-1f, tree0, 5));
		attachParallaxEntity(new ParallaxEntity(-1f, mGround));
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public Sprite getGroundSprite() {
		return mGround;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public static class ParallaxEntityScattered extends ParallaxEntity {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================
		private final float mParallaxFactor;
		private final IEntity mAreaShape;
		private final float mScatterRatio;

		// ===========================================================
		// Constructors
		// ===========================================================
		public ParallaxEntityScattered(final float pParallaxFactor, final IEntity pAreaShape) {
			this(pParallaxFactor, pAreaShape, 1);
		}

		public ParallaxEntityScattered(final float pParallaxFactor, final IEntity pAreaShape, final float pScatterRatio) {
			super(pParallaxFactor, pAreaShape);
			this.mParallaxFactor = pParallaxFactor;
			this.mAreaShape = pAreaShape;
			this.mScatterRatio = pScatterRatio;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
		public void onDraw(final GLState pGLState, final Camera pCamera, final float pParallaxValue) {
			pGLState.pushModelViewGLMatrix();
			{
				float widthRange = pCamera.getWidth();

				final float shapeWidthScaled = mAreaShape.getWidthScaled() * mScatterRatio;
				float baseOffset = (pParallaxValue * mParallaxFactor) % shapeWidthScaled;

				while (baseOffset > 0) {
					baseOffset -= shapeWidthScaled;
				}
				pGLState.translateModelViewGLMatrixf(baseOffset, 0, 0);

				float currentMaxX = baseOffset;

				do {
					mAreaShape.onDraw(pGLState, pCamera);
					pGLState.translateModelViewGLMatrixf(shapeWidthScaled - 1, 0, 0);
					currentMaxX += shapeWidthScaled;
				} while (currentMaxX < widthRange);
			}
			pGLState.popModelViewGLMatrix();
		}



		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
