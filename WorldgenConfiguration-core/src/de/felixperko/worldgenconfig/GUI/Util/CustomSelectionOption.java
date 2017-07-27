package de.felixperko.worldgenconfig.GUI.Util;

/*
 * a custom option in a select list
 */

@SuppressWarnings("rawtypes")
public class CustomSelectionOption<T extends WorldgenSelectBox> extends SelectWrapper implements Comparable<CustomSelectionOption>{
	
	public double orderPriority;
	public double defaultOptionPriority;
	
	public CustomSelectionOption(String name, double orderPriority, double defaultOptionPriority) {
		super(name);
		this.orderPriority = orderPriority;
		this.defaultOptionPriority = defaultOptionPriority;
	}
	
	public void onSelect(T box){}
	
	@Override
	public int compareTo(CustomSelectionOption o) {
		if (orderPriority < o.orderPriority)
			return 1;
		else if (orderPriority > o.orderPriority)
			return -1;
		return 0;
	}
}
