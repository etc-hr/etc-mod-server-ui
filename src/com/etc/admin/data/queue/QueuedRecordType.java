package com.etc.admin.data.queue;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * QueuedRecordType is a tracking 'Key' class used as the key in a HashMap<br>
 * to track the processing of queued updates. Whenever a new queuedUpdate is created,<br>
 * a new QueuedRecordType is added to the tracking list to prevent any dependend updates<br>
 * pointing at the type from running. The Utility is used to track all running and waiting queues.</p>
 * 
 * @author Stephen Carter
 *
 * @param <X> CoreData class
 */
public class QueuedRecordType<X> implements Serializable 
{
	private static final long serialVersionUID = -2107510144275557585L;
	
	private Class<X> queueClass = null;
	private Long classId = null;
	private boolean queued = false;

	private QueuedRecordType() { super(); }
	
	public QueuedRecordType(Class<X> queueClass)
	{
		this();
		this.queueClass = queueClass;
	}
	
	public QueuedRecordType(Class<X> queueClass, Long classId)
	{
		this(queueClass);
		this.classId = classId;
	}
	

	public Class<X> getQueueClass() { return queueClass; } 
	public void setQueueClass(Class<X> queueClass) { this.queueClass = queueClass; }

	
	public Long getClassId() { return classId; } 
	public void setClassId(Long classId) { this.classId = classId; }
	
	public boolean isQueued() { return this.queued; }
	public void setIsQueued(boolean queued) { this.queued = queued; } 

	@Override
	public int hashCode() {
		return Objects.hash(queueClass);
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
		QueuedRecordType<X> other = (QueuedRecordType<X>) obj;
		return Objects.equals(queueClass, other.queueClass) && (classId == null || Objects.equals(classId, other.classId));
	}
}
