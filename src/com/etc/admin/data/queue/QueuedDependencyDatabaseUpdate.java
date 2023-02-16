package com.etc.admin.data.queue;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Triple;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.data.LocalDataManager;
import com.etc.entities.CoreData;

/**
 * <p>
 * QueuedDependencyDatabaseUpdate uses a provided Parent entity Class, <br>
 * the entity id, and a List of optional mappings to build the entity in <br>
 * question. The entity is fetched locally, the list of optional mappings is<br>
 * iterated over and these dependent entities are fetched, either locally or from<br>
 * the server. If they are received from the server, the dependent type is udpated<br>
 * with its requirements and returned for use. </p>
 * 
 * @author Stephen Carter
 * @since 09/01/2021
 *
 * @param <T>
 */
public class QueuedDependencyDatabaseUpdate<T extends CoreData> implements Runnable, Serializable 
{
	private static final long serialVersionUID = -8073888716006735907L;

	private Class<T> entityClass;
	private Long entityId;
	private Logger logr;

	@SuppressWarnings("rawtypes")
	private List optionals;

	public <X extends CoreData> QueuedDependencyDatabaseUpdate(Class<T> entityClass, Long entityId, List<Triple<Class<X>,Long,String>> optionals)
	{
		super();
		this.entityClass = entityClass;
		this.entityId = entityId;
		setOptionals(optionals);
		logr = Logger.getLogger(this.getClass().getCanonicalName());
	}

	public <X extends CoreData> void setOptionals(List<Triple<Class<X>,Long,String>> optionals) { this.optionals = optionals; }
	@SuppressWarnings("unchecked")
	public <X extends CoreData> List<Triple<Class<X>,Long,String>> getOptionals() { return optionals; }
	
	@Override
	public void run() 
	{
		T entity = null;
		CoreData setObj = null;
		Method setter = null;
		boolean failedSetter = false;
		
		try
		{
			if(entityClass != null  && (entityId != null && entityId > 0l))
			{
				try (LocalDataManager mgr = new LocalDataManager(EmsApp.getInstance().getEntityManager()))
				{
					if((entity = mgr.getLocal(entityClass, entityId)) != null && !getOptionals().isEmpty())
					{
						for(Triple<Class<CoreData>,Long,String> triple : getOptionals())
						{
							try { setter = entity.getClass().getMethod(triple.getRight(), triple.getLeft()); }
							catch(NoSuchMethodException | SecurityException e2) 
							{ throw new CoreException("Unable to locate setter method for type : " + entity.getClass().getName(), e2); }
							
							if(setter != null)
							{
								//fetch dependent type
								if((setObj = mgr.get(triple.getLeft(), triple.getMiddle())) != null)
								{
									//update entity if it is not managed
									if(!mgr.getEntityManager().contains(setObj))
										setObj = mgr.updateEntity(setObj);
									
									//set dependent on parent
									try  { setter.invoke(entity, setObj); }
									catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e3) 
									{ throw new CoreException("Unable to invoke setter method for type: " + entity.getClass().getName() + "." + triple.getRight(), e3); }
								}else
									failedSetter = true;
							}else
								throw new CoreException("The setter method '" + triple.getRight() + "' was null for Type " + triple.getLeft().getName());
						}
						//mark this entity as loaded (optionals and required)
						entity.setPartialLoad(failedSetter);
								
					}else
						throw new CoreException("The Type " + entityClass.getName() + " :: " + entityId + " did not exist or no Optional relationships were provided.");
				} 
			}
		}catch(CoreException e)
		{
			logr.log(Level.SEVERE, "Failure in QueuedDependencyDatabaseUpdate: ", e);
		}finally
		{
			entity = null;
			setObj = null;
			setter = null;
		}
	}
}
