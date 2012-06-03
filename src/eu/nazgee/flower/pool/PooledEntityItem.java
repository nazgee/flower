package eu.nazgee.flower.pool;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.adt.pool.PoolItem;
import org.andengine.util.call.Callback;

abstract public class PooledEntityItem<T extends Entity> extends PoolItem {
	protected T mEntity;
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;

	public PooledEntityItem(EntityDetachRunnablePoolUpdateHandler mDetacher) {
		this.mDetacher = mDetacher;
	}

	public T getEntity() {
		return mEntity;
	}

	/**
	 * This method safely detaches entity, and puts Item back to the pool
	 */
	public void scheduleDetachAndRecycle() {
		mDetacher.scheduleDetach(getEntity(), new DetachCallback());
	}

	private class DetachCallback implements Callback<IEntity> {
		@Override
		public void onCallback(IEntity pCallbackValue) {
			recycle();
		}
	}
}
