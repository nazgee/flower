package eu.nazgee.game.flower;

import org.andengine.engine.Engine;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;

import android.content.Context;
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;

public class Statics extends SimpleLoadableResource {
	public static EntityDetachRunnablePoolUpdateHandler ENTITY_DETACH_HANDLER;

	@Override
	public void onLoadResources(Engine e, Context c) {
		ENTITY_DETACH_HANDLER = new EntityDetachRunnablePoolUpdateHandler();
		e.registerUpdateHandler(ENTITY_DETACH_HANDLER);
	}

	@Override
	public void onLoad(Engine e, Context c) {
	}

	@Override
	public void onUnload() {
	}
}
