package eu.nazgee.game.flower.pool;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.util.adt.pool.PoolItem;
import org.andengine.util.call.Callback;

import eu.nazgee.game.flower.Statics;

abstract public class PooledEntityItem<T extends Entity> extends PoolItem {
	protected T mEntity;

	public T getEntity() {
		return mEntity;
	}

	/**
	 * This method safely detaches entity, and puts Item back to the pool
	 */
	public void scheduleDetachAndRecycle() {
		Statics.ENTITY_DETACH_HANDLER.scheduleDetach(getEntity(), new DetachCallback());
	}

	private class DetachCallback implements Callback<IEntity> {
		@Override
		public void onCallback(IEntity pCallbackValue) {
			recycle();
		}
	}
}
