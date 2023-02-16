package com.etc.admin.data.queue;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Embedded;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.etc.CoreException;
import com.etc.admin.AdminApp;
import com.etc.admin.data.LocalDataManager;
import com.etc.corvetto.ems.pipeline.entities.cov.CoverageFile;
import com.etc.entities.CoreData;

/**
 * <p>
 * QueuedDatabaseOptionalUpdate uses a provided HashMap containing a Pair of the <br>
 * parent class type and its corresponding id as the key, and a List of optional mappings<br>
 * as the value to attach the previously persisted or updated optional entities on the parent.<br>
 * Each item in the mapping list is iterated over, and its corresponding entity fetched locally <br>
 * before being set on the parent. If the entity cannot be located locally, a flag is set and <br>
 * after the operation the parent entity will remain as partialLoad in the database. Otherwise, <br>
 * if all optionals are found and set, the entity is marked as partialLoad false or complete.</p>
 * 
 * @author Stephen Carter
 * @since 09/23/2021
 *
 * @param <T> Parent Entity Type
 */
public class QueuedDatabaseOptionalUpdate<T extends CoreData> implements Runnable, Serializable {

	
	private static final long serialVersionUID = 4618689233750809386L;
	
	@SuppressWarnings("rawtypes")
	HashMap entityMappings,optionalMap;
	Logger logr;
	Class<T> clazz;
	
	private QueuedDatabaseOptionalUpdate() { super(); }
	
	public <X extends CoreData> QueuedDatabaseOptionalUpdate(HashMap<Pair<Class<T>,Long>,List<Triple<Class<X>,Long,String>>> entityMappings, Class<T> clazz)
	{
		this();
		this.entityMappings = entityMappings;
		this.optionalMap = new HashMap<Class<X>,List<Long>>();
		this.clazz = clazz;
		logr = Logger.getLogger(this.getClass().getCanonicalName());
	}
	
	@SuppressWarnings("unchecked")
	public <X extends CoreData> HashMap<Pair<Class<T>,Long>,List<Triple<Class<X>,Long,String>>> getEntityMappings() { return entityMappings; }
	@SuppressWarnings("unchecked")
	public <X extends CoreData> HashMap<Class<X>,List<Long>> getOptionalMap() { return optionalMap; }

	@Override
	public void run() 
	{
		Method setter = null;
		Method getter = null;
		Object embed = null;
		T parent = null;
		boolean failedSetter = false;
		CoreData dependent = null;
		
		if(entityMappings != null && !entityMappings.isEmpty())
		{
			logr.config("Starting QueuedDatabaseOptionalUpdate for Type " + clazz.getName() + ". Size=[" + entityMappings.size() + "].");
			try (LocalDataManager mgr = new LocalDataManager(AdminApp.getInstance().getEntityManager()))
			{
				
				for(Entry<Pair<Class<T>,Long>,List<Triple<Class<CoreData>,Long,String>>> item : getEntityMappings().entrySet())
				{
					if((parent = mgr.getLocal(item.getKey().getLeft(), item.getKey().getRight())) != null)
					{
						for(Triple<Class<CoreData>,Long,String> trp : item.getValue())
						{
							try { getter = parent.getClass().getMethod(StringUtils.replace(trp.getRight(), "set", "get")); }
							catch(NoSuchMethodException e2) { getter = null; }
							
							//get this type's getter
							if(getter != null)
							{
								//check that we do not already have a copy of this type attached
								if((dependent = (CoreData)getter.invoke(parent)) == null)
								{
									//get this type's setter
									if((setter = parent.getClass().getMethod(trp.getRight(), trp.getLeft())) != null)
									{
										//attach the type if it is found locally, otherwise mark as failed (to preserver the partialLoad status of the parent)
										if((dependent = mgr.getLocal(trp.getLeft(), trp.getMiddle())) != null)
											setter.invoke(parent, dependent);
										else
										{
											logr.severe("The Type " + trp.getLeft().getName() + " was not found locally by Id=[" + trp.getMiddle() + "] for the parent type of " + parent.getClass().getName());
											failedSetter = true;
										}
									}else
										logr.severe("Unable to locate setter method: " + trp.getLeft().getName().concat(trp.getRight()));
								}
							}
							//LOOK FOR EMBEDDABLE
							else
							{
								for(Method m : parent.getClass().getMethods())
								{
									if(m.isAnnotationPresent(Embedded.class))
									{
										if(parent.getClass().equals(CoverageFile.class) && parent.getId().equals(30380l))
											logr.info("Test Test");
										if((embed = m.invoke(parent)) != null)
										{
											//get this type's getter
											if((getter = embed.getClass().getMethod(StringUtils.replace(trp.getRight(), "set", "get"))) != null)
											{
												//check that we do not already have a copy of this type attached
												if((dependent = (CoreData)getter.invoke(embed)) == null)
												{
													//get this type's setter
													if((setter = embed.getClass().getMethod(trp.getRight(), trp.getLeft())) != null)
													{
														//attach the type if it is found locally, otherwise mark as failed (to preserver the partialLoad status of the parent)
														if((dependent = mgr.getLocal(trp.getLeft(), trp.getMiddle())) != null)
															setter.invoke(embed, dependent);
														else
														{
															logr.severe("The Type " + trp.getLeft().getName() + " was not found locally by Id=[" + trp.getMiddle() + "] for the parent type of " + embed.getClass().getName());
															failedSetter = true;
														}
													}else
														logr.severe("Unable to locate setter method: " + trp.getLeft().getName().concat(trp.getRight()));
												}
												break;
											}
										}
									}
								}
							}
						}
					}else
						throw new CoreException("Unable to locate local copy of parent entity Type: " + clazz.getName() + " Id=[" + item.getKey().getRight() + "].");
					//SET IF WE HAVE SUCCESSFULLY ADDED ALL OPTINALS
					parent.setPartialLoad(failedSetter);
				}
				
				logr.config("Completed QueuedDatabaseOptionalUpdate for type " + clazz.getName());
			}catch(Exception e)
			{
				logr.log(Level.SEVERE, "Failure in QueuedDatabaseOptionalUpdate. Exception: ", e);
			}
		}else
			logr.severe("The entityMapping list was empty or null.");
	}

}
