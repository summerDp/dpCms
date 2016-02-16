package org.summery.dp.cms.entity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity implements java.io.Serializable{
	protected Long id;
	
}
