package cn.k.mybatis_learn.interceptor;

import java.lang.reflect.Field;

public class BeanUtil {

	
	static public Object setFiledValue(Object obj, String fieldName,Object value){
		if (obj == null)
			return null;

		Class<?> objClass = obj.getClass();
		Object fieldValue = null;
		do {
			try {
				FindResult result = setFieldValue(objClass, obj, fieldName, value);
				if(result.findResult) {
					return result.fieldValue;
				} else {
					objClass = objClass.getSuperclass();
				}
			} catch (Exception e) {
				return null;
			}
		} while (objClass != null);

		return fieldValue;
	}

	static public Object getFieldValue(Object obj, String fieldName) {

		if (obj == null)
			return null;

		Class<?> objClass = obj.getClass();
		Object fieldValue = null;
		do {
			try {
				FindResult result = getFieldValue(objClass, obj, fieldName);
				if(result.findResult) {
					return result.fieldValue;
				} else {
					objClass = objClass.getSuperclass();
				}
			} catch (Exception e) {
				return null;
			}
		} while (objClass != null);

		return fieldValue;
	}

	private static FindResult getFieldValue(Class<?> objClass, Object obj, String fieldName)
			throws IllegalArgumentException, IllegalAccessException {
		
		for (Field field : objClass.getDeclaredFields()) {
			boolean fieldAccessible = field.isAccessible();
			field.setAccessible(true);
			try {
				if (field.getName().equals(fieldName)) {
					return FindResult.createFoundResult(field.get(obj));
				}
			} finally {
				field.setAccessible(fieldAccessible);
			}
		}
		return FindResult.createNotFoundResult();
	}
	
	private static FindResult setFieldValue(Class<?> objClass, Object obj, String fieldName,Object value)
			throws IllegalArgumentException, IllegalAccessException {
		
		for (Field field : objClass.getDeclaredFields()) {
			boolean fieldAccessible = field.isAccessible();
			field.setAccessible(true);
			try {
				if (field.getName().equals(fieldName)) {
					field.set(obj, value);
					return FindResult.createFoundResult(value);
				}
			} finally {
				field.setAccessible(fieldAccessible);
			}
		}
		return FindResult.createNotFoundResult();
	}
	
	private static class FindResult {
		
		private boolean findResult;
		
		private Object fieldValue;
		
		private static FindResult createFoundResult(Object fieldValue) {
			FindResult result = new FindResult();
			result.findResult = true;
			result.fieldValue = fieldValue;
			return result;
		}
		
		private static FindResult createNotFoundResult() {
			FindResult result = new FindResult();
			result.findResult = false;
			return result;
		}
	}
}