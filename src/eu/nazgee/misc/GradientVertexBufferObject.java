package eu.nazgee.misc;

import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.adt.color.Color;

import android.util.SparseArray;

public class GradientVertexBufferObject extends HighPerformanceVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================
	public static final int VERTICES_PER_GRADIENT_BAND = 2;
	protected enum eGradientVerticesLayout {
		HORIZONTAL_TOP,
		HORIZONTAL_BOTTOM,
		VERTICAL_LEFT,
		VERTICAL_RIGHT;

		public int getVertexIndex(int pGradientBand) {
			switch (this) {
			case VERTICAL_RIGHT:
			case HORIZONTAL_TOP:
				return pGradientBand * VERTICES_PER_GRADIENT_BAND + 0;
			case VERTICAL_LEFT:
			case HORIZONTAL_BOTTOM:
				return pGradientBand * VERTICES_PER_GRADIENT_BAND + 1;
			}

			return -1;
		}
	}

	public enum eGradientLayout {
		LAYOUT_HORIZONTAL(eGradientVerticesLayout.HORIZONTAL_TOP, eGradientVerticesLayout.HORIZONTAL_BOTTOM),
		LAYOUT_VERTICATL(eGradientVerticesLayout.VERTICAL_LEFT, eGradientVerticesLayout.VERTICAL_RIGHT);

		private final eGradientVerticesLayout mVertexLayout1;
		private final eGradientVerticesLayout mVertexLayout2;

		private eGradientLayout(eGradientVerticesLayout pVertexLayout1, eGradientVerticesLayout pVertexLayout2) {
			this.mVertexLayout1 = pVertexLayout1;
			this.mVertexLayout2 = pVertexLayout2;
		}

		public eGradientVerticesLayout getLayout1() {
			return mVertexLayout1;
		}

		public eGradientVerticesLayout getLayout2() {
			return mVertexLayout2;
		}
	}

	// ===========================================================
	// Fields
	// ===========================================================
	private final int mBandsCount;
	private final SparseArray<Color> mVerticesColors;
	private final eGradientLayout mGradientLayout;
	// ===========================================================
	// Constructors
	// ===========================================================
	public GradientVertexBufferObject(
			VertexBufferObjectManager pVertexBufferObjectManager,
			int pGradientBands, DrawType pDrawType, boolean pAutoDispose,
			VertexBufferObjectAttributes pVertexBufferObjectAttributes,
			eGradientLayout pGradientLayout) {
		super(pVertexBufferObjectManager, pGradientBands * VERTICES_PER_GRADIENT_BAND * GradientRectangle.VERTEX_SIZE, pDrawType, pAutoDispose,
				pVertexBufferObjectAttributes);
		this.mBandsCount = pGradientBands;
		this.mVerticesColors = new SparseArray<Color>(pGradientBands * VERTICES_PER_GRADIENT_BAND);
		this.mGradientLayout = pGradientLayout;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public void setGradientVertex(int pVertex, Color pColor) {
		mVerticesColors.put(pVertex, pColor);
	}

	public void setGradientVertexAlpha(final int pVertex, final float pAlpha) {
		mVerticesColors.get(pVertex).setAlpha(pAlpha);
	}

	public void clearGradientVertex(int pVertex) {
		mVerticesColors.delete(pVertex);
	}

	public void setGradientBand(int pGradientBand, Color pColor) {
		setGradientVertex(mGradientLayout.getLayout1().getVertexIndex(pGradientBand), pColor);
		setGradientVertex(mGradientLayout.getLayout2().getVertexIndex(pGradientBand), pColor);
	}

	public void setGradientBandAlpha(int pGradientBand, final float pAlpha) {
		setGradientVertexAlpha(mGradientLayout.getLayout1().getVertexIndex(pGradientBand), pAlpha);
		setGradientVertexAlpha(mGradientLayout.getLayout2().getVertexIndex(pGradientBand), pAlpha);
	}

	public void clearGradientBand(int pGradientBand) {
		clearGradientVertex(mGradientLayout.getLayout1().getVertexIndex(pGradientBand));
		clearGradientVertex(mGradientLayout.getLayout2().getVertexIndex(pGradientBand));
	}

	public void clearGradientVertices() {
		mVerticesColors.clear();
	}

	public int getGradientBandsNumber() {
		return mBandsCount;
	}

	public int getGradientVerticesNumber() {
		return mBandsCount * VERTICES_PER_GRADIENT_BAND;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public void onUpdateColor(final GradientRectangle pGradient) {
		final float[] bufferData = this.mBufferData;

		final float packedColor = pGradient.getColor().getABGRPackedFloat();
		final int verticesCount = getGradientVerticesNumber();
		for(int i = 0; i < verticesCount; i++) {
//			Log.e("aaa", "i=" + i);
			bufferData[i * GradientRectangle.VERTEX_SIZE + GradientRectangle.COLOR_INDEX]
					= (mVerticesColors.get(i) == null) ? packedColor : mVerticesColors.get(i).getABGRPackedFloat();
		}

		this.setDirtyOnHardware();
	}

	public void onUpdateVertices(final GradientRectangle pGradient) {
		final float[] bufferData = this.mBufferData;

		float x = 0;
		float y = 0;
		final float w = pGradient.getWidth();
		final float h = pGradient.getHeight();

		final int bandsNumber = getGradientBandsNumber();
//		Log.e("aaa", "onUpdateVertices(); mGradientColors.size()=" + mVerticesColors.size());

		if (mGradientLayout == eGradientLayout.LAYOUT_HORIZONTAL) {
			// horizontal gradient layout
			for(int i = 0; i < bandsNumber; i++) {
				x = i * w / (bandsNumber - 1);
//				Log.e("aaa", "x=" + x + "; w=" + w);
				bufferData[eGradientVerticesLayout.HORIZONTAL_TOP.getVertexIndex(i) * GradientRectangle.VERTEX_SIZE + GradientRectangle.VERTEX_INDEX_X] = x;
				bufferData[eGradientVerticesLayout.HORIZONTAL_TOP.getVertexIndex(i) * GradientRectangle.VERTEX_SIZE + GradientRectangle.VERTEX_INDEX_Y] = y;

				bufferData[eGradientVerticesLayout.HORIZONTAL_BOTTOM.getVertexIndex(i) * GradientRectangle.VERTEX_SIZE + GradientRectangle.VERTEX_INDEX_X] = x;
				bufferData[eGradientVerticesLayout.HORIZONTAL_BOTTOM.getVertexIndex(i) * GradientRectangle.VERTEX_SIZE + GradientRectangle.VERTEX_INDEX_Y] = y + h;
			}
		} else {
			// vertical gradient layout
			for(int i = 0; i < bandsNumber; i++) {
				y = i * h / (bandsNumber - 1);;
				bufferData[eGradientVerticesLayout.VERTICAL_RIGHT.getVertexIndex(i) * GradientRectangle.VERTEX_SIZE + GradientRectangle.VERTEX_INDEX_X] = x + w;
				bufferData[eGradientVerticesLayout.VERTICAL_RIGHT.getVertexIndex(i) * GradientRectangle.VERTEX_SIZE + GradientRectangle.VERTEX_INDEX_Y] = y;

				bufferData[eGradientVerticesLayout.VERTICAL_LEFT.getVertexIndex(i) * GradientRectangle.VERTEX_SIZE + GradientRectangle.VERTEX_INDEX_X] = x;
				bufferData[eGradientVerticesLayout.VERTICAL_LEFT.getVertexIndex(i) * GradientRectangle.VERTEX_SIZE + GradientRectangle.VERTEX_INDEX_Y] = y;
			}
		}

		this.setDirtyOnHardware();
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
