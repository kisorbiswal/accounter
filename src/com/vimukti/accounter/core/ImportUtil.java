package com.vimukti.accounter.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class ImportUtil extends ObjectConvertUtil {

	private Map<String, IAccounterServerCore> importedObjects = new HashMap<String, IAccounterServerCore>();

	@SuppressWarnings("unchecked")
	private <D extends IAccounterServerCore, S extends IAccounterCore> Object toServerList(
			List<?> set) {
		if (set == null)
			return null;
		ArrayList result = new ArrayList();
		if (set.size() == 0)
			return result;
		Class class1 = getServerEqivalentClass(set.iterator().next().getClass());
		for (Object src : set) {
			if (isNotMappingEntity(class1)) {
				result.add(createObjectWithPrimitiveValues(null, (S) src));
			} else {
				D obj = (D) toServerObject(null, (S) src);
				if (obj != null)
					result.add(modifyObjectWithReferanceValues(obj, (S) src));
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private <D extends IAccounterServerCore, S extends IAccounterCore> Object toServerSet(
			Set<?> set) {
		if (set == null)
			return null;
		HashSet result = new HashSet();
		if (set.size() == 0)
			return result;
		Object next = set.iterator().next();
		if (next != null) {
			Class class1 = getServerEqivalentClass(set.iterator().next()
					.getClass());
			for (Object src : set) {
				if (isNotMappingEntity(class1)) {
					result.add(createObjectWithPrimitiveValues(null, (S) src));
				} else {
					D obj = (D) toServerObject(null, (S) src);
					if (obj != null)
						result
								.add(modifyObjectWithReferanceValues(obj,
										(S) src));
				}
			}
		}
		return result;
	}

	@SuppressWarnings( { "unchecked", "null" })
	private <D extends IAccounterServerCore, S extends IAccounterCore> void modifyList(
			Field desField, Field srcField, Object dst, Object src) {

		try {
			List<S> list = null;

			if (isSet(srcField.getType())) {
				HashSet set = (HashSet) toServerSet((Set<?>) srcField.get(src));
				if (set != null)
					list.addAll(set);

			} else

				list = (List<S>) toServerList((List<S>) srcField.get(src));
			desField.set(dst, list);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private <D extends IAccounterServerCore, S extends IAccounterCore> void modifySet(
			Field desField, Field srcField, Object dst, Object src) {
		try {

			Set<S> set = new HashSet<S>();

			if (isList(srcField.getType())) {
				List list = (List) toServerList((List<?>) srcField.get(src));
				if (list != null)
					set.addAll(list);

			} else

				set = (Set<S>) toServerSet((Set<S>) srcField.get(src));

			desField.set(dst, set);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Import Client Object into Server Objects
	 * 
	 * @param <D>
	 * @param <S>
	 * @param srcList
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public <D extends IAccounterServerCore, S extends IAccounterCore> List<D> toServerObjects(
			List<S> srcList) {

		List<D> serverList = new ArrayList<D>();
		for (S core : srcList) {
			D dst = createObjectWithPrimitiveValues(null, core);
			importedObjects.put(core.getStringID(), dst);
		}
		for (S core : srcList) {
			D dst = modifyObjectWithReferanceValues(
					(D) getObjectFromCacheMapByStringID(core.getStringID()),
					core);
			if (dst != null)
				serverList.add(dst);
		}

		return serverList;
	}

	/**
	 * Convert Imported Object to Server Object
	 * 
	 * @param <D>
	 * @param <S>
	 * @param dst
	 * @param core
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <D extends IAccounterServerCore, S extends IAccounterCore> D toServerObject(
			D dst, S core) {
		cache.set(null);
		dst = createObjectWithPrimitiveValues(dst, core);

		importedObjects.put(core.getStringID(), dst);

		dst = modifyObjectWithReferanceValues(
				(D) getObjectFromCacheMapByStringID(core.getStringID()), core);
		cache.set(null);
		return dst;
	}

	@SuppressWarnings("unchecked")
	public <D extends IAccounterServerCore, S extends IAccounterCore> D createObjectWithPrimitiveValues(
			D dst, S src) {
		try {

			if (src == null)
				return null;
			D dt = null;
			if (dst == null) {
				Class<D> serverCoreClass = getServerEqivalentClass(src
						.getClass());
				if (!isNotMappingEntity(serverCoreClass)) {
					// dst = (D) session.get(serverCoreClass, src.getId());
					if (src.getStringID() != null)
						dt = (D) getObjectFromCacheMapByStringID(src
								.getStringID());
				}
				dst = dt == null ? dst == null ? serverCoreClass.newInstance()
						: dst : dt;
			}

			Class<S> srcType = (Class<S>) src.getClass();

			Class<D> dstType = (Class<D>) dst.getClass();

			Map<String, Field> srcMap = getAllFields(srcType);

			Map<String, Field> dstMap = getAllFields(dstType);

			for (String dstFieldName : dstMap.keySet()) {

				Field dstField = dstMap.get(dstFieldName);

				Field srcField = srcMap.get(dstFieldName);

				if (srcField == null)
					continue;

				if ((srcField.getModifiers() & Modifier.STATIC) > 0)
					continue;

				srcField.setAccessible(true);

				dstField.setAccessible(true);

				Class<S> srcfieldType = (Class<S>) srcField.getType();

				Class<D> dstfieldType = (Class<D>) dstField.getType();

				// For Collection Objects
				if (srcField.get(src) == null)
					continue;

				if (dstfieldType.isPrimitive()) {

					if (srcfieldType.equals(int.class)) {
						if (srcField.getInt(src) == 0)
							continue;
					}
					if (srcfieldType.equals(long.class)) {
						if (srcField.getLong(src) == 0)
							continue;
					}

					if (srcfieldType.equals(double.class)) {
						if (DecimalUtil.isEquals(srcField.getDouble(src), 0.0d))
							continue;
					}

					// if Primitive
					if (srcfieldType.isPrimitive()) {

						dstField.set(dst, srcField.get(src));

					}

				} else if (isPrimitiveWrapper(dstfieldType)) {
					if (srcField.get(src) != null)
						dstField.set(dst, srcField.get(src));
				} else {

					// its A Non Collection Reference

					if (srcfieldType.isPrimitive()) {
						// Check Whether its Date

						long longValue = srcField.getLong(src);
						if (longValue == 0)
							continue;

						if (isFinanceDate(dstfieldType)) {
							dstField.set(dst, new FinanceDate(longValue));

						}
					}
					if (isString(dstField.getType())) {

						dstField.set(dst, srcField.get(src));

					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dst;

	}

	@SuppressWarnings("unchecked")
	public <D extends IAccounterServerCore, S extends IAccounterCore> D modifyObjectWithReferanceValues(
			D dst, S src) {
		try {

			if (src == null)
				return null;

			D dt = null;
			if (dst == null) {
				Class<D> serverCoreClass = getServerEqivalentClass(src
						.getClass());
				if (!isNotMappingEntity(serverCoreClass)) {
					// dst = (D) session.get(serverCoreClass, src.getId());
					if (src.getStringID() != null)
						dt = (D) getObjectFromCacheMapByStringID(src
								.getStringID());
				}
				dst = dt == null ? dst == null ? serverCoreClass.newInstance()
						: dst : dt;
			}

			getCache().put(src, dst);

			Class<S> srcType = (Class<S>) src.getClass();

			Class<D> dstType = (Class<D>) dst.getClass();

			Map<String, Field> srcMap = getAllFields(srcType);

			Map<String, Field> dstMap = getAllFields(dstType);

			for (String dstFieldName : dstMap.keySet()) {

				Field dstField = dstMap.get(dstFieldName);

				Field srcField = srcMap.get(dstFieldName);

				if (srcField == null)
					continue;

				if ((srcField.getModifiers() & Modifier.STATIC) > 0)
					continue;

				srcField.setAccessible(true);

				dstField.setAccessible(true);

				Class<S> srcfieldType = (Class<S>) srcField.getType();

				Class<D> dstfieldType = (Class<D>) dstField.getType();

				// For Collection Objects
				if (srcField.get(src) == null)
					continue;

				if (isPrimitive(srcfieldType) && isPrimitive(dstfieldType))
					continue;
				if (isPrimitiveWrapper(dstfieldType)
						|| isPrimitiveWrapper(srcfieldType)) {
					continue;
				}

				if (isSet(dstfieldType)) {

					modifySet(dstField, srcField, dst, src);

				} else if (isList(dstfieldType)) {

					modifyList(dstField, srcField, dst, src);

				} else if (isMap(dstfieldType)
						|| srcField.get(src) instanceof Map) {
					Map<?, ?> map = toMap((Map) srcField.get(src));
					dstField.set(dst, map);
				} else {

					// its A Non Collection Reference

					// Its Not Primitive and Non Collection Object

					if ((isString(srcfieldType) && !isString(dstfieldType))
							&& (srcField.getName().equals(dstField.getName()))) {
						if (isNotMappingEntity(dstfieldType)) {
							dstField.set(dst, modifyObjectWithReferanceValues(
									(D) dstField.get(dst), (S) srcField
											.get(src)));
						} else {
							dstField
									.set(
											dst,
											getObjectFromCacheMapByStringID((String) srcField
													.get(src)));
						}

					} else if (isString(dstField.getType())) {

						dstField.set(dst, srcField.get(src));

					} else {

						dstField.set(dst, getServerAfterCheckingInCache(
								(D) dstField.get(dst), (S) srcField.get(src)));

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dst;

	}

	private Map<?, ?> toMap(Map map) {
		Map<Object, Object> imprtedMap = new HashMap<Object, Object>();
		for (Object key : map.keySet()) {
			Object val = map.get(key);
			Object clientKey = null;
			if (key instanceof IAccounterServerCore) {
				clientKey = modifyObjectWithReferanceValues(null,
						(IAccounterCore) val);
			}
			if (val instanceof IAccounterServerCore) {
				val = modifyObjectWithReferanceValues(null,
						(IAccounterCore) val);
			}
			if (clientKey == null)
				imprtedMap.put(key, val);
			else {
				imprtedMap.put(clientKey, val);
			}
		}
		return imprtedMap;
	}

	@SuppressWarnings( { "unused" })
	private <D extends IAccounterServerCore, S extends IAccounterCore> D toServerObjectInternal(
			D dst, S src) {

		return dst;

	}

	@SuppressWarnings("unchecked")
	private <D extends IAccounterServerCore> D getObjectFromCacheMapByStringID(
			String Id) {

		if (Id != null && Id.length() != 0) {
			return (D) importedObjects.get(Id);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <D extends IAccounterServerCore, S extends IAccounterCore> D getServerAfterCheckingInCache(
			D dst, S src) {
		Map<Object, Object> localCache = getCache();

		if (src == null) {
			return null;
		}
		D ret = (D) localCache.get(src) == null ? (D) getObjectFromCacheMapByStringID(src
				.getStringID())
				: (D) localCache.get(src);
		if (ret == null) {
			ret = modifyObjectWithReferanceValues(dst, src);
			ret = createObjectWithPrimitiveValues(dst, src);
			localCache.put(src, ret);
		}
		return ret;
	}

}
