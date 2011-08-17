package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VList<E> extends ArrayList<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ListFilter<E>> filters;
	private List<ListListener<E>> listeners;
	private Map<ListFilter<E>, ListListener<E>> filterListeners;

	public VList() {
		filters = new ArrayList<ListFilter<E>>();
		listeners = new ArrayList<ListListener<E>>();
		filterListeners = new HashMap<ListFilter<E>, ListListener<E>>();
	}

	public VList(Collection<E> collection) {
		this();
		addAll(collection);
	}

	public void addFilter(ListFilter<E> listFilter) {
		filters.add(listFilter);

	}

	public void addListener(ListListener<E> listListener) {
		listeners.add(listListener);
	}

	public void removeListener(ListListener<E> listListener) {
		listeners.remove(listListener);
	}

	public void removeFilter(ListFilter<E> listFilter) {
		filters.remove(listFilter);
	}

	public void addListener(ListFilter<E> listFilter,
			ListListener<E> listListener) {
		filterListeners.put(listFilter, listListener);
	}

	public VList<E> filter(final ListFilter<E> listFilter) {
		final VList<E> filteredList = new VList<E>();
		for (E e : this) {
			if (listFilter.filter(e)) {
				filteredList.add(e);
			}
		}

		DummyListener<E> thisListListener = new DummyListener<E>() {

			@Override
			public void onAdd(E e) {
				filteredList.addInternal(e, getExcept());
			}

			@Override
			public void onRemove(Object e) {
				filteredList.removeInternal(e, getExcept());
			}
		};

		DummyListener<E> newListListener = new DummyListener<E>() {

			@Override
			public void onAdd(E e) {
				addInternal(e, getExcept());
			}

			@Override
			public void onRemove(Object e) {
				removeInternal(e, getExcept());
			}
		};

		thisListListener.setExcept(newListListener);
		newListListener.setExcept(thisListListener);

		filteredList.addListener(newListListener);

		this.addListener(listFilter, thisListListener);
		return filteredList;
	}

	public VList<E> and(final VList<E> list) {
		final VList<E> filteredList = new VList<E>();
		for (E e : this) {
			if (list.contains(e)) {
				filteredList.add(e);
			}
		}
		list.addListener(new ListListener<E>() {

			@Override
			public void onAdd(E e) {
				if (contains(e)) {
					filteredList.add(e);
				}
			}

			@Override
			public void onRemove(Object e) {
				filteredList.remove(e);
			}
		});

		this.addListener(new ListListener<E>() {

			@Override
			public void onAdd(E e) {
				if (list.contains(e)) {
					filteredList.add(e);
				}
			}

			@Override
			public void onRemove(Object e) {
				filteredList.remove(e);
			}
		});
		return filteredList;
	}

	public VList<E> or(final VList<E> list) {
		final VList<E> filteredList = new VList<E>();
		for (E e : this) {
			filteredList.add(e);
		}

		for (E e : filteredList) {
			if (!list.contains(e)) {
				add(e);
			}
		}
		list.addListener(new ListListener<E>() {

			@Override
			public void onAdd(E e) {
				filteredList.add(e);
			}

			@Override
			public void onRemove(Object e) {
				if (!contains(e)) {
					filteredList.remove(e);
				}
			}
		});
		this.addListener(new ListListener<E>() {

			@Override
			public void onAdd(E e) {
				filteredList.add(e);
			}

			@Override
			public void onRemove(Object e) {
				if (!list.contains(e)) {
					filteredList.remove(e);
				}
			}
		});
		return filteredList;
	}

	public boolean add(E e) {
		add(size(), e);
		return true;
	}

	public boolean addInternal(E e, ListListener<E> except) {
		addInternal(size(), e, null);
		return true;
	}

	public void add(int index, E e) {
		addInternal(index, e, null);
	}

	private void addInternal(int index, E e, ListListener<E> except) {
		for (ListFilter<E> filter : filters) {
			if (!filter.filter(e)) {
				return;
			}
		}
		super.add(e);

		for (ListListener<E> listener : listeners) {
			if (listener == except) {
				continue;
			}
			listener.onAdd(e);
		}

		Set<ListFilter<E>> keySet = filterListeners.keySet();
		for (ListFilter<E> key : keySet) {
			if (key.filter(e)) {
				ListListener<E> listener = filterListeners.get(key);
				if (listener == except) {
					continue;
				}
				listener.onAdd(e);
			}
		}
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> collection) {
		Iterator<? extends E> iterator = collection.iterator();
		while (iterator.hasNext()) {
			E e = iterator.next();
			this.add(index++, e);
		}
		return collection.size() > 0 ? true : false;
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		Iterator<? extends E> iterator = collection.iterator();
		while (iterator.hasNext()) {
			E e = iterator.next();
			this.add(e);
		}
		return collection.size() > 0 ? true : false;
	}

	@Override
	public E remove(int index) {
		E e = get(index);
		remove(e);
		return e;
	}

	public boolean remove(Object e) {
		return removeInternal(e, null);
	}

	public boolean removeInternal(Object e, ListListener<E> except) {
		E ee = (E) e;
		boolean result = super.remove(e);
		for (ListListener<E> listener : listeners) {
			if (listener == except) {
				continue;
			}
			listener.onRemove(ee);
		}
		Set<ListFilter<E>> keySet = filterListeners.keySet();
		for (ListFilter<E> key : keySet) {
			if (key.filter(ee)) {
				ListListener<E> listener = filterListeners.get(key);
				if (listener == except) {
					continue;
				}
				listener.onRemove(ee);
			}
		}
		return result;
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		for (int i = fromIndex; i < toIndex; i++) {
			remove(i);
		}
	}

	@Override
	public E set(int index, E e) {
		E remove = remove(index);
		add(index, e);
		return remove;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		Iterator<?> iterator = collection.iterator();
		boolean result = false;
		while (iterator.hasNext()) {
			Object e = iterator.next();
			result = result || remove(e);
		}
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		Iterator<?> iterator = this.iterator();
		boolean result = false;
		while (iterator.hasNext()) {
			Object e = iterator.next();
			if (!collection.contains(e)) {
				result = result || remove(e);
			}
		}
		return result;
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		for (E e : this) {
			remove(e);
		}
	}

	abstract class DummyListener<EE> implements ListListener<EE> {
		private ListListener<EE> except;

		public ListListener<EE> getExcept() {
			return except;
		}

		public void setExcept(ListListener<EE> except) {
			this.except = except;
		}
	}
}
