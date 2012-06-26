package eu.nazgee.flower.level;

import org.andengine.util.IMatcher;
import org.andengine.util.adt.list.SmartList;

public class Basket<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final SmartList<T> mItems = new SmartList<T>();
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public Basket<T> clear(final T pItem) {
		mItems.clear();
		return this;
	}

	public SmartList<T> getItems() {
		return mItems;
	}

	public Basket<T> add(final T pItem) {
		return add(pItem, 1);
	}

	public Basket<T> add(final T pItem, final int pCount) {
		for (int i = 0; i < pCount; i++) {
			getItems().add(pItem);
		}
		return this;
	}

	public Basket<T> add(final T ... pItems) {
		for (final T item : pItems) {
			mItems.add(item);
		}
		return this;
	}

	public Basket<T> removeOne(final T pItem) {
		mItems.remove(pItem);
		return this;
	}

	public Basket<T> removeAllEqual(final T pItem) {
		mItems.removeAll(new IMatcher<T>() {
			@Override
			public boolean matches(final T pObject) {
				return (pObject.equals(pItem));
			}
		});
		return this;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
