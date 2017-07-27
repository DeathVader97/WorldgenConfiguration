package de.felixperko.worldgenconfig.MainMisc.Utilities.Options.OptionSystem;

import de.felixperko.worldgenconfig.MainMisc.Utilities.Events.EventSystem.EventManager;

public class OptionBuilder<T> {
	
	public T storedValue;
	public String fieldName;
	
	
//	@SuppressWarnings("unchecked")
//	public Class<? extends Option<?>> optionClass = (Class<? extends Option<?>>) Option.class;
	
//	@SuppressWarnings("unchecked")
//	public Option<T> build(EventManager manager){
//		try {
//			
//			Class<T> cls = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//			Constructor<? extends Option<?>> constructor = optionClass.getDeclaredConstructor(OptionBuilder.class, cls, EventManager.class);
//			return ((Constructor<? extends Option<T>>)constructor).newInstance(this, storedValue, manager);
//			
//		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	public Option<T> build(EventManager manager){
		return new Option<T>(this, storedValue, manager);
	}
	
	public String getFieldName(){
		return fieldName;
	}

	public T getStoredValue() {
		return storedValue;
	}

	public void setStoredValue(T storedValue) {
		this.storedValue = storedValue;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

//	public Class<? extends Option<?>> getOptionClass() {
//		return optionClass;
//	}
//
//	public void setOptionClass(Class<? extends Option<?>> optionClass) {
//		this.optionClass = optionClass;
//	}
	
}
