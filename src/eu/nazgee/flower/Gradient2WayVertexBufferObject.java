package eu.nazgee.flower;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.vbo.HighPerformanceRectangleVertexBufferObject;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.color.Color;

import android.util.SparseArray;

public class Gradient2WayVertexBufferObject extends HighPerformanceRectangleVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================
	public enum eGradientVertices {
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT;

		int getIndex() {
			switch (this) {
			case TOP_LEFT:
				return 0;
			case BOTTOM_LEFT:
				return 1;
			case TOP_RIGHT:
				return 2;
			case BOTTOM_RIGHT:
				return 3;
			}

			return 666;
		}
	}
	// ===========================================================
	// Fields
	// ===========================================================
	SparseArray<Color> mGradientColors = new SparseArray<Color>(Rectangle.VERTICES_PER_RECTANGLE);
	// ===========================================================
	// Constructors
	// ===========================================================
	public Gradient2WayVertexBufferObject(
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
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onUpdateColor(final Rectangle pRectangle) {
		final float[] bufferData = this.mBufferData;

		final float packedColor = pRectangle.getColor().getABGRPackedFloat();

		bufferData[0 * Rectangle.VERTEX_SIZE + Rectangle.COLOR_INDEX] = (mGradientColors.get(0) == null) ? packedColor : mGradientColors.get(0).getABGRPackedFloat();
		bufferData[1 * Rectangle.VERTEX_SIZE + Rectangle.COLOR_INDEX] = (mGradientColors.get(1) == null) ? packedColor : mGradientColors.get(1).getABGRPackedFloat();
		bufferData[2 * Rectangle.VERTEX_SIZE + Rectangle.COLOR_INDEX] = (mGradientColors.get(2) == null) ? packedColor : mGradientColors.get(2).getABGRPackedFloat();
		bufferData[3 * Rectangle.VERTEX_SIZE + Rectangle.COLOR_INDEX] = (mGradientColors.get(3) == null) ? packedColor : mGradientColors.get(3).getABGRPackedFloat();

		this.setDirtyOnHardware();
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
