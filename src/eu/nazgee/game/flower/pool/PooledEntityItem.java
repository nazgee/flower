package eu.nazgee.game.flower.pool;

import org.andengine.entity.Entity;
import org.andengine.util.adt.pool.PoolItem;

abstract public class PooledEntityItem<T extends Entity> extends PoolItem {
	protected T mEntity;

	public T getEntity() {
		return mEntity;
	}
}
