package eu.nazgee.flower;

import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.color.Color;

import android.util.SparseArray;

public class Gradient3WayVertexBufferObject extends HighPerformanceVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================
	public enum eGradientVertices {
		TOP_LEFT,
		TOP_MIDDLE,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_MIDDLE,
		BOTTOM_RIGHT;

		int getIndex() {
			switch (this) {
			case TOP_LEFT:
				return 0;
			case BOTTOM_LEFT:
				return 1;
			case TOP_MIDDLE:
				return 2;
			case BOTTOM_MIDDLE:
				return 3;
			case TOP_RIGHT:
				return 4;
			case BOTTOM_RIGHT:
				return 5;
			}

			return 666;
		}
	}
	// ===========================================================
	// Fields
	// ===========================================================
	SparseArray<Color> mGradientColors = new SparseArray<Color>(Gradient3Way.VERTICES_PER_GRADIENT3WAY);
	// ===========================================================
	// Constructors
	// ===========================================================
	public Gradient3WayVertexBufferObject(
			VertexBufferObjectManager pVertexBufferObjectManager,
			int pCapacity, DrawType pDrawType, boolean pAutoDispose,
			VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose,
				pVertexBufferObjectAttributes);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public void setGradientVertex(eGradientVertices pVertex, Color pColor) {
		mGradientColors.append(pVertex.getIndex(), pColor);
	}

	public void clearGradientVertex(eGradientVertices pVertex) {
		mGradientColors.delete(pVertex.getIndex());
	}

	public void clearGradientVertices() {
		mGradientColors.clear();
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public void onUpdateColor(final Gradient3Way pGradient) {
		final float[] bufferData = this.mBufferData;

		final float packedColor = pGradient.getColor().getABGRPackedFloat();

		bufferData[0 * Gradient3Way.VERTEX_SIZE + Gradient3Way.COLOR_INDEX] = (mGradientColors.get(0) == null) ? packedColor : mGradientColors.get(0).getABGRPackedFloat();
		bufferData[1 * Gradient3Way.VERTEX_SIZE + Gradient3Way.COLOR_INDEX] = (mGradientColors.get(1) == null) ? packedColor : mGradientColors.get(1).getABGRPackedFloat();
		bufferData[2 * Gradient3Way.VERTEX_SIZE + Gradient3Way.COLOR_INDEX] = (mGradientColors.get(2) == null) ? packedColor : mGradientColors.get(2).getABGRPackedFloat();
		bufferData[3 * Gradient3Way.VERTEX_SIZE + Gradient3Way.COLOR_INDEX] = (mGradientColors.get(3) == null) ? packedColor : mGradientColors.get(3).getABGRPackedFloat();
		bufferData[4 * Gradient3Way.VERTEX_SIZE + Gradient3Way.COLOR_INDEX] = (mGradientColors.get(4) == null) ? packedColor : mGradientColors.get(4).getABGRPackedFloat();
		bufferData[5 * Gradient3Way.VERTEX_SIZE + Gradient3Way.COLOR_INDEX] = (mGradientColors.get(5) == null) ? packedColor : mGradientColors.get(5).getABGRPackedFloat();

		this.setDirtyOnHardware();
	}

	public void onUpdateVertices(final Gradient3Way pGradient) {
		final float[] bufferData = this.mBufferData;

		final float x = 0;
		final float y = 0;
		final float x2 = pGradient.getWidth();
		final float y2 = pGradient.getHeight();
		final float xmiddle = x2 * pGradient.getGradientStep();

		bufferData[0 * Gradient3Way.VERTEX_SIZE + Gradient3Way.VERTEX_INDEX_X] = x;
		bufferData[0 * Gradient3Way.VERTEX_SIZE + Gradient3Way.VERTEX_INDEX_Y] = y;

		bufferData[1 * Gradient3Way.VERTEX_SIZE + Gradient3Way.VERTEX_INDEX_X] = x;
		bufferData[1 * Gradient3Way.VERTEX_SIZE + Gradient3Way.VERTEX_INDEX_Y] = y2;

		bufferData[2 * Gradient3Way.VERTEX_SIZE + Gradient3Way.VERTEX_INDEX_X] = xmiddle;
		bufferData[2 * Gradient3Way.VERTEX_SIZE + Gradient3Way.VERTEX_INDEX_Y] = y;

		bufferData[3 * Gradient3Way.VERTEX_SIZE + Gradient3Way.VERTEX_INDEX_X] = xmiddle;
		bufferData[3 * Gradient3Way.VERTEX_SIZE + Gradient3Way.VERTEX_INDEX_Y] = y2;

		bufferData[4 * Gradient3Way.VERTEX_SIZE + Gradient3Way.VERTEX_INDEX_X] = x2;
		bufferData[4 * Gradient3Way.VERTEX_SIZE + Gradient3Way.VERTEX_INDEX_Y] = y;

		bufferData[5 * Gradient3Way.VERTEX_SIZE + Gradient3Way.VERTEX_INDEX_X] = x2;
		bufferData[5 * Gradient3Way.VERTEX_SIZE + Gradient3Way.VERTEX_INDEX_Y] = y2;

		this.setDirtyOnHardware();
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
