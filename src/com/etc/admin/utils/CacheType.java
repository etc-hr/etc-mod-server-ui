package com.etc.admin.utils;

import java.io.Serializable;
import java.util.Objects;

import com.etc.entities.CoreData;

/**
 * <p>
 * CacheType<T,X> is used to control the caching of entities and their filter types.<br>
 * The CacheType is used as the key to a HashMap to control and throttle caching operations<br>
 * of required and optional relationships as well as top level entities, preventing multiple<br>
 * caching operations.</p>
 * 
 * @author Stephen Carter
 *
 * @param <T> The root type
 * @param <X> The filter type
 */
public class CacheType<T extends CoreData, X extends CoreData> implements Serializable
{
	private static final long serialVersionUID = -3872036043129343987L;

	private Class<T> rootClass;
	private Class<X> filterClass;
	
	private CacheType() { super(); }
	
	public CacheType(Class<T> root, Class<X> filter)
	{
		this();
		rootClass = root;
		filterClass = filter;
	}

	@Override
	public int hashCode() {
		return Objects.hash(rootClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CacheType<T,X> other = (CacheType<T,X>) obj;
		//SHOULD RETURN A MATCH IF WE HAVE A NON-FILTERED CACHE OR WE FIND A MATCH FOR A FILTERED CACHE
		return (other.filterClass == null || Objects.equals(filterClass, other.filterClass)) && Objects.equals(rootClass, other.rootClass);
	}
	
	public String toString() 
	{
		return (rootClass != null ? rootClass.getName() : "root is null") + ", " + (filterClass != null ? filterClass.getName() : "filter is null");
	}
}
