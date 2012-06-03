package eu.nazgee.flower.activity.levelselector.scene;

import java.util.LinkedList;

import org.andengine.engine.Engine;

import android.content.Context;
import eu.nazgee.flower.level.GameLevel;
import eu.nazgee.game.utils.loadable.LoadableResourceSimple;

public class GameLevelsLoader extends LoadableResourceSimple {
	public LinkedList<GameLevel> levels = new LinkedList<GameLevel>();

	public GameLevelsLoader() {
		int lvls = GameLevel.LEVELS_NUMBER;
		for (int i = 0; i < lvls; i++) {
			GameLevel lvl = GameLevel.getLevelById(i + 1);
			getLoader().install(lvl.resources);
			levels.add(lvl);
		}
	}

	@Override
	public void onLoadResources(Engine e, Context c) {
	}

	@Override
	public void onLoad(Engine e, Context c) {
	}

	@Override
	public void onUnload() {
	}
}
