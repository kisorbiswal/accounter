package com.vimukti.accounter.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CloneUtil extends ObjectConvertUtil {
	@SuppressWarnings("unchecked")
	private <D extends IAccounterServerCore, S extends IAccounterServerCore> Object toCloneList(
			List<?> set) {
		if (set == null)
			return null;
		ArrayList result = new ArrayList();
		if (set.size() == 0)
			return result;
		for (Object src : set) {
			result.add(cloneObject(null, (S) src));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private <D extends IAccounterServerCore, S extends IAccounterServerCore> Object toCloneSet(
			Set<?> set) {
		if (set == null)
			return null;
		HashSet result = new HashSet();
		if (set.size() == 0)
			return result;
		Object next = set.iterator().next();
		if (next != null) {
			for (Object src : set) {
				result.add(cloneObject(null, (S) src));
			}
		}
		return result;
	}

	@SuppressWarnings( { "unchecked", "null" })
	private <D extends IAccounterServerCore, S extends IAccounterServerCore> void cloneList(
			Field desField, Field srcField, Object dst, Object src) {

		try {
			List<S> list = null;
			list = (List<S>) toCloneList((List<S>) srcField.get(src));
			desField.set(dst, list);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private <D extends IAccounterServerCore, S extends IAccounterServerCore> void cloneSet(
			Field desField, Field srcField, Object dst, Object src) {
		try {

			Set<S> set = new HashSet<S>();
			set = (Set<S>) toCloneSet((Set<S>) srcField.get(src));
			desField.set(dst, set);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public <D extends IAccounterServerCore, S extends IAccounterServerCore> D clone(
			D dst, S src) {
		cache.set(null);
		D ret = cloneObject(dst, src);
		cache.set(null);
		return ret;
	}

	@SuppressWarnings("unchecked")
	private <D extends IAccounterServerCore, S extends IAccounterServerCore> D cloneObject(
			D dst, S src) {

		try {

			if (src == null)
				return null;

			dst = (D) src.getClass().newInstance();

			getCache().put(src, dst);

			Class<S> srcType = (Class<S>) src.getClass();

			Class<D> dstType = (Class<D>) dst.getClass();

			Map<String, Field> srcMap = getAllFields(srcType);

			Map<String, Field> dstMap = getAllFields(dstType);

			for (String srcFieldName : srcMap.keySet()) {

				Field dstField = dstMap.get(srcFieldName);

				Field srcField = srcMap.get(srcFieldName);

				if (srcField == null)
					continue;

				if ((srcField.getModifiers() & Modifier.STATIC) > 0)
					continue;

				srcField.setAccessible(true);

				dstField.setAccessible(true);

				Class<D> dstfieldType = (Class<D>) dstField.getType();

				// For Collection Objects
				if (srcField.get(src) == null)
					continue;

				if (isRefferedObject(srcField)) {
					dstField.set(dst, srcField.get(src));
					continue;
				}

				if (isSet(dstfieldType)) {

					cloneSet(dstField, srcField, dst, src);

				} else if (isList(dstfieldType)) {

					cloneList(dstField, srcField, dst, src);

				} else if (dstfieldType.isPrimitive()) {
					dstField.set(dst, srcField.get(src));

				} else if (isPrimitiveWrapper(dstfieldType)) {
					if (srcField.get(src) != null)
						dstField.set(dst, srcField.get(src));
				} else if (isFinanceDate(dstfieldType)) {
					dstField.set(dst, srcField.get(src));

				} else if (isString(dstField.getType())) {
					dstField.set(dst, srcField.get(src));
				} else if (srcField.get(src) instanceof IAccounterServerCore) {
					dstField.set(dst, getAfterCheckingInCache((D) dstField
							.get(dst), (S) srcField.get(src)));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dst;

	}

	private boolean isRefferedObject(Field srcField) {

		if (srcField.getAnnotation(ReffereredObject.class) != null)
			return true;

		return false;
	}

	@SuppressWarnings("unchecked")
	private <D extends IAccounterServerCore, S extends IAccounterServerCore> D getAfterCheckingInCache(
			D dst, S src) {
		Map<Object, Object> localCache = getCache();

		if (src == null) {
			return null;
		}
		D ret = (D) localCache.get(src);
		if (ret == null) {
			ret = cloneObject(dst, src);
			localCache.put(src, ret);
		}
		return ret;
	}

}
