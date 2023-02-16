package com.etc.admin.data.queue;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Embedded;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.etc.admin.AdminApp;
import com.etc.admin.data.LocalDataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.corvetto.rqs.CorvettoRequest;
import com.etc.entities.CoreData;

/**
 * <p>
 * QueuedDatabaseRequiredUpdate uses a provided HashMap containing the parent entity as the key,<br>
 * and a Pair of lists containing mappings as the value. The entries of this HashMap are iterated<br>
 * over. The left side of the Pair is a List of required mappings for the parent. These are then <br>
 * iterated over and all required relationships are set. In most cases the local copy (in the local db)<br>
 * will be the most up to date and used. If the attached copy is more up to date, this version is then<br>
 * updated and refreshed before being set on the parent.</p>
 * <p>
 * Optional realtionships are treated differently. In almost all cases, optional relationships will depend<br>
 * on the parent to exist in the database prior to the dependents' existence there. To accomodate this, the<br>
 * optionals are stripped off of the parent before the parent is persisted/updated, and added to a separate <br>
 * HashMap containing the parents' type and id as the key, and the optional mapping as the value. A separate map<br>
 * containing a class type as the key and a list of longs as the value is used to fetch all the optional types <br>
 * that are needed for this list of parent entities. Fetching these types automatically queues up subsequent <br>
 * instances of this type for those dependent types. After this list has been iterated over and all types have <br>
 * been fetched, the hashmap containing the parent type/id key and optional mapping value is passed into the <br>
 * QueuedDatabaseOptionalUpdate type.</p>
 * 
 * @author Stephen Carter 
 * @since 09/01/2021
 * @param <T>  the Parent Type
 */
public class QueuedDatabaseRequiredUpdate<T extends CoreData> implements Runnable, Serializable {

	
	private static final long serialVersionUID = 4618689233750809386L;
	
	@SuppressWarnings("rawtypes")
	HashMap entityMappings,optionalEntityMappings,optionalMap;
	Logger logr;
	Class<T> clazz;
	
	private QueuedDatabaseRequiredUpdate() { super(); }
	
	public <X extends CoreData> QueuedDatabaseRequiredUpdate(HashMap<T,Pair<List<Triple<Class<X>,Long,String>>,List<Triple<Class<X>,Long,String>>>> entityMappings, Class<T> clazz)
	{
		this();
		this.entityMappings = entityMappings;
		this.optionalEntityMappings = new HashMap<Pair<Class<T>,Long>,List<Triple<Class<X>,Long,String>>>();
		this.optionalMap = new HashMap<Class<X>,List<Long>>();
		this.clazz = clazz;
		logr = Logger.getLogger(this.getClass().getCanonicalName());
	}
	
	@SuppressWarnings("unchecked")
	public <X extends CoreData> HashMap<T,Pair<List<Triple<Class<X>,Long,String>>,List<Triple<Class<X>,Long,String>>>> getEntityMappings() { return entityMappings; }
	@SuppressWarnings("unchecked")
	public <X extends CoreData> HashMap<Pair<Class<T>,Long>,List<Triple<Class<X>,Long,String>>> getOptionalEntityMappings() { return optionalEntityMappings; }
	@SuppressWarnings("unchecked")
	public <X extends CoreData> HashMap<Class<X>,List<Long>> getOptionalMap() { return optionalMap; }
	
	
	@Override
	public void run() 
	{
		Method getter = null;
		Method setter = null;
		
		Object embed = null;
		CoreData parent = null;
		CoreData dependent = null;
		CoreData localDependent = null;
		
		if(entityMappings != null && !entityMappings.isEmpty())
		{
			logr.config("Starting QueuedDatabaseRequiredUpdate for Type " + clazz.getName() + ". Size=[" + entityMappings.size() + "].");
			
			try
			{
				try (LocalDataManager mgr = new LocalDataManager(AdminApp.getInstance().getEntityManager()))
				{
					for(Entry<T,Pair<List<Triple<Class<CoreData>,Long,String>>,List<Triple<Class<CoreData>,Long,String>>>> item : getEntityMappings().entrySet())
					{
						//double-check the entity was not updated/persisted prior to this queue running.
						if((parent = mgr.getLocal(clazz, item.getKey().getId())) == null || item.getKey().getLastUpdated().after(parent.getLastUpdated()))
						{
							if(parent != null)
								logr.warning("Type " + parent.getClass().getName() + " was found to be out of date. Local date: " + parent.getLastUpdated().getTime() + ". Server date: " + item.getKey().getLastUpdated().getTime());
							
							//PROCESS AND ATTACH REQUIRED RELATIONSHIPS
							if(item.getValue().getLeft() != null && !item.getValue().getLeft().isEmpty())
							{
								//ITERATE OVER REQUIRED FIELDS- CHECK IF A LOCAL COPY THAT IS UP TO DATE EXISTS
								for(Triple<Class<CoreData>,Long,String> rqd : item.getValue().getLeft())
								{
									try { getter = item.getKey().getClass().getMethod(StringUtils.replace(rqd.getRight(), "set", "get")); }
									catch(NoSuchMethodException e2) { getter = null; }
									
									if(getter != null)
									{
										if((dependent = (CoreData)getter.invoke(item.getKey())) != null)
											//local copy did not exist or was out of date
											if((localDependent = mgr.getLocal(rqd.getLeft(), rqd.getMiddle())) == null || dependent.getLastUpdated().after(localDependent.getLastUpdated()))
											{
												if((setter = item.getKey().getClass().getMethod(rqd.getRight(), rqd.getLeft())) != null)
												{
													dependent.setPartialLoad(true);
													//update/build requirements for partially loaded required dependent
													setter.invoke(item.getKey(), mgr.updateEntity(dependent));
												}
											}
											//set local copy, it's up to date.
											else
												if((setter = item.getKey().getClass().getMethod(rqd.getRight(), rqd.getLeft())) != null)
												{
													setter.invoke(item.getKey(), localDependent);
												}
									}
									//LOOK FOR EMBEDDABLE
									else
									{
										for(Method m : item.getKey().getClass().getMethods())
										{
											if(m.isAnnotationPresent(Embedded.class))
											{
												if((embed = m.invoke(item.getKey())) != null)
												{
													//get this type's getter
													if((getter = embed.getClass().getMethod(StringUtils.replace(rqd.getRight(), "set", "get"))) != null)
													{
														if((dependent = (CoreData)getter.invoke(embed)) != null)
															//local copy did not exist or was out of date
															if((localDependent = mgr.getLocal(rqd.getLeft(), rqd.getMiddle())) == null || dependent.getLastUpdated().after(localDependent.getLastUpdated()))
															{
																if((setter = embed.getClass().getMethod(rqd.getRight(), rqd.getLeft())) != null)
																{
																	dependent.setPartialLoad(true);
																	//update/build requirements for partially loaded required dependent
																	setter.invoke(embed, mgr.updateEntity(dependent));
																}
															}
															//set local copy, it's up to date.
															else
																if((setter = embed.getClass().getMethod(rqd.getRight(), rqd.getLeft())) != null)
																{
																	setter.invoke(embed, localDependent);
																}
														break;
													}
												}
											}
										}
									}
								}
							//PROCESS AND CLEAR OUT OPTIONAL/ADD TO OPTIONAL MAP IF THE ENTITY IS EITHER NULL OR NOT MANAGED (ALREADY IN DB)
							}if(item.getValue().getRight() != null && !item.getValue().getRight().isEmpty())
							{
								//ITERATE OVER OPTIONAL FIELDS- CHECK IF A LOCAL COPY THAT IS UP TO DATE EXISTS
								for(Triple<Class<CoreData>,Long,String> opt : item.getValue().getRight())
								{
									try { getter = item.getKey().getClass().getMethod(StringUtils.replace(opt.getRight(), "set", "get")); }
									catch(NoSuchMethodException e2) { getter = null; }
									if(getter != null)
									{
										if((dependent = (CoreData)getter.invoke(item.getKey())) != null && !mgr.getEntityManager().contains(dependent))
										{
											//null this optional field before adding the parent into the db
											dependent = null;
											if((setter = item.getKey().getClass().getMethod(opt.getRight(), opt.getLeft())) != null)
												setter.invoke(item.getKey(), dependent);
										}
									}
									//LOOK FOR EMBEDDABLE TYPE
									else
									{
										for(Method m : item.getKey().getClass().getMethods())
										{
											if(m.isAnnotationPresent(Embedded.class))
											{
												if((embed = m.invoke(item.getKey())) != null)
												{
													if((getter = embed.getClass().getMethod(StringUtils.replace(opt.getRight(), "set", "get"))) != null)
													{
														if((dependent = (CoreData)getter.invoke(embed)) != null && !mgr.getEntityManager().contains(dependent))
														{
															//null this optional field before adding the parent into the db
															dependent = null;
															if((setter = embed.getClass().getMethod(opt.getRight(), opt.getLeft())) != null)
																setter.invoke(embed, dependent);
														}
														break;
													}
												}
											}
										}
									}
								}
												
							}else
								//no optional fields exist, set partial load to false
								item.getKey().setPartialLoad(false);
							
							//update parent with required fields
							mgr.updateEntity(item.getKey());
							//create mapping for optional fields update
							getOptionalEntityMappings().put(Pair.of(clazz, item.getKey().getId()), item.getValue().getRight());
							//add optional type and ids to map
							for(Triple<Class<CoreData>,Long,String> trp : item.getValue().getRight())
								if(!getOptionalMap().containsKey(trp.getLeft()))
									getOptionalMap().put(trp.getLeft(), new ArrayList<Long>(Arrays.asList(trp.getMiddle())));
								else
								{
									if(!getOptionalMap().get(trp.getLeft()).contains(trp.getMiddle()))
										getOptionalMap().get(trp.getLeft()).add(trp.getMiddle());
								}
						}
					}
				}
				
				//iterate over optional types and create pulls for those types by id lists
				for(Entry<Class<CoreData>,List<Long>> optional : getOptionalMap().entrySet())
				{
					if(optional.getValue() != null && !optional.getValue().isEmpty())
					{
						CorvettoRequest<CoreData> rqs = new CorvettoRequest<CoreData>(optional.getKey());
						rqs.setIdList(optional.getValue());
						rqs.setFetchInactive(true);
						//this pulls the list of all optionals by this type, creates queue update
						AdminPersistenceManager.getInstance().getAll(rqs);
					}
				}
				
				//re-queue this list to attach the optionals.
				if(!getOptionalEntityMappings().isEmpty())
				{
					logr.config("Creating queuedDatabaseOptionalUpdate for Type " + clazz.getName());
					AdminPersistenceManager.getInstance().createQueuedDatabaseOptionalUpdate(getOptionalEntityMappings(), clazz);
				}
				
				logr.config("Completed QueuedDatabaseRequiredUpdate for type " + clazz.getName());
			}catch(Exception e)
			{
				logr.log(Level.SEVERE, "Failure in QueuedDatabaseRequiredUpdate. Exception: ", e);
			}
		}else
			logr.severe("The entityMapping list was empty or null.");
	}

}
