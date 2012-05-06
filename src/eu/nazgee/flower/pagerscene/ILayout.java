package eu.nazgee.flower.pagerscene;

import java.util.LinkedList;

import org.andengine.entity.IEntity;


public interface ILayout {
	void layoutItems(LinkedList<? extends IEntity> pItems);
	int getCapacity();
}
