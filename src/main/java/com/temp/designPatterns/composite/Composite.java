package com.temp.designPatterns.composite;

import java.util.List;

import com.google.common.collect.Lists;

public class Composite extends Component {
	
	private List<Component> childComponents = Lists.newArrayList();

	@Override
	public void someOperation() {
		if(this.childComponents != null) {
			for(Component c : childComponents) {
				c.someOperation();
			}
		}
	}

	@Override
	public void addChild(Component child) {
		childComponents.add(child);
	}
	
	@Override
	public void removeChild(Component child) {
		childComponents.remove(child);
	}
	
	@Override
	public Component getChildren(int index) {
		if(index >= 0 && index < childComponents.size()) {
			return childComponents.get(index);
		}
		return null;
	}
}
