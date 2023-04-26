package com.boot.exam.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NotNullUtil {

	public static boolean isBlank(Object src) {
		return isBlankAlert(src) != null;
	}

	public static String isBlankAlert(Object src) {
		// 获得对象中所有的属性
		Field[] fields = src.getClass().getDeclaredFields();
		for (Field f : fields) {
			try { // 判断属性是否有NotNull注解
			if (f.isAnnotationPresent(NotNull.class)) {
				// 打开权限
				f.setAccessible(true);
				// 获得属性名字
				String name = f.getName();
				// f.getType()为获得属性数据类型
				String pref = f.getType().isAssignableFrom(Boolean.class) ? "is" : "get";
				// 获得get方法名字
				String getter = pref + name.substring(0, 1).toUpperCase() + name.substring(1);
				// 获得get方法
				Method method = src.getClass().getMethod(getter);
				// 验证该属性值是否为null
				Object obj = method.invoke(src);
				if (obj instanceof String) {
					String str = (String) obj;
					if ("".equals(str.trim())) return toAlert(f);
				} else {
					if (obj == null) return toAlert(f);
				}
			}} catch (Exception e) { e.printStackTrace(); }
		}
		return null;
	}

	private static String toAlert(Field f) {
		NotNull notNull = f.getAnnotation(NotNull.class);
		String alert = notNull.value();
		if ("".equals(alert)) {
			alert = notNull.fieldName();
			return "请填写" + alert + "！";
		}
		return alert;
	}

}
