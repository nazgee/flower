package eu.nazgee.misc;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.opengl.util.GLState;

public class ParallaxLayerEntity {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================
		private final boolean mIsScrollable;
		private final float mParallaxFactor;
		private final IAreaShape mAreaShape;
		private final float mFrequencyRatio;

		// ===========================================================
		// Constructors
		// ===========================================================
		public ParallaxLayerEntity(final float pParallaxFactor, final IAreaShape pAreaShape) {
			this(pParallaxFactor, pAreaShape, false, 1);
		}

		public ParallaxLayerEntity(final float pParallaxFactor, final IAreaShape pAreaShape, final boolean mIsScrollable) {
			this(pParallaxFactor, pAreaShape, mIsScrollable, 1);
		}

		public ParallaxLayerEntity(final float pParallaxFactor, final IAreaShape pAreaShape, final boolean mIsScrollable, final float pFrequencyRatio) {
			this.mParallaxFactor = pParallaxFactor;
			this.mAreaShape = pAreaShape;
			this.mIsScrollable = mIsScrollable;
			this.mFrequencyRatio = pFrequencyRatio;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================
		public boolean isScrollable() {
			return mIsScrollable;
		}
		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
		public void onDraw(final GLState pGLState, final Camera pCamera, final float pParallaxValue, final float pWorldSize) {
			pGLState.pushModelViewGLMatrix();
			{
				float widthRange;

				if (pWorldSize != 0) {
					widthRange = pWorldSize;
				} else {
					widthRange = pCamera.getWidth();
				}

				final float shapeWidthScaled = mAreaShape.getWidthScaled() * mFrequencyRatio;
				float baseOffset = (pParallaxValue * this.mParallaxFactor) % shapeWidthScaled;

				while (baseOffset > 0) {
					baseOffset -= shapeWidthScaled;
				}
				pGLState.translateModelViewGLMatrixf(baseOffset, 0, 0);

				float currentMaxX = baseOffset;

				do {
					this.mAreaShape.onDraw(pGLState, pCamera);
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