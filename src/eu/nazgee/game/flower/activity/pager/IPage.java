package eu.nazgee.game.flower.activity.pager;

import java.util.LinkedList;

import org.andengine.entity.IEntity;

public interface IPage extends IEntity {
	int getCapacity();
	void setItems(IEntity... pItems);
	LinkedList<IEntity> getItems();
}
