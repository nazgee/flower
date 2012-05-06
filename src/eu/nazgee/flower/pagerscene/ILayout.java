package eu.nazgee.flower.pagerscene;

import org.andengine.entity.IEntity;


public interface ILayout {
	void layoutItems(IEntity... pItems);
	int getCapacity();
}
