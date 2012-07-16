package eu.nazgee.flower.activity.levelselector;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityParameterCallable;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.level.ILevelEntity;
import org.andengine.util.level.LevelLoaderUtils;
import org.xmlpull.v1.XmlSerializer;

import android.content.Intent;
import android.util.Log;
import android.util.Xml;
import eu.nazgee.flower.BuildConfig;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.activity.game.ActivityGame;
import eu.nazgee.flower.activity.levelselector.scene.GameLevelItem;
import eu.nazgee.flower.activity.levelselector.scene.SceneLevelselector;
import eu.nazgee.flower.base.pagerscene.ScenePager;
import eu.nazgee.flower.base.pagerscene.ScenePager.IItemClikedListener;
import eu.nazgee.flower.bases.BaseActivityPager;
import eu.nazgee.flower.flower.Flower;
import eu.nazgee.flower.flower.LoadableSeed;
import eu.nazgee.util.ModifiersFactory;

public class ActivityLevelselector extends BaseActivityPager<GameLevelItem>{

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	public TexturesLibrary mTexturesLibrary = new TexturesLibrary();
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected ScenePager<GameLevelItem> populatePagerScene(final float w, final float h,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		final ScenePager<GameLevelItem> scene = new SceneLevelselector(w, h, pVertexBufferObjectManager, mTexturesLibrary);
		scene.setItemClikedListener(new IItemClikedListener<GameLevelItem>() {
			@Override
			public void onItemClicked(final GameLevelItem pItem) {
				// launch game activity
				if (!pItem.getLevel().resources.isLocked()) {
					final Intent i = new Intent(ActivityLevelselector.this, ActivityGame.class);
					i.putExtra(ActivityGame.BUNDLE_LEVEL_ID, pItem.getLevel().id);
					startActivityForResult(i, ActivityGame.REQUESTCODE_PLAY_GAME);
				} else {
					pItem.registerEntityModifier(ModifiersFactory.shakeYourHead(3, 0.1f, 20));
//					mSceneInfo.load(getEngine(), ActivityLevelselector.this);
//					getEngine().getScene().setChildScene(mSceneInfo, false, false, true);
				}
			}
		});

		LevelEntityr ent = new LevelEntityr();
		for (int i = 0; i < 16; i++) {
			ent.attachChild(new Flower(0, 0, LoadableSeed.getSeedById(i+1), getVertexBufferObjectManager(), mTexturesLibrary, null));
		}
		dumpToXML(ent);

		return scene;
	}

	protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
		if (requestCode == ActivityGame.REQUESTCODE_PLAY_GAME) {
//			if (resultCode == RESULT_OK) {
//			}
			callOnEveryItem(new IEntityParameterCallable() {
				@Override
				public void call(IEntity pEntity) {
					GameLevelItem item = (GameLevelItem) pEntity;
					if ((item.getLevel().id == resultCode) ||
						(item.getLevel().id == (resultCode+1))) {
						item.reload(mTexturesLibrary, getVertexBufferObjectManager());
						Log.d(getClass().getSimpleName(), "Reloading level " + item.getLevel().id);
					}
				}
			});
		}
	}

	@Override
	protected void onCreateResources() {
		super.onCreateResources();

		// Load spritesheets
		mTexturesLibrary.loadResources(getEngine(), this);
		mTexturesLibrary.load(getEngine(), this);
	}

	private void dumpToXML(ILevelEntity pLevelEntity) {
		final XmlSerializer serializer = Xml.newSerializer();
		final StringWriter writer = new StringWriter();
		serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);

			serializer.startTag("", pLevelEntity.getLevelTagName());
			pLevelEntity.fillLevelTag(serializer);
			serializer.endTag("", pLevelEntity.getLevelTagName());

			serializer.endDocument();

			if (BuildConfig.DEBUG) {
				Log.d(getClass().getSimpleName(), writer.toString());
			}

			// write the file
			final FileOutputStream fOut = openFileOutput("current-level.xml", MODE_WORLD_READABLE);
			final OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(writer.toString());
			// ensure that everything is really written out and close
			osw.flush();
			osw.close();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	class LevelEntityr extends Entity implements ILevelEntity {
		@Override
		public void fillLevelTag(XmlSerializer pSerializer) throws IOException {
			LevelLoaderUtils.dumpChildren(this, pSerializer);
		}

		@Override
		public String getLevelTagName() {
			return "ent";
		}
		
	}
}