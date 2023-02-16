package com.etc.admin.data;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.commons.lang3.StringUtils;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.corvetto.ServerPagination;
import com.etc.corvetto.rqs.CorvettoRequest;
import com.etc.entities.CoreData;
import com.etc.rqs.CoreRequest;
import com.etc.utils.hibernate.PersistenceTransaction;
import com.sun.xml.internal.stream.Entity;

/**
 * <p>
 * LocalDataManager is responsible for the Following: <br><br>
 * <ul>
 * <li>Checking if the local version of an entity is out of Date</li>
 * <ul><li>Pulling any updates found</li></ul>
 * <li>Checking for any updates for a type exist since its last fetch</li>
 * <li>Returning the most up to date version of an entity or list</li>
 * </ul></p>
 * 
 * @author Stephen Carter 
 * @since 08/17/21
 *	
 *	
 */
public class LocalDataManager extends PersistenceTransaction implements Serializable 
{
	private static final long serialVersionUID = -8263309068594794170L;
	
	Logger logr = null;

	public LocalDataManager(EntityManager em) throws CoreException 
	{
		super(em);
		logr = Logger.getLogger(this.getClass().getCanonicalName());
	}
	
	public LocalDataManager(EntityManager em, boolean newTransaction) throws CoreException
	{
		super(em, newTransaction);
		logr = Logger.getLogger(this.getClass().getCanonicalName());
	}
	
	
	/**
	 * <p>
	 * getServerLastUpdated attempts to get the lastUpdated for the entity <br>
	 * specified by the class type and id provided.</p>
	 * @param <T> the entity Type
	 * @param clazz the entity Class
	 * @param id the entity Id
	 * @return Calendar lastUpdated 
	 * @throws CoreException
	 */
	private <T extends CoreData> Calendar getServerLastUpdated(Class<T> clazz, Long id) throws CoreException
	{
		CorvettoRequest<T> rqs;

		try
		{
			if(clazz != null && (id != null && id.longValue() > 0l))
			{
				rqs = new CorvettoRequest<T>(clazz);
				rqs.setId(id);
				return EmsApp.getInstance().getApiManager().getLastUpdated(rqs);
			}else
				throw new CoreException("Invalid Parameters.");
		}catch(InterruptedException e)
		{
			throw new CoreException("Exception.", e);
		}finally
		{
			rqs = null;
		}
	}
	
	
	/**
	 * <p>
	 * getFromServer uses the provided class and id to find the <br>
	 * corresponding entity on the server. </p>
	 * @param <T> the entity Type
	 * @param clazz the entity Class
	 * @param id the entity Id
	 * @return Entity
	 * @throws CoreException
	 */
	private <T extends CoreData> T getFromServer(Class<T> clazz, Long id) throws CoreException
	{
		CorvettoRequest<T> rqs;
		
		try
		{
			if(clazz != null && (id != null && id.longValue() > 0l))
			{
				rqs = new CorvettoRequest<T>(clazz);
				rqs.setId(id);
				return EmsApp.getInstance().getApiManager().get(rqs);
			}else
				throw new CoreException("Either the Class<T> or the Id was invalid.");
		}catch(InterruptedException e)
		{
			throw new CoreException("Exception.", e);
		}
	}
	
	/**
	 * <p>getAll takes the provided CoreRequest and fetches all of the <br>
	 * entity(s) requested. The list is then merged into the local Db. </p>
	 * @param <T> CoreData entity Type
	 * @param <X> CoreRequest rqs Type
	 * @param request CoreRequest 
	 * @return List<T CoreData> list
	 * @throws CoreException
	 */
	public <T extends CoreData, X extends CoreRequest<T,X>> List<T> getAllFromServer(final CoreRequest<T,X> request) throws CoreException
	{ 
		try 
		{ 
			return EmsApp.getInstance().getApiManager().getAll(request); 
		}catch (Exception e) { 
			throw new CoreException("Exception. ", e);
		}
	} 
	
	
	/**
	 * <p>
	 * getAllLocal retrieves all of the local database copies of the provided<br>
	 * entity Class<T>.</p>
	 * @param <T>
	 * @param clazz
	 * @return List<T> entities
	 * @throws CoreException
	 */
	public <T extends CoreData> List<T> getAllLocal(final Class<T> clazz) throws CoreException
	{
		CriteriaBuilder bldr = null;
		CriteriaQuery<T> qry = null;

		try
		{
			if((bldr = getEntityManager().getCriteriaBuilder()) != null)
			{
				(qry = bldr.createQuery(clazz)).from(clazz);
				return getEntityManager().createQuery(qry).getResultList();
			}else
				throw new CoreException("Unable to create CriteriaBuilder. Invalid EntityManager state.");
		}catch(Exception e)
		{
			throw new CoreException("Exception. ", e);
		}finally
		{
			bldr = null;
			qry = null;
		}
	}
	
	/**
	 * <p>
	 * getAll attempts to pull all locally stored copies of a Type, <br>
	 * and all updated versions from the server. If none exist locally, <br>
	 * all copies are pulled from the server. If any of the local entities are <br>
	 * only partially loaded, i.e. missing optional relationships, then they are <br>
	 * replaced by the server version.</p>
	 * @param <T> Entity Type
	 * @param clazz entity class
	 * @return List<T> entities
	 * @throws CoreException
	 */
	public <T extends CoreData, X extends CoreRequest<T,X>> List<T> getAll(final CoreRequest<T,X> rq) throws CoreException
	{
		CriteriaQuery<T> qry = null;
		CriteriaQuery<Long> countQry = null;
		List<T> entityList = null;
		List<T> serverList = null;
		List<T> modifyList = null;
		ServerPagination pagination = null;

		try
		{
			//GET LOCAL COPIES
			//CREATE AND EXECUTE QUERY
			if((qry = rq.getEntityQuery(getEntityManager().getCriteriaBuilder(), DataManager.i().mLocalUser)) != null && (countQry = rq.getEntityQueryCount(getEntityManager().getCriteriaBuilder(), DataManager.i().mLocalUser)) != null)
			{
				//GET PAGINATION
				if((pagination = (ServerPagination)rq.getPagination()) != null)
				{
					//SET RESULT SIZE
					pagination.setResultCount(getEntityManager().createQuery(countQry).getSingleResult().intValue());
					//PROCESS RESULTS, IF ANY
					if((entityList = getEntityManager().createQuery(qry).setFirstResult(pagination.getStartIndex()).setMaxResults(pagination.getPageSize()).getResultList()) != null && !entityList.isEmpty())
					{
						logr.config("mgr.getAll: entityList for type " + rq.getEntityClass().getName() + " found to exist. size: " + entityList.size());

						//PULL ALL UPDATED COPIES 
						if((serverList = getAllFromServer(rq)) != null && !serverList.isEmpty())
						{
							modifyList = new ArrayList<T>();
							//REMOVE STALE COPIES/UPDATE NEW COPIES
							for(T s : serverList)
								if(entityList.contains(s))
								{
									for(T e : entityList)
										if(s.getId().equals(e.getId()))
										{
											//use the server version if it is up to date, or if the originating entity is only partially loaded.
											if(s.getLastUpdated().after(e.getLastUpdated()) || e.isPartialLoad())
												modifyList.add(s);
											break;
										}
								}else
									modifyList.add(s);
											
							entityList.removeAll(modifyList);
							entityList.addAll(modifyList);
						}
						
					}else
					{
						logr.warning("NO RESULTS FROM LOCAL.");
						entityList = getAllFromServer(rq);
					}
				}else
					throw new CoreException("Invalid Pagination.");
				
			}else
				throw new CoreException("Unable to build Query.");
			
			return entityList;
		}catch(Exception e)
		{
			throw new CoreException("Exception. ", e);
		}finally
		{
			qry = null;
			serverList = null;
			entityList = null;
			modifyList = null;
		}
	}
	
	/**
	 * <p>
	 * findById uses the provided classType and id to fetch the local copy of the<br>
	 * provided entity type if it exists, update it with the server version if that <br>
	 * version is found to be more up to date, or fetch the server version if no local<br>
	 * copy is found.</p>
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @return
	 * @throws CoreException
	 */
	public <T extends CoreData> T get(final Class<T> clazz, final long id) throws CoreException
	{
		T entity = null;
		CorvettoRequest<T> rqs = null;

		try
		{
			rqs = new CorvettoRequest<T>(clazz);
			rqs.setId(id);

			if(id > 0l && clazz != null)
			{
				if(clazz.isAnnotationPresent(javax.persistence.Entity.class))
				{
					//LOOK FOR LOCAL VERSION
					if((entity = getEntityManager().find(clazz, id)) == null)
					{
						logr.finest("Local copy not found, searching on server.");
						if((entity = EmsApp.getInstance().getApiManager().get(rqs)) == null)
							throw new CoreException("No entity found by the provided id.");
					}else
					{
						logr.finest("Found " + clazz.getName() + " locally, checking for more up-to-date version on server.");
						//CHECK IF SERVER VERSION IS UPDATED OR IF THE SPECIFIED ENTITY IS ONLY PARTIALLY LOADED
						if(getServerLastUpdated(clazz, id).after(entity.getLastUpdated()) || entity.isPartialLoad())
						{
							logr.finest("Found Server has more up-to-date version for " + clazz.getName());
							entity = getFromServer(clazz, id);
						}else
							logr.finer("ServerEntity for " + clazz.getSimpleName() + " was not found to have updates.");
					}
				}else
				{
					rqs = new CorvettoRequest<T>(clazz);
					rqs.setId(id);
					entity = EmsApp.getInstance().getApiManager().get(rqs);
				}
				
				return entity;
			}else 
				throw new CoreException("Invalid Parameters.");
		}catch(Exception e)
		{
			throw new CoreException("Exception. ", e);
		}finally
		{
			entity = null;
			rqs = null;
		}
	}
	
	/**
	 * <p>
	 * get uses the provided CoreRequest to search locally and the server for <br>
	 * the specified entity.</p>
	 * @param <T> the Entity Type
	 * @param <X> the Request Type
	 * @param rqs CoreRequest
	 * @return T entity
	 * @throws CoreException
	 */
	public <T extends CoreData, X extends CoreRequest<T,X>> T get(final CoreRequest<T,X> rqs) throws CoreException
	{
		T entity = null;
		
		try
		{
			if(rqs != null && rqs.getId() != null && rqs.getId().longValue() > 0l)
			{
				//CHECK FOR A DATA OBJECT OR ENTITY
				if(rqs.getEntityClass().isAnnotationPresent(javax.persistence.Entity.class))
				{
					if((entity = getEntityManager().find(rqs.getEntityClass(), rqs.getId())) == null)
					{
						if((entity = EmsApp.getInstance().getApiManager().get(rqs)) == null)
							throw new CoreException("No entity found by the provided id.");
					}else
					{
						if(getServerLastUpdated(rqs.getEntityClass(), rqs.getId()).after(entity.getLastUpdated()) || entity.isPartialLoad())
						{
							entity = EmsApp.getInstance().getApiManager().get(rqs);
						}
					}
				}else
					entity = EmsApp.getInstance().getApiManager().get(rqs);
				
				return entity;
			}else
				throw new CoreException("Invalid Request.");
		}catch(Exception e)
		{
			throw new CoreException("Exception.", e);
		}
	}
	
	/**
	 * <p>
	 * getLocal will return the entity by the provided class and id <br>
	 * if it exists, null otherwise.</p>
	 * @param <T> The entity Type
	 * @param clazz the entity Class
	 * @param id the entity Id
	 * @return entity
	 * @throws CoreException
	 */
	public <T extends CoreData> T getLocal(final Class<T> clazz, final long id) throws CoreException
	{
		try
		{
			if(clazz != null && id > 0l)
				return getEntityManager().find(clazz, id);
			else
				throw new CoreException("Invalid paramters.");
		}catch(Exception e)
		{
			throw new CoreException("Exception.", e);
		}
	}
	
	/**
	 * <p>
	 * getDependencyList will attempt to build a list of dependencies, either<br>
	 * optional or required, based on the optional flag provided.<br>
	 * These relationships will be either @ManyToOne or @OneToOne.</p>
	 * 
	 * @param <T> Root entity Type
	 * @param <X> Relationship entity Type
	 * @param entity the root Entity
	 * @param optional if the requirement should be optional or not
	 * @return List of the related entity classes that match the optional flag
	 * @throws CoreException
	 */
//	@SuppressWarnings("unchecked")
//	public <T extends CoreData, X extends CoreData> List<Class<X>> getDependencyList(final Class<T> clazz, boolean optional) throws CoreException
//	{
//		List<Class<X>> classList = null;
//		Method[] methods = null;
//		ApiRequest apiRqs = null;
//		try
//		{
//			if(clazz == null)
//				throw new CoreException("Invalid entity.");
//			if(clazz.isAnnotationPresent(ApiRequest.class))
//			{
//				apiRqs = clazz.getAnnotation(ApiRequest.class);
//				if(apiRqs.apiCacheable() && (apiRqs.cacheLevel().equals("FULL") || (apiRqs.cacheLevel().equals("REQUIRED") && !optional)))
//				{
//					classList = new ArrayList<Class<X>>();
//					if((methods = clazz.getDeclaredMethods()) == null)
//						throw new CoreException("Unable to find methods on this class type: " + clazz.getName());
//					for(Method m : methods)
//						//look for many to one relationships (often the only type that has optional=false)
//						if(m.isAnnotationPresent(ManyToOne.class) || m.isAnnotationPresent(OneToOne.class))
//						{
//							//STORE IF THIS ANNOTATION'S RELATIONSHIP IS OPTIONAL
//							if((m.isAnnotationPresent(ManyToOne.class) && ((ManyToOne)m.getAnnotation(ManyToOne.class)).optional() == optional)  || 
//									(m.isAnnotationPresent(OneToOne.class) && ((OneToOne)m.getAnnotation(OneToOne.class)).optional() == optional))
//							{
//								//check that the return type is of CoreData, cast if it is.
//								if(CoreData.class.isAssignableFrom(m.getReturnType()))
//									classList.add((Class<X>)m.getReturnType());
//							}
//						}
//				}
//			}
//			return classList;
//		}catch(Exception e)
//		{
//			throw new CoreException("Exception. ", e);
//		}finally
//		{
//			methods = null;
//			classList = null;
//		}
//	}
	
	/**
	 * <p>
	 * buildRequiredRelationships parses the annotations on the CoreData relationships<br>
	 * for the provided entity and attempts to either find the local version first <br>
	 * for all optional and required relationships, and then if none is found, find the<br>
	 * server version for any required relationships. The requiredOnly flag is provided<br>
	 * to only look and fetch the required relationships or the optional relationships.</p>
	 * @param <T> The Root entity type
	 * @param <X> The related entity type
	 * @param entity The root entity
	 * @param requiredOnly if the relationship should be required (optional=false)
	 * @throws CoreException 
	 */
	@SuppressWarnings("unchecked")
	private <T extends CoreData, X extends CoreData> void buildRequiredRelationships(final T entity) throws CoreException
	{
		Method[] methods = null;
		Class<X> requiredType = null;
		Long requiredId = null;
		Method lookupMethod = null;
		Method setterMethod = null;
		Class<X>[] setterArgTypes = null;
		X managedEntity = null;
//		X getterObj = null;
		boolean optional = true;

		try
		{
			if(entity != null)
			{
				if((methods = entity.getClass().getDeclaredMethods()) == null)
					throw new CoreException("Unable to find methods on this class type: " + entity.getClass());
				for(Method m : methods)
					//look for many to one relationships (often the only type that has optional=false)
					if(m.isAnnotationPresent(ManyToOne.class) || m.isAnnotationPresent(OneToOne.class))
					{
						//STORE IF THIS ANNOTATION'S RELATIONSHIP IS OPTIONAL
						if(m.isAnnotationPresent(ManyToOne.class))
							optional = ((ManyToOne)m.getAnnotation(ManyToOne.class)).optional();
						else if(m.isAnnotationPresent(OneToOne.class))
							optional = ((OneToOne)m.getAnnotation(OneToOne.class)).optional();
						
						//TODO: FIX THIS TO MATCH ADMINPERSISTENCE GETDEPENDENCIES LOGIC -- WHAT IF THE TYPE ID IS JUST UPDATED??
						if(m.invoke(entity) == null)
						{
							//check that the return type is of CoreData, cast if it is.
							if(CoreData.class.isAssignableFrom(m.getReturnType()))
							{
								requiredType = (Class<X>)m.getReturnType();
								//check for the lookup id method and type
								try { lookupMethod = entity.getClass().getMethod(m.getName().concat("Id")); }
								catch(NoSuchMethodException e) { lookupMethod = null; }
								
								if(lookupMethod != null)
								{
									//get the lookup id value
									if(((requiredId = (Long)lookupMethod.invoke(entity)) != null) && !optional)
									{
										logr.finest(entity.getClass().getName().concat("adding required entity. optional: " + optional));
										//prepare the setter for the required entity
										setterArgTypes = new Class[1];
										setterArgTypes[0] = requiredType;
										setterMethod = entity.getClass().getMethod(StringUtils.replace(m.getName(),"get","set"), setterArgTypes);
										
										if((managedEntity = getLocal(requiredType, requiredId)) == null)
										{
											if((managedEntity = getFromServer(requiredType, requiredId)) != null)
											{
												//BEGIN REQUIRED UPDATE TREE. REQUIRED ENTITIES MUST BE ADDED BEFORE THE 
												//ROOT ENTITY CAN BE ADDED/UPDATED.
												if((managedEntity = updateEntity(requiredType.cast(managedEntity))) != null)
													setterMethod.invoke(entity, managedEntity);
												else
													throw new CoreException("Unable to add required Type " + entity.getClass().getName().concat(".").concat(requiredType.getClass().getName()) + " to persistence Context.");
											}else
												throw new CoreException("Unable to locate type by provided id. type=[" + entity.getClass().getName() + "].");
										}else
											setterMethod.invoke(entity, managedEntity);
									}else
										logr.finer("Lookup id was either null or optional.");
								}else
									logr.finer(entity.getClass().getName().concat(".").concat(m.getName()).concat(" does not have a corresponding lookup method."));
							}else
								throw new CoreException("Unable to determine CoreData return Type for required relationship for type " + entity.getClass().getName().concat(".").concat(m.getName()));
						}//TODO: IN THE FUTURE, POSSIBLY CHECK FOR AN UPDATED SERVER VERSION IF WE HAVE A LOCAL COPY??
						else
							logr.finer("Method " + entity.getClass().getName().concat(".").concat(m.getName()).concat(" Has a value. No need to fetch entity."));
					}
			}else
				throw new CoreException("Invalid entity.");
		}catch(Exception e)
		{
			throw new CoreException("Exception. ", e);
		}finally
		{
			methods = null;
			requiredType = null;
			requiredId = null;
			lookupMethod = null;
			setterMethod = null;
			setterArgTypes = null;
		}
	}

	/**
	 * <p>
	 * updateRequiredRelationships parses the annotations on the CoreData relationships<br>
	 * for the provided entity and attempts to either find the local version first <br>
	 * for all optional and required relationships, and then if none is found, find the<br>
	 * server version for any required relationships. The new entity provides the id's if they exist.<br>
	 *  The requiredOnly flag is provided to only look and fetch the required relationships or the optional relationships.</p>
	 * @param <T> The Root entity type
	 * @param <X> The related entity type
	 * @param managedEntity The root entity from the local db
	 * @param newEntity the root entity who's relationships will be persisted to the managed entity
	 * @param requiredOnly if the relationship should be required (optional=false)
	 * @throws CoreException 
	 */
	@SuppressWarnings("unchecked")
	private <T extends CoreData, X extends CoreData> void updateRequiredRelationships(T managedEntity, T newEntity) throws CoreException
	{
		Method[] methods = null;
		Class<X> requiredType = null;
		Long requiredId = null;
		Method lookupMethod = null;
		Method setterMethod = null;
		X dependent = null;
		boolean optional = true;

		try
		{
			if(newEntity != null)
			{
				if((methods = newEntity.getClass().getDeclaredMethods()) == null)
					throw new CoreException("Unable to find methods on this class type: " + newEntity.getClass());
				for(Method m : methods)
					//look for many to one relationships (often the only type that has optional=false)
					if(m.isAnnotationPresent(ManyToOne.class) || m.isAnnotationPresent(OneToOne.class))
					{
						//STORE IF THIS ANNOTATION'S RELATIONSHIP IS OPTIONAL
						if(m.isAnnotationPresent(ManyToOne.class))
							optional = ((ManyToOne)m.getAnnotation(ManyToOne.class)).optional();
						else if(m.isAnnotationPresent(OneToOne.class))
							optional = ((OneToOne)m.getAnnotation(OneToOne.class)).optional();
						if(m.invoke(newEntity) == null)
						{
							//check that the return type is of CoreData, cast if it is.
							if(CoreData.class.isAssignableFrom(m.getReturnType()))
							{
								requiredType = (Class<X>)m.getReturnType();
								//check for the lookup id method and type from the new entity (updating relationships)
								try { lookupMethod = newEntity.getClass().getMethod(m.getName().concat("Id")); }
								catch(NoSuchMethodException e) { lookupMethod = null; }
								
								if(lookupMethod != null)
								{
									//get the lookup id value
									if(((requiredId = (Long)lookupMethod.invoke(newEntity)) != null) && !optional)
									{
										logr.finest(newEntity.getClass().getName().concat("adding required entity. optional: " + optional));
										//prepare the setter for the required entity
										setterMethod = newEntity.getClass().getMethod(StringUtils.replace(m.getName(),"get","set"), requiredType);
										
										if((dependent = getLocal(requiredType, requiredId)) == null)
										{
											if((dependent = getFromServer(requiredType, requiredId)) != null)
											{
												//BEGIN REQUIRED UPDATE TREE. REQUIRED ENTITIES MUST BE ADDED BEFORE THE 
												//ROOT ENTITY CAN BE ADDED/UPDATED.
												if((dependent = updateEntity(requiredType.cast(dependent))) != null)
													setterMethod.invoke(newEntity, dependent);
												else
													throw new CoreException("Unable to add required Type " + newEntity.getClass().getName().concat(".").concat(requiredType.getClass().getName()) + " to persistence Context.");
											}else
												throw new CoreException("Unable to locate type by provided id. type=[" + newEntity.getClass().getName() + "].");
										}else
											setterMethod.invoke(newEntity, dependent);
									}else
										logr.finer("Lookup id was either null or optional.");
								}else
									logr.finer(newEntity.getClass().getName().concat(".").concat(m.getName()).concat(" does not have a corresponding lookup method."));
							}else
								throw new CoreException("Unable to determine CoreData return Type for required relationship for type " + newEntity.getClass().getName().concat(".").concat(m.getName()));
						}//TODO: IN THE FUTURE, POSSIBLY CHECK FOR AN UPDATED SERVER VERSION IF WE HAVE A LOCAL COPY??
						else
							logr.finer("Method " + newEntity.getClass().getName().concat(".").concat(m.getName()).concat(" Has a value. No need to fetch entity."));
					//attaching managed entity's persistent bags
					}else if(m.isAnnotationPresent(OneToMany.class))
					{
						if(managedEntity.getClass().getMethod(m.getName()).invoke(managedEntity) != null)
						{
							if((setterMethod = newEntity.getClass().getMethod(StringUtils.replace(m.getName(), "get", "set"), List.class)) != null)
							{
								setterMethod.invoke(newEntity, (List<X>)managedEntity.getClass().getMethod(m.getName()).invoke(managedEntity));
							}else
								throw new CoreException("Unable to find setter method for Collection of type " + m.getReturnType().getName());
						}
					}
			}else
				throw new CoreException("Invalid entity.");
		}catch(Exception e)
		{
			throw new CoreException("Exception. ", e);
		}finally
		{
			methods = null;
			requiredType = null;
			requiredId = null;
			lookupMethod = null;
			setterMethod = null;
		}
	}
	
	/**
	 * <p>
	 * updateEntity takes the provided entity, validates for an <br>
	 * existing id, before sending the entity to be assembled with the <br>
	 * required relationships via local and/or server entities and finally being<br>
	 * placed in the persistence context.</p>
	 * @param <T> CoreData subtype
	 * @param entity Entity to be updated
	 * @return The managed CoreData entity
	 * @throws CoreException
	 */
	@SuppressWarnings("unchecked")
	public <T extends CoreData> T updateEntity(T entity) throws CoreException
	{
		validateEntity(entity);
		
		T managedEntity = null;

		try
		{
			if(entity != null)
			{
				//check for managed entity
				if(!getEntityManager().contains(entity))
				{
					logr.finest("Checking lastUpdated for " + entity.getClass().getSimpleName() + " and id: " + entity.getId());
					//check for most current version.
					if((managedEntity = getEntityManager().find((Class<T>)entity.getClass(), entity.getId())) == null)
					{
						//this entity does not exist locally yet. build requirements and merge.
						buildRequiredRelationships(entity);
						managedEntity = getEntityManager().merge(entity);
					}else
					{
						//this entity was found to exist locally. update required relationships from provided entity and merge.
						//this will also help protect from nulling out the persistent bags around our collections
						updateRequiredRelationships(managedEntity, entity);
						managedEntity = getEntityManager().merge(entity);
					}
				}else
					//the provided entity was managed. do nothing.
					managedEntity = entity;
			}else
				throw new CoreException("Invalid entity.");
			return managedEntity;
		}catch(Exception e)
		{
			throw new CoreException("Exception. ", e);
		}finally
		{
			managedEntity = null;
		}
	}
	
	public void validateEntity(CoreData data) throws CoreException
	{
		if(data != null)
		{
			if(data.getId() == null)
				throw new CoreException("Invalid Id for local persist/update.");
			if(data.getLastUpdated() == null)
				throw new CoreException("Invalid LastUpdated for local perist/update.");
		}else
			throw new CoreException("Invalid Entity.");
	}
}
