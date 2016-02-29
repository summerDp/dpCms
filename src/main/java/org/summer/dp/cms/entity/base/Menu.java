/**
 * @author 
 * @version 1.0
 * @since  2016-02-29 22:27:51
 * @desc Menu
 */

package org.summer.dp.cms.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "menu")
@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})//序列化成json是不序列化这两个属性否则会报错
public class Menu implements java.io.Serializable{
	private static final long serialVersionUID = -1L;
	
	//alias
	public static final String TABLE_ALIAS = "Menu";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_NAME = "菜单名字";
	public static final String ALIAS_URI = "连接";
	public static final String ALIAS_LEVEL = "菜单级别";
	public static final String ALIAS_WEIGHT = "菜单权重排序倒序";
	public static final String ALIAS_ACCOUNT_ID = "accountId";
	public static final String ALIAS_CREATE_TIME = "createTime";
	public static final String ALIAS_CREATE_USER = "createUser";
	public static final String ALIAS_UPDATE_TIME = "updateTime";
	public static final String ALIAS_UPDATE_USER = "updateUser";
	
	

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	
	private Long id;
	@NotBlank @Length(max=64)
	private java.lang.String name;
	@Length(max=255)
	private java.lang.String uri;
	@Max(32767)
	private Short level;
	@Max(32767)
	private Short weight;
	
	private Long accountId;
	
	private java.util.Date createTime;
	
	private Long createUser;
	
	private java.util.Date updateTime;
	
	private Long updateUser;
	//columns END


	public Menu(){
	}

	public Menu(
		Long id
	){
		this.id = id;
	}

	

	public void setId(Long value) {
		this.id = value;
	}
	
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 19)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "table")
	@GenericGenerator(name = "table", strategy = "org.hibernate.id.MultipleHiLoPerTableGenerator", parameters = {
			@Parameter(name = "max_lo", value = "5") })//增长级别为5，可根据并发级别适当调整
	public Long getId() {
		return this.id;
	}
	
	@Column(name = "NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 64)
	public java.lang.String getName() {
		return this.name;
	}
	
	public void setName(java.lang.String value) {
		this.name = value;
	}
	
	@Column(name = "URI", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getUri() {
		return this.uri;
	}
	
	public void setUri(java.lang.String value) {
		this.uri = value;
	}
	
	@Column(name = "LEVEL", unique = false, nullable = true, insertable = true, updatable = true, length = 5)
	public Short getLevel() {
		return this.level;
	}
	
	public void setLevel(Short value) {
		this.level = value;
	}
	
	@Column(name = "WEIGHT", unique = false, nullable = true, insertable = true, updatable = true, length = 5)
	public Short getWeight() {
		return this.weight;
	}
	
	public void setWeight(Short value) {
		this.weight = value;
	}
	
	@Column(name = "ACCOUNT_ID", unique = false, nullable = true, insertable = true, updatable = true, length = 19)
	public Long getAccountId() {
		return this.accountId;
	}
	
	public void setAccountId(Long value) {
		this.accountId = value;
	}
	

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name = "CREATE_TIME", unique = false, nullable = true, insertable = true, updatable = true, length = 0)
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
	
	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	@Column(name = "CREATE_USER", unique = false, nullable = true, insertable = true, updatable = true, length = 19)
	public Long getCreateUser() {
		return this.createUser;
	}
	
	public void setCreateUser(Long value) {
		this.createUser = value;
	}
	

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name = "UPDATE_TIME", unique = false, nullable = true, insertable = true, updatable = true, length = 0)
	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}
	
	public void setUpdateTime(java.util.Date value) {
		this.updateTime = value;
	}
	
	@Column(name = "UPDATE_USER", unique = false, nullable = true, insertable = true, updatable = true, length = 19)
	public Long getUpdateUser() {
		return this.updateUser;
	}
	
	public void setUpdateUser(Long value) {
		this.updateUser = value;
	}
	
	
	private Account account;
	
	public void setAccount(Account account){
		this.account = account;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_ID",nullable = false, insertable = false, updatable = false)
	public Account getAccount() {
		return account;
	}

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("Name",getName())
			.append("Uri",getUri())
			.append("Level",getLevel())
			.append("Weight",getWeight())
			.append("AccountId",getAccountId())
			.append("CreateTime",getCreateTime())
			.append("CreateUser",getCreateUser())
			.append("UpdateTime",getUpdateTime())
			.append("UpdateUser",getUpdateUser())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Menu == false) return false;
		if(this == obj) return true;
		Menu other = (Menu)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
}

