package eu.nazgee.flower.base.pagerscene;

import java.util.LinkedList;

import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IAreaShape;

public interface IPage<T extends IEntity> extends IAreaShape {
	int getCapacity();
	void setItems(LinkedList<T> pItems);
	LinkedList<T> getItems();
}
