package eu.nazgee.flower.pagerscene;

import java.util.LinkedList;

import org.andengine.entity.IEntity;

public interface IPage<T extends IEntity> extends IEntity {
	int getCapacity();
	void setItems(LinkedList<T> pItems);
	LinkedList<T> getItems();
}
