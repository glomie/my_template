package com.temp.designPatterns.iterator;

import java.util.List;

public class ConcreteIterator implements Iterator {

	private List<PayModel> list;
	private int cursor = 0;

	public ConcreteIterator(List<PayModel> list) {
		this.list = list;
	}

	@Override
	public void first() {
		cursor = 0;
	}

	@Override
	public void next() {
		cursor++;
	}

	@Override
	public boolean isDone() {
		return cursor >= list.size();
	}

	@Override
	public Object currentItem() {
		if (isDone()) {
			return null;
		}
		return list.get(cursor);
	}

}
