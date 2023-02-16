package com.etc.admin.localData;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Embedded;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.data.LocalDataManager;
import com.etc.admin.data.queue.QueuedDatabaseOptionalUpdate;
import com.etc.admin.data.queue.QueuedDatabaseRequiredUpdate;
import com.etc.api.CorePagination;
import com.etc.corvetto.rqs.CorvettoRequest;
import com.etc.entities.CoreData;
import com.etc.rqs.CoreRequest;

public class AdminPersistenceManager implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3636283007308253925L;
	
	private static AdminPersistenceManager instance = null;
	private static Logger logr = null;

	static 
	{ 
		instance = new AdminPersistenceManager(); 
		logr = Logger.getLogger(instance.getClass().getCanonicalName());
	}

	public static AdminPersistenceManager getInstance() { return instance; }
	
	public AdminPersistenceManager() { super(); }
	
	/**
	 * <p>
	 * get is used to retrieve a singular entity, with its relationships attached. <br>
	 * The required relationships, and any requirements on the subset, will be forced. <br>
	 * All optional relationships will be pulled and attached in a detached state before being <br>
	 * returned. A Queued update will be created to pull all optional relationships, update the  <br>
	 * entities in the local Db, and update the originating entity. </p>
	 * @param <T> The parent Type
	 * @param <X> The dependent Type
	 * @param clazz The Parent Class
	 * @param id the Parent Class Id
	 * @return T the parent Entity
	 * @throws CoreException 
	 */
	@SuppressWarnings("unchecked")
	public <T extends CoreData, X extends CoreData> T get(final Class<T> clazz, final Long id) throws CoreException
	{
		T entity = null;
		T _entity = null;
		X dependent = null;
		List<Triple<Class<X>,Long,String>> optionals = null;
		List<Triple<Class<X>,Long,String>> requireds = null;
		LinkedHashMap<T,Pair<List<Triple<Class<X>,Long,String>>,List<Triple<Class<X>,Long,String>>>> entityMapping = new LinkedHashMap<T,Pair<List<Triple<Class<X>,Long,String>>,List<Triple<Class<X>,Long,String>>>>();
		Method setter = null;
		Method getter = null;
		Object embed = null;

		if(clazz != null && (id != null && id > 0l))
		{
			try(LocalDataManager mgr = new LocalDataManager(EmsApp.getInstance().getEntityManager()))
			{
				if((entity = mgr.get(clazz, id)) != null)
				{
					if(entity.getClass().isAnnotationPresent(javax.persistence.Entity.class) && !mgr.getEntityManager().contains(entity))
					{
						//create list of triples for any optionals that do not exist in db
						optionals = getDependencyList(entity, false);
						requireds = getDependencyList(entity, true);
						
						if(requireds != null && !requireds.isEmpty())
						{
							for(Triple<Class<X>,Long,String> rqdTrp : requireds)
							{
								try { getter = entity.getClass().getMethod(StringUtils.replace(rqdTrp.getRight(), "set", "get")); }
								catch(NoSuchMethodException e2) { getter = null; }
								//get the getter to check for pre-existing dependent (local version)
								if(getter != null)
								{
									//check for the local copy of the dependent and if it is managed. otherwise we will replace it
									if((dependent = (X)getter.invoke(entity)) == null || !mgr.getEntityManager().contains(dependent))
									{
										//get the setter method from this type
										if((setter = entity.getClass().getMethod(rqdTrp.getRight(), rqdTrp.getLeft())) != null)
											//check for this entity type in the entityMap
											//get the entity from the server if necessary if the list did not contain it.
											setter.invoke(entity, get(rqdTrp.getLeft(), rqdTrp.getMiddle()));	
									}
								}else 
								{
									for(Method m : entity.getClass().getMethods())
										if(m.isAnnotationPresent(Embedded.class))
										{
											if((embed = m.invoke(entity)) != null)
											{
												//get the getter to check for pre-existing dependent (local version)
												if((getter = embed.getClass().getMethod(StringUtils.replace(rqdTrp.getRight(), "set", "get"))) != null)
												{
													//check for the local copy of the dependent and if it is managed. otherwise we will replace it
													if((dependent = (X)getter.invoke(embed)) == null || !mgr.getEntityManager().contains(dependent))
													{
														//get the setter method from this type
														if((setter = embed.getClass().getMethod(rqdTrp.getRight(), rqdTrp.getLeft())) != null)
															//check for this entity type in the entityMap
															//get the entity from the server if necessary if the list did not contain it.
															setter.invoke(embed, get(rqdTrp.getLeft(), rqdTrp.getMiddle()));	
													}
													break;
												}
											}
										}
								}
							}
						}
						if(optionals != null && !optionals.isEmpty())
						{
							for(Triple<Class<X>,Long,String> optTrp : optionals)
							{
								try { getter = entity.getClass().getMethod(StringUtils.replace(optTrp.getRight(), "set", "get")); }
								catch(NoSuchMethodException e2) { getter = null; }
								//get the getter to check for pre-existing dependent (local version)
								if(getter != null)
								{
									//check for the local copy of the dependent and if it is managed. otherwise we will replace it
									if((dependent = (X)getter.invoke(entity)) == null || !mgr.getEntityManager().contains(dependent))
									{
										//get the setter method from this type
										if((setter = entity.getClass().getMethod(optTrp.getRight(), optTrp.getLeft())) != null)
											//check for this entity type in the entityMap
											//get the entity from the server if necessary if the list did not contain it.
											setter.invoke(entity, mgr.get(optTrp.getLeft(), optTrp.getMiddle()));		
									}
								}else
								{
									for(Method m : entity.getClass().getMethods())
										if(m.isAnnotationPresent(Embedded.class))
										{
											if((embed = m.invoke(entity)) != null)
											{
												//get the getter to check for pre-existing dependent (local version)
												if((getter = embed.getClass().getMethod(StringUtils.replace(optTrp.getRight(), "set", "get"))) != null)
												{
													//check for the local copy of the dependent and if it is managed. otherwise we will replace it
													if((dependent = (X)getter.invoke(embed)) == null || !mgr.getEntityManager().contains(dependent))
													{
														//get the setter method from this type
														if((setter = embed.getClass().getMethod(optTrp.getRight(), optTrp.getLeft())) != null)
															//check for this entity type in the entityMap
															//get the entity from the server if necessary if the list did not contain it.
															setter.invoke(embed, get(optTrp.getLeft(), optTrp.getMiddle()));	
													}
													break;
												}
											}
										}
								}
							}
						}
						
						_entity = (T)entity.clone();
						logr.config("Creating [SINGLE] QueuedDatabaseRequiredUpdate for type : " + entity.getClass().getName());
						entityMapping.put(entity, Pair.of(requireds, optionals));
						instance.createQueuedDatabaseRequiredUpdate(entityMapping, clazz);
						
						return _entity;
					}else
						return (T)entity.clone();
					
				}else
					throw new CoreException("No entity found by the provided Id.");
						
			}catch(Exception e)
			{
				throw new CoreException("Exception. ", e);
			}finally
			{
				entity = null;
				_entity = null;
				dependent = null;
				optionals = null;
				requireds = null;
				entityMapping = null;
				setter = null;
				getter = null;
				embed = null;
			}
		}else
			throw new CoreException("Invalid Class or Id provided.");
	}

	/**
	 * <p>
	 * get is used to retrieve a singular entity, with its relationships attached. <br>
	 * The required relationships, and any requirements on the subset, will be forced. <br>
	 * All optional relationships will be pulled and attached in a detached state before being <br>
	 * returned. A Queued update will be created to pull all optional relationships, update the  <br>
	 * entities in the local Db, and update the originating entity. </p>
	 * @param <T> The parent Type
	 * @param <X> The dependent Type
	 * @param <U> The CoreRequest Type
	 * @param rqs CoreRequest
	 * @return T the parent Entity
	 * @throws CoreException 
	 */
	@SuppressWarnings("unchecked")
	public <T extends CoreData, X extends CoreData, U extends CoreRequest<T,U>> T get(final CoreRequest<T,U> rqs) throws CoreException
	{
		T entity = null;
		T _entity = null;
		X dependent = null;
		Object embed = null;
		List<Triple<Class<X>,Long,String>> optionals = null;
		List<Triple<Class<X>,Long,String>> requireds = null;
		LinkedHashMap<T,Pair<List<Triple<Class<X>,Long,String>>,List<Triple<Class<X>,Long,String>>>> entityMapping = new LinkedHashMap<T,Pair<List<Triple<Class<X>,Long,String>>,List<Triple<Class<X>,Long,String>>>>();
		Method setter = null;
		Method getter = null;

		if(rqs != null && rqs.getId() != null && rqs.getId().longValue() > 0l)
		{
			try(LocalDataManager mgr = new LocalDataManager(EmsApp.getInstance().getEntityManager()))
			{
				if((entity = mgr.get(rqs)) != null)
				{
					if(entity.getClass().isAnnotationPresent(javax.persistence.Entity.class) && !mgr.getEntityManager().contains(entity))
					{
						//create list of triples for any optionals that do not exist in db
						optionals = getDependencyList(entity, false);
						requireds = getDependencyList(entity, true);
						
						
						if(requireds != null && !requireds.isEmpty())
						{
							for(Triple<Class<X>,Long,String> rqdTrp : requireds)
							{
								try { getter = entity.getClass().getMethod(StringUtils.replace(rqdTrp.getRight(), "set", "get")); }
								catch(NoSuchMethodException e2) { getter = null; }
								//get the getter to check for pre-existing dependent (local version)
								if(getter != null)
								{
									//check for the local copy of the dependent and if it is managed. otherwise we will replace it
									if((dependent = (X)getter.invoke(entity)) == null || !mgr.getEntityManager().contains(dependent))
									{
										//get the setter method from this type
										if((setter = entity.getClass().getMethod(rqdTrp.getRight(), rqdTrp.getLeft())) != null)
											//check for this entity type in the entityMap
											//get the entity from the server if necessary if the list did not contain it.
											setter.invoke(entity, get(rqdTrp.getLeft(), rqdTrp.getMiddle()));	
									}
								}
								//LOOK FOR GETTER ON ANY POSSIBLE EMBEDDABLES
								else 
								{
									for(Method m : entity.getClass().getMethods())
										if(m.isAnnotationPresent(Embedded.class))
										{
											if((embed = m.invoke(entity)) != null)
											{
												//get the getter to check for pre-existing dependent (local version)
												if((getter = embed.getClass().getMethod(StringUtils.replace(rqdTrp.getRight(), "set", "get"))) != null)
												{
													//check for the local copy of the dependent and if it is managed. otherwise we will replace it
													if((dependent = (X)getter.invoke(embed)) == null || !mgr.getEntityManager().contains(dependent))
													{
														//get the setter method from this type
														if((setter = embed.getClass().getMethod(rqdTrp.getRight(), rqdTrp.getLeft())) != null)
															//check for this entity type in the entityMap
															//get the entity from the server if necessary if the list did not contain it.
															setter.invoke(embed, get(rqdTrp.getLeft(), rqdTrp.getMiddle()));	
													}
													break;
												}
											}
										}
								}
							}
						}
						if(optionals != null && !optionals.isEmpty())
						{
							for(Triple<Class<X>,Long,String> optTrp : optionals)
							{
								try { getter = entity.getClass().getMethod(StringUtils.replace(optTrp.getRight(), "set", "get")); }
								catch(NoSuchMethodException e2) { getter = null; }
								//get the getter to check for pre-existing dependent (local version)
								if(getter != null)
								{
									//check for the local copy of the dependent and if it is managed. otherwise we will replace it
									if((dependent = (X)getter.invoke(entity)) == null || !mgr.getEntityManager().contains(dependent))
									{
										//get the setter method from this type
										if((setter = entity.getClass().getMethod(optTrp.getRight(), optTrp.getLeft())) != null)
											//check for this entity type in the entityMap
											//get the entity from the server if necessary if the list did not contain it.
											setter.invoke(entity, mgr.get(optTrp.getLeft(), optTrp.getMiddle()));		
									}
								}
								//LOOK FOR GETTER ON ANY POSSIBLE EMBEDDABLES
								else 
								{
									for(Method m : entity.getClass().getMethods())
										if(m.isAnnotationPresent(Embedded.class))
										{
											if((embed = m.invoke(entity)) != null)
											{
												//get the getter to check for pre-existing dependent (local version)
												if((getter = embed.getClass().getMethod(StringUtils.replace(optTrp.getRight(), "set", "get"))) != null)
												{
													//check for the local copy of the dependent and if it is managed. otherwise we will replace it
													if((dependent = (X)getter.invoke(embed)) == null || !mgr.getEntityManager().contains(dependent))
													{
														//get the setter method from this type
														if((setter = embed.getClass().getMethod(optTrp.getRight(), optTrp.getLeft())) != null)
															//check for this entity type in the entityMap
															//get the entity from the server if necessary if the list did not contain it.
															setter.invoke(embed, get(optTrp.getLeft(), optTrp.getMiddle()));	
													}
													break;
												}
											}
										}
								}
							}
						}
						
						_entity = (T)entity.clone();
						logr.config("Creating [SINGLE] QueuedDatabaseRequiredUpdate for type : " + rqs.getEntityClass().getName());
						entityMapping.put(entity, Pair.of(requireds, optionals));
						instance.createQueuedDatabaseRequiredUpdate(entityMapping, rqs.getEntityClass());
						
						return _entity;
					}else
						return entity;
					
				}else
					throw new CoreException("No entity found by the provided Id.");
						
			}catch(Exception e)
			{
				throw new CoreException("Exception. ", e);
			}finally
			{
				entity = null;
				_entity = null;
				dependent = null;
				optionals = null;
				requireds = null;
				entityMapping = null;
				setter = null;
				getter = null;
				embed = null;
			}
		}else
			throw new CoreException("Invalid Class or Id provided.");
	}

	/**
	 * <p>getAll is responsible for using the provided CoreRequest to first search the local Db<br>
	 * for any corresponding records, and if they are found to have updates on the server and/or<br>
	 * no results were found, fetch the result set from the server. Using this list, maps are built<br>
	 * to contain the required and optional relationship method types/ids/names, as well as a map to hold<br>
	 * all the supporting entities found via the previous map. The required and optional lists of entities are <br>
	 * parsed and set on the parent entities before a queued update is created to persist the changes if any.<br>
	 * the full result set is returned.</p>
	 * @param <T> the Parent Type
	 * @param <U> the Dependent Type
	 * @param <X> the CoreRequest Type
	 * @param rqs CoreRequest<T,X>
	 * @return List<T> entities
	 * @throws CoreException
	 */
	@SuppressWarnings("unchecked")
	public <T extends CoreData, U extends CoreData, X extends CoreRequest<T,X>> List<T> getAll(final CoreRequest<T,X> rqs) throws CoreException
	{
		List<T> cleanList = new ArrayList<T>();
		List<T> entities = null;
		LinkedHashMap<Class<U>,List<Long>> requiredMap = new LinkedHashMap<Class<U>,List<Long>>();
		LinkedHashMap<Class<U>,List<Long>> optionalMap = new LinkedHashMap<Class<U>,List<Long>>();
		
		//LEFT IS REQUIRED, RIGHT IS OPTIONAL
		LinkedHashMap<T,Pair<List<Triple<Class<U>,Long,String>>,List<Triple<Class<U>,Long,String>>>> entityMapping = new LinkedHashMap<T,Pair<List<Triple<Class<U>,Long,String>>,List<Triple<Class<U>,Long,String>>>>();
		LinkedHashMap<Pair<Class<U>,Boolean>,List<U>> entityMap = new LinkedHashMap<Pair<Class<U>,Boolean>,List<U>>();
		
		List<Triple<Class<U>,Long,String>> required = null;
		List<Triple<Class<U>,Long,String>> optionals = null;
		
		CorvettoRequest<U> cvtoRqs = null;
		Method setter = null;
		Method getter = null;
		
		U dependent = null;
		
		Object embed = null;
		
		if(rqs != null)
		{
			try (LocalDataManager mgr = new LocalDataManager(EmsApp.getInstance().getEntityManager()))
			{
				if((entities = mgr.getAll(rqs)) != null && !entities.isEmpty())
				{
					for(T entity : entities)
					{
						
						//only queue updates for unmanaged entities (server version)
						if(entity.getClass().isAnnotationPresent(javax.persistence.Entity.class) && !mgr.getEntityManager().contains(entity))
						{
							required = getDependencyList(entity, true);
							optionals = getDependencyList(entity, false);
							
							//ADD LIST OF TYPE FOR REQUIRED FIELDS
							if(required != null && !required.isEmpty())
								for(Triple<Class<U>,Long,String> trp : required)
									if(!requiredMap.containsKey(trp.getLeft()))
										requiredMap.put(trp.getLeft(), new ArrayList<Long>(Arrays.asList(trp.getMiddle())));
									else
									{
										if(!requiredMap.get(trp.getLeft()).contains(trp.getMiddle()))
											requiredMap.get(trp.getLeft()).add(trp.getMiddle());
									}
							//ADD LIST OF TYPE FOR OPTIONAL FIELDS
							if(optionals != null && !optionals.isEmpty())
								for(Triple<Class<U>,Long,String> trp : optionals)
									if(!optionalMap.containsKey(trp.getLeft()))
										optionalMap.put(trp.getLeft(), new ArrayList<Long>(Arrays.asList(trp.getMiddle())));
									else
									{
										if(!optionalMap.get(trp.getLeft()).contains(trp.getMiddle()))
											optionalMap.get(trp.getLeft()).add(trp.getMiddle());
									}
							//CREATE A MAPPING FOR THIS ENTITY TO REFERENCE AFTER ALL THE LISTS FOR TYPE IDS HAVE BEEN BUILT
							entityMapping.put(entity, Pair.of(required, optionals));
//							entityList.add(Pair.of(entity, Pair.of(required, optionals)));
						}else
						{
//							mgr.getEntityManager().detach(entity);
							//this is a local copy, no need to clone.
							cleanList.add(entity);
						}
					}
					//PULL LISTS OF REQUIREMENTS AND ADD THEM TO HASHMAP
					if(!requiredMap.isEmpty())
						for(Entry<Class<U>,List<Long>> item : requiredMap.entrySet())
						{
							cvtoRqs = new CorvettoRequest<U>(item.getKey(), true);
							cvtoRqs.setIdList(item.getValue());
							List<U> rqdList = getAll(cvtoRqs);
							entityMap.put(Pair.of(item.getKey(), Boolean.TRUE), rqdList);
						}
					//PULL LISTS OF OPTIONALS AND ADD THEM TO HASHMAP
					if(!optionalMap.isEmpty())
						for(Entry<Class<U>,List<Long>> item : optionalMap.entrySet())
						{
							cvtoRqs = new CorvettoRequest<U>(item.getKey(), true);
							cvtoRqs.setIdList(item.getValue());
							//we are calling mgr.getAll here to prevent a recursive database update loop with optional/required relationships
							List<U> optList = mgr.getAll(cvtoRqs);
							entityMap.put(Pair.of(item.getKey(), Boolean.FALSE), optList);
						}
					if(!entityMapping.isEmpty())
					{
						
						//parse map of entities with their required and optional setter maps
						for(Entry<T,Pair<List<Triple<Class<U>,Long,String>>,List<Triple<Class<U>,Long,String>>>> item : entityMapping.entrySet())
						{
//							parent = item.getKey();
							//parse required type map and udpate parent entity
							for(Triple<Class<U>,Long,String> rqdTrp : item.getValue().getLeft())
							{
								try { getter = item.getKey().getClass().getMethod(StringUtils.replace(rqdTrp.getRight(), "set", "get")); }
								catch(NoSuchMethodException e2) { getter = null; }
								//get the getter to check for pre-existing dependent (local version)
								if(getter != null)
								{
									//check for the local copy of the dependent and if it is managed. otherwise we will replace it
									if((dependent = (U)getter.invoke(item.getKey())) == null || !mgr.getEntityManager().contains(dependent))
									{
										//get the setter method from this type
										if((setter = item.getKey().getClass().getMethod(rqdTrp.getRight(), rqdTrp.getLeft())) != null)
											//check for this entity type in the entityMap
											if(entityMap.containsKey(Pair.of(rqdTrp.getLeft(), Boolean.TRUE)))
											{
												for(U entity : entityMap.get(Pair.of(rqdTrp.getLeft(), Boolean.TRUE)))
													//find matching entity record
													if(entity.getId().equals(rqdTrp.getMiddle()))
													{
														//set entity on parent type
														setter.invoke(item.getKey(), entity);
														break;
													}
											}else
												//get the entity from the server if necessary if the list did not contain it.
												setter.invoke(item.getKey(), mgr.get(rqdTrp.getLeft(), rqdTrp.getMiddle()));
									}
								}else
								{
									for(Method m : item.getKey().getClass().getMethods())
										if(m.isAnnotationPresent(Embedded.class))
											if((embed = m.invoke(item.getKey())) != null)
												//get the getter to check for pre-existing dependent (local version)
												if((getter = embed.getClass().getMethod(StringUtils.replace(rqdTrp.getRight(), "set", "get"))) != null)
												{
													//check for the local copy of the dependent and if it is managed. otherwise we will replace it
													if((dependent = (U)getter.invoke(embed)) == null || !mgr.getEntityManager().contains(dependent))
														//get the setter method from this type
														if((setter = embed.getClass().getMethod(rqdTrp.getRight(), rqdTrp.getLeft())) != null)
															//check for this entity type in the entityMap
															if(entityMap.containsKey(Pair.of(rqdTrp.getLeft(), Boolean.TRUE)))
															{
																for(U entity : entityMap.get(Pair.of(rqdTrp.getLeft(), Boolean.TRUE)))
																	//find matching entity record
																	if(entity.getId().equals(rqdTrp.getMiddle()))
																	{
																		//set entity on parent type
																		setter.invoke(embed, entity);
																		break;
																	}
															}else
																//get the entity from the server if necessary if the list did not contain it.
																setter.invoke(embed, mgr.get(rqdTrp.getLeft(), rqdTrp.getMiddle()));
													break;
												}
								}
							}
							//parse optional type map and update parent entity
							for(Triple<Class<U>,Long,String> optTrp : item.getValue().getRight())
							{
								try  { getter = item.getKey().getClass().getMethod(StringUtils.replace(optTrp.getRight(), "set", "get")); }
								catch(NoSuchMethodException e2) { getter = null; }
								//get the getter to check for pre-existing dependent (local version)
								if(getter != null)
								{
									//if the getter has a value that is not managed or no value, set it from our entity list. (used to return to front end)
									if((dependent = (U)getter.invoke(item.getKey())) == null || !mgr.getEntityManager().contains(dependent))
									{
										//get the setter method from this type
										if((setter = item.getKey().getClass().getMethod(optTrp.getRight(), optTrp.getLeft())) != null)
										{
											if(entityMap.containsKey(Pair.of(optTrp.getLeft(), Boolean.FALSE)))
											{
												for(U entity : entityMap.get(Pair.of(optTrp.getLeft(), Boolean.FALSE)))
													//find matching entity record
													if(entity.getId().equals(optTrp.getMiddle()))
													{
														setter.invoke(item.getKey(), entity);
														break;
													}
											}
										}
									}
								}else 
								{
									for(Method m : item.getKey().getClass().getMethods())
										if(m.isAnnotationPresent(Embedded.class))
											if((embed = m.invoke(item.getKey())) != null)
												//get the getter to check for pre-existing dependent (local version)
												if((getter = embed.getClass().getMethod(StringUtils.replace(optTrp.getRight(), "set", "get"))) != null)
												{
													//check for the local copy of the dependent and if it is managed. otherwise we will replace it
													if((dependent = (U)getter.invoke(embed)) == null || !mgr.getEntityManager().contains(dependent))
														//get the setter method from this type
														if((setter = embed.getClass().getMethod(optTrp.getRight(), optTrp.getLeft())) != null)
															//check for this entity type in the entityMap
															if(entityMap.containsKey(Pair.of(optTrp.getLeft(), Boolean.FALSE)))
															{
																for(U entity : entityMap.get(Pair.of(optTrp.getLeft(), Boolean.FALSE)))
																	//find matching entity record
																	if(entity.getId().equals(optTrp.getMiddle()))
																	{
																		//set entity on parent type
																		setter.invoke(embed, entity);
																		break;
																	}
															}else
																//get the entity from the server if necessary if the list did not contain it.
																setter.invoke(embed, mgr.get(optTrp.getLeft(), optTrp.getMiddle()));
													break;
												}
								}
							}
							//CLONE FULLY BUILT ENTITY INTO SEPARATE LIST
							cleanList.add((T)item.getKey().clone());
						}
						logr.config("Creating QueuedDatabaseRequiredUpdate for type : " + rqs.getEntityClass().getName() + " size=[" + entityMapping.size() + "].");
						//add to queue
						instance.createQueuedDatabaseRequiredUpdate(entityMapping, rqs.getEntityClass());
						
//						//RE-SORT BY IDS
//						Collections.sort(cleanList, new Comparator<T>() {
//							@Override
//							public int compare(T o1, T o2) {
//								return Long.compare(o1.getId(), o2.getId());
//							}							
//						});
					}else
						logr.fine("No entities found to update for type " + rqs.getEntityClass().getName());
				}else
					logr.warning("No results returned for type : " + rqs.getEntityClass());
				return cleanList;
			}catch(NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | CloneNotSupportedException e)
			{
				throw new CoreException("Exception. ", e);
			}finally
			{
				entities = null;
				requiredMap = null;
				optionalMap = null;
				required = null;
				optionals = null;
				entityMapping = null;
				entityMap = null;
				cvtoRqs = null;
				setter = null;
				getter = null;
				dependent = null;
				embed = null;
			}
		}else
			throw new CoreException("Invalid CoreRequest.");
	}
	
	public <T extends CoreData, U extends CoreData, X extends CoreRequest<T,X>> List<T> getAllAutoPaginated(final CoreRequest<T,X> rqs) throws CoreException
	{
		List<T> entities = null;
		List<T> es = null;
		try
		{
			entities = new ArrayList<T>();
			rqs.setPaginationEnabled(true);
			
			while(rqs.getPagination().getPageNumber().doubleValue() <= rqs.getPagination().getPageCount())
			{
				//FETCH ENTITIES
				es = getAll(rqs);
				entities.addAll(es);
				//BREAK AFTER LAST PAGE IS FETCHED OR IF PAGINATION IS DISABLED
				if(rqs.getPagination().getPageNumber().doubleValue() == rqs.getPagination().getPageCount() || !rqs.isPaginationEnabled())
					break;
				//INCREMENT TO NEXT PAGE AVAILABLE
				rqs.getPagination().incrementPageNumber();
			}
			 
//			//THE NUMBER OF PAGES
//			rqs.getPagination().getPageCount();
//			//THE CURRENT PAGE REQUESTED
//			rqs.getPagination().getPageNumber();
//			//THE RESULT SIZE PER PAGE
//			rqs.getPagination().getPageSize();
//			//INCREMENTS THE PAGE NUMBER BY 1, OR THE MAX IF ALREADY THERE.
//			rqs.getPagination().incrementPageNumber();
//			//DECREMENTS THE PAGE NUMBER BY 1, OR RETURNS 0 IF ALREADY THERE.
//			rqs.getPagination().decrementPageNumber();
//			//SETS THE PAGE NUMBER TO THE NUMBER SPECIFIED, LIMITED BY THE MAX NUMBER AND 0.
//			rqs.getPagination().setPageNumber(3);
			
			return entities;
			
		}catch(CoreException e)
		{
			throw new CoreException("Exception.", e);
		}finally
		{
			entities = null;
		}
	}
	
	/**
	 * <p>
	 * addOrUpdate will use the provided CoreRequest to update the underlying type via the <br>
	 * server. Once the Updated type is returned, it is either updated or added with its requirements, <br>
	 * followed by a queue update for any new or added optional dependencies. The fully assembled entity<br>
	 * is returned for use.</p>
	 * @param <T> The Parent CoreData type
	 * @param <U> The dependent CoreData type
	 * @param <X> The CoreRequest type
	 * @param rqs CoreRequest
	 * @return T entity
	 * @throws CoreException
	 */
	@SuppressWarnings("unchecked")
	public <T extends CoreData, U extends CoreData, X extends CoreRequest<T,X>> T addOrUpdate(final CoreRequest<T,X> rqs ) throws CoreException
	{
		T entity = null;
		T _entity = null;
		U dependent = null;
		Object embed = null;
		List<Triple<Class<U>,Long,String>> optionals = null;
		List<Triple<Class<U>,Long,String>> requireds = null;
		LinkedHashMap<T,Pair<List<Triple<Class<U>,Long,String>>,List<Triple<Class<U>,Long,String>>>> entityMapping = new LinkedHashMap<T,Pair<List<Triple<Class<U>,Long,String>>,List<Triple<Class<U>,Long,String>>>>();
		Method setter = null;
		Method getter = null;
		
		//validate request
		if(rqs != null && rqs.getEntityClass().isAnnotationPresent(javax.persistence.Entity.class))
		{
			try (LocalDataManager mgr = new LocalDataManager(EmsApp.getInstance().getEntityManager()))
			{
				if(rqs.getId() != null && rqs.getId().longValue() > 0l)
				{
					//update with server
					if((entity = EmsApp.getInstance().getApiManager().update(rqs)) == null)
						throw new CoreException("Unable to update Type " + rqs.getEntityClass().getName());
				}else 
				{
					if(rqs.getEntity() == null)
						throw new CoreException("Unable to add Type " + rqs.getEntityClass().getName() + ". Entity was null.");
					if((entity = EmsApp.getInstance().getApiManager().add(rqs)) == null)
						throw new CoreException("Unable to add Type " + rqs.getEntityClass().getName());
				}
				
				//create list of triples for any optionals that do not exist in db
				optionals = getDependencyList(entity, false);
				requireds = getDependencyList(entity, true);
				
				if(requireds != null && !requireds.isEmpty())
				{
					for(Triple<Class<U>,Long,String> rqdTrp : requireds)
					{
						try { getter = entity.getClass().getMethod(StringUtils.replace(rqdTrp.getRight(), "set", "get")); }
						catch(NoSuchMethodException e2) { getter = null; }
						
						//get the getter to check for pre-existing dependent (local version)
						if(getter != null)
						{
							//check for the local copy of the dependent and if it is managed. otherwise we will replace it
							if((dependent = (U)getter.invoke(entity)) == null || !mgr.getEntityManager().contains(dependent))
							{
								//get the setter method from this type
								if((setter = entity.getClass().getMethod(rqdTrp.getRight(), rqdTrp.getLeft())) != null)
									//check for this entity type in the entityMap
									//get the entity from the server if necessary if the list did not contain it.
									setter.invoke(entity, get(rqdTrp.getLeft(), rqdTrp.getMiddle()));	
							}
						}
						//LOOK FOR GETTER ON ANY POSSIBLE EMBEDDABLES
						else 
						{
							for(Method m : entity.getClass().getMethods())
								if(m.isAnnotationPresent(Embedded.class))
								{
									if((embed = m.invoke(entity)) != null)
									{
										//get the getter to check for pre-existing dependent (local version)
										if((getter = embed.getClass().getMethod(StringUtils.replace(rqdTrp.getRight(), "set", "get"))) != null)
										{
											//check for the local copy of the dependent and if it is managed. otherwise we will replace it
											if((dependent = (U)getter.invoke(embed)) == null || !mgr.getEntityManager().contains(dependent))
											{
												//get the setter method from this type
												if((setter = embed.getClass().getMethod(rqdTrp.getRight(), rqdTrp.getLeft())) != null)
													//check for this entity type in the entityMap
													//get the entity from the server if necessary if the list did not contain it.
													setter.invoke(embed, get(rqdTrp.getLeft(), rqdTrp.getMiddle()));	
											}
											break;
										}
									}
								}
						}
					}
				}
				if(optionals != null && !optionals.isEmpty())
				{
					for(Triple<Class<U>,Long,String> optTrp : optionals)
					{
						try { getter = entity.getClass().getMethod(StringUtils.replace(optTrp.getRight(), "set", "get")); }
						catch(NoSuchMethodException e2) { getter = null; }
						//get the getter to check for pre-existing dependent (local version)
						if(getter != null)
						{
							//check for the local copy of the dependent and if it is managed. otherwise we will replace it
							if((dependent = (U)getter.invoke(entity)) == null || !mgr.getEntityManager().contains(dependent))
							{
								//get the setter method from this type
								if((setter = entity.getClass().getMethod(optTrp.getRight(), optTrp.getLeft())) != null)
									//check for this entity type in the entityMap
									//get the entity from the server if necessary if the list did not contain it.
									setter.invoke(entity, mgr.get(optTrp.getLeft(), optTrp.getMiddle()));		
							}
						}
						//LOOK FOR GETTER ON ANY POSSIBLE EMBEDDABLES
						else 
						{
							for(Method m : entity.getClass().getMethods())
								if(m.isAnnotationPresent(Embedded.class))
								{
									if((embed = m.invoke(entity)) != null)
									{
										//get the getter to check for pre-existing dependent (local version)
										if((getter = embed.getClass().getMethod(StringUtils.replace(optTrp.getRight(), "set", "get"))) != null)
										{
											//check for the local copy of the dependent and if it is managed. otherwise we will replace it
											if((dependent = (U)getter.invoke(embed)) == null || !mgr.getEntityManager().contains(dependent))
											{
												//get the setter method from this type
												if((setter = embed.getClass().getMethod(optTrp.getRight(), optTrp.getLeft())) != null)
													//check for this entity type in the entityMap
													//get the entity from the server if necessary if the list did not contain it.
													setter.invoke(embed, get(optTrp.getLeft(), optTrp.getMiddle()));	
											}
											break;
										}
									}
								}
						}
					}
				}

				_entity = (T)entity.clone();
				logr.config("Creating [SINGLE] QueuedDatabaseRequiredUpdate for type : " + rqs.getEntityClass().getName());
				entityMapping.put(entity, Pair.of(requireds, optionals));
				instance.createQueuedDatabaseRequiredUpdate(entityMapping, rqs.getEntityClass());
				
				return _entity;
					
			}catch(CoreException e2)
			{
				throw e2;
			}catch(Exception e)
			{
				throw new CoreException("Exception", e);
			}finally
			{
				entity = null;
				_entity = null;
				dependent = null;
				optionals = null;
				requireds = null;
				entityMapping = null;
				setter = null;
				getter = null;
				embed = null;
			}
		}else
			throw new CoreException("Invalid CoreRequest for add or update.");
	}
	
	/**
	 * <p>
	 * remove uses the provided CoreRequest to mark the corresponding entity as inactive<br>
	 * on the Server. If a valid response is received, i.e. a 'true' ack, the local version <br>
	 * if it exists is marked as inactive, or the server version is pulled to be inserted locally.</p>
	 * @param <T> The Entity Type
	 * @param <X> The CoreRequest Type
	 * @param rqs CoreRequest
	 * @return boolean removed
	 * @throws CoreException
	 */
	public <T extends CoreData, X extends CoreRequest<T,X>> boolean remove(final CoreRequest<T,X> rqs) throws CoreException
	{
		T entity;
		boolean removed = false;
		
		if(rqs != null && rqs.getId() != null && rqs.getId().longValue() > 0l)
		{
			try (LocalDataManager mgr = new LocalDataManager(EmsApp.getInstance().getEntityManager()))
			{
				if((removed = EmsApp.getInstance().getApiManager().remove(rqs)))
					if((entity = mgr.getLocal(rqs.getEntityClass(), rqs.getId())) == null)
						mgr.get(rqs.getEntityClass(), rqs.getId());
					else
						entity.setActive(false);
				return removed;
			}catch(InterruptedException e)
			{
				throw new CoreException("Exception.", e);
			}
		}else
			throw new CoreException("Invalid Request for removal.");
	}
	
	/**
	 * <p>
	 * getDependencyList will attempt to build a list of dependencies, either<br>
	 * optional or required, based on the optional flag provided.<br>
	 * These relationships will be either @ManyToOne or @OneToOne.<br>
	 * If a local copy of the Parent has a relationship updated, its<br>
	 * reference to the object will be new but the lookup Id will reference<br>
	 * the old. In this case we return the optional type's Id.</p>
	 * 
	 * @param <T> Root entity Type
	 * @param <X> Relationship entity Type
	 * @param entity the root Entity
	 * @param optional if the requirement should be optional or not
	 * @return List of the related entity classes that match the optional flag
	 * @throws CoreException
	 */
	@SuppressWarnings("unchecked")
	public <T extends CoreData, X extends CoreData> List<Triple<Class<X>,Long,String>> getDependencyList(final T entity, boolean required) throws CoreException
	{
		List<Triple<Class<X>,Long,String>> dependencies = null;
		Method[] methods = null;
		Method lookupMethod = null;
		Long lookupId = null;
		X getterObj = null;
		Object embed = null;

		try
		{
			if(entity == null)
				throw new CoreException("Invalid entity.");
			dependencies = new ArrayList<Triple<Class<X>,Long,String>>();
			if((methods = entity.getClass().getMethods()) == null)
				throw new CoreException("Unable to find methods on this class type: " + entity.getClass().getName());
			for(Method m : methods)
			{
				//look for many to one relationships (often the only type that has optional=false)
				if(m.isAnnotationPresent(ManyToOne.class) || m.isAnnotationPresent(OneToOne.class))
				{
					//STORE IF THIS ANNOTATION'S RELATIONSHIP IS OPTIONAL
					if((m.isAnnotationPresent(ManyToOne.class) && ((ManyToOne)m.getAnnotation(ManyToOne.class)).optional() != required)  || 
							(m.isAnnotationPresent(OneToOne.class) && ((OneToOne)m.getAnnotation(OneToOne.class)).optional() != required))
						//check that the return type is of CoreData, cast if it is.
						if(CoreData.class.isAssignableFrom(m.getReturnType()))
						{
							//if this is an optional self reference, skip it.
							if(m.getReturnType().equals(entity.getClass()) && !required)
								continue;
							//get the return value if it exists
							getterObj = (X)m.invoke(entity);
							//check for the lookupId value, add new triple if it exists
							try { lookupMethod = entity.getClass().getMethod(m.getName().concat("Id")); }
							catch(NoSuchMethodException e2) { lookupMethod = null; logr.log(Level.WARNING, "Could not locate lookup method. Type " + m.getReturnType().getName() + " will not be added. Exception: " + e2.getMessage());}
							if(lookupMethod != null)
								if(Long.class.isAssignableFrom(lookupMethod.getReturnType()) && ((lookupId = (Long)lookupMethod.invoke(entity)) != null && lookupId > 0l))
									//if the getter was null or the id's match
									if(getterObj == null || getterObj.getId() == lookupId)
										dependencies.add(Triple.of((Class<X>)m.getReturnType(), lookupId, StringUtils.replace(m.getName(),"get","set")));
									//if the relationship was updated locally
									else
										dependencies.add(Triple.of((Class<X>)m.getReturnType(), getterObj.getId(), StringUtils.replace(m.getName(),"get","set")));
						}
				}
				//look for embeddable object and its required/optional fields
				else if(m.isAnnotationPresent(Embedded.class))
				{
					//check for a valid embeddable
					if((embed = m.invoke(entity)) != null)
					{
						for(Method em : embed.getClass().getMethods())
						{
							//STORE IF THIS ANNOTATION'S RELATIONSHIP IS OPTIONAL
							if((em.isAnnotationPresent(ManyToOne.class) && ((ManyToOne)em.getAnnotation(ManyToOne.class)).optional() != required)  || 
									(em.isAnnotationPresent(OneToOne.class) && ((OneToOne)em.getAnnotation(OneToOne.class)).optional() != required))
								//check that the return type is of CoreData, cast if it is.
								if(CoreData.class.isAssignableFrom(em.getReturnType()))
								{
									//if this is an optional self reference, skip it.
									if(em.getReturnType().equals(embed.getClass()) && !required)
										continue;
									//get the return value if it exists
									getterObj = (X)em.invoke(embed);
									//check for the lookupId value, add new triple if it exists
									try { lookupMethod = embed.getClass().getMethod(em.getName().concat("Id")); }
									catch(NoSuchMethodException e2) { lookupMethod = null; logr.log(Level.WARNING, "Could not locate lookup method. Type " + em.getReturnType().getName() + " will not be added. Exception: " + e2.getMessage());}
									if(lookupMethod != null)
										if(Long.class.isAssignableFrom(lookupMethod.getReturnType()) && ((lookupId = (Long)lookupMethod.invoke(embed)) != null && lookupId > 0l))
											//if the getter was null or the id's match
											if(getterObj == null || getterObj.getId() == lookupId)
												dependencies.add(Triple.of((Class<X>)em.getReturnType(), lookupId, StringUtils.replace(em.getName(),"get","set")));
											//if the relationship was updated locally
											else
												dependencies.add(Triple.of((Class<X>)em.getReturnType(), getterObj.getId(), StringUtils.replace(em.getName(),"get","set")));
								}
						}
					}
				}
			}
			return dependencies;
		}catch(Exception e)
		{
			throw new CoreException("Exception. ", e);
		}finally
		{
			methods = null;
			dependencies = null;
			lookupMethod = null;
			lookupId = null;
			embed = null;
		}
	}

//	/**
//	 * <p>
//	 * createQueuedDependencyDatabaseUpdate is used to finish building a singular instance.<br>
//	 * The parent entity class type, its id, and a List of triples containing its optional<br>
//	 * relationship mappings are provided. A new QueuedDependencyDatabaseUpdate is created<br>
//	 * and added to the queue immediately or once a spot opens up.</p>
//	 * @param <T> the Parent Type
//	 * @param <X> the Dependent Type
//	 * @param clazz the parent Class
//	 * @param id the Parent id
//	 * @param optionals List
//	 * @throws CoreException
//	 */
//	public <T extends CoreData, X extends CoreData> void createQueuedDependencyDatabaseUpdate(Class<T> clazz, Long id, List<Triple<Class<X>,Long,String>> optionals) throws CoreException
//	{
//		if(clazz != null && id != null && id > 0l && optionals != null && !optionals.isEmpty())
//			try {
//				EmsApp.getInstance().getQueue().put(new QueuedDependencyDatabaseUpdate<T>(clazz, id, optionals));
//			} catch (InterruptedException e) { throw new CoreException("Exception. ", e); }
//		else
//			throw new CoreException("Invalid Map.Entry. Unable to create QueuedDependencyDatabaseUpdate.");
//	}
	
	/**
	 * <p>createQueuedDatabaseRequiredUpdate is used to persist a list of a type of entity<br>
	 * provided in a hashmap. The hashmap will also contain the required and optional mappings<br>
	 * for the entity in the key of the map. The class type is provided as a convenience and for <br>
	 * helping with typing the following optionalUpdate if one is required.</p>
	 * @param <T> the parent Type
	 * @param <X> the Dependent Type
	 * @param entityMappings HashMap
	 * @param clazz parent Class
	 * @throws CoreException
	 */
	public <T extends CoreData, X extends CoreData> void createQueuedDatabaseRequiredUpdate(HashMap<T,Pair<List<Triple<Class<X>,Long,String>>,List<Triple<Class<X>,Long,String>>>> entityMappings, Class<T> clazz) throws CoreException
	{
		if(entityMappings == null || entityMappings.isEmpty())
			throw new CoreException("Invalid Mapping Set.");
		try {
			EmsApp.getInstance().getQueue().put(new QueuedDatabaseRequiredUpdate<T>(entityMappings, clazz));
		} catch (InterruptedException e) { throw new CoreException("Exception. ", e); }
	}
	
	/**
	 * <p>createQueuedDatabaseOptionalUpdate is used to build the optional relationships for a list of<br>
	 * a type of entity. A Map containing a Pair of the parent type class along with the parent id as the key<br>
	 * and a List of triples containing the optional mappings which *should be now persisted in the local db<br>
	 * are provided as the value. The Parent Class type is provided as a convenience separately.</p>
	 * @param <T> the Parent Type
	 * @param <X> the Dependent Type
	 * @param entityMappings HashMap
	 * @param clazz parent Class
	 * @throws CoreException
	 */
	public <T extends CoreData, X extends CoreData> void createQueuedDatabaseOptionalUpdate(HashMap<Pair<Class<T>,Long>,List<Triple<Class<X>,Long,String>>> entityMappings, Class<T> clazz) throws CoreException
	{
		if(entityMappings == null || entityMappings.isEmpty())
			throw new CoreException("Invalid Mapping set for QueuedDatabaseOptionalUpdate.");
		try {
			EmsApp.getInstance().getQueue().put(new QueuedDatabaseOptionalUpdate<T>(entityMappings, clazz));
		} catch (InterruptedException e) { throw new CoreException("Exception. ", e); }
	}
}