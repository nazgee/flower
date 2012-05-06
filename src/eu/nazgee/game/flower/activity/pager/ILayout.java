package eu.nazgee.game.flower.activity.pager;

import org.andengine.entity.IEntity;


public interface ILayout {
	void layoutItems(IEntity... pItems);
	int getCapacity();
}
