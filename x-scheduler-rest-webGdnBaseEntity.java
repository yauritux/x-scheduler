package com.gdn.common.base.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class GdnBaseEntity implements Serializable {

  public static final String MARK_FOR_DELETE = "MARK_FOR_DELETE";

  public static final String UPDATED_DATE = "UPDATED_DATE";

  public static final String CREATED_DATE = "CREATED_DATE";

  public static final String OPTLOCK = "OPTLOCK";

  public static final String STORE_ID = "STORE_ID";

  public static final String ID = "ID";

  private static final long serialVersionUID = 1L;

  public static final String CREATED_BY = "CREATED_BY";

  public static final String UPDATED_BY = "UPDATED_BY";

  @Id
  @Column(name = GdnBaseEntity.ID)
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid2")
  @org.springframework.data.annotation.Id
  private String id;

  @Column(name = GdnBaseEntity.STORE_ID)
  private String storeId;

  @Version
  @org.springframework.data.annotation.Version
  @Column(name = GdnBaseEntity.OPTLOCK)
  private Long version;

  @CreatedDate
  @Temporal(value = TemporalType.TIMESTAMP)
  @Column(name = GdnBaseEntity.CREATED_DATE, nullable = false)
  private Date createdDate;

  @CreatedBy
  @Column(name = GdnBaseEntity.CREATED_BY)
  private String createdBy;

  @LastModifiedDate
  @Temporal(value = TemporalType.TIMESTAMP)
  @Column(name = GdnBaseEntity.UPDATED_DATE)
  private Date updatedDate;

  @LastModifiedBy
  @Column(name = GdnBaseEntity.UPDATED_BY)
  private String updatedBy;

  @Column(name = GdnBaseEntity.MARK_FOR_DELETE)
  private boolean markForDelete;

  /*
 *    * (non-Javadoc)
 *       * 
 *          * @see java.lang.Object#equals(java.lang.Object)
 *             */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    GdnBaseEntity other = (GdnBaseEntity) obj;
    if (this.id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!this.id.equals(other.id)) {
      return false;
    }
    return true;
  }

  public String getCreatedBy() {
    return this.createdBy;
  }

  /**
 *    * @return the createdDate
 *       */
  public Date getCreatedDate() {
    return this.createdDate;
  }

  /**
 *    * @return the id
 *       */
  public String getId() {
    return this.id;
  }

  /**
 *    * @return the storeId
 *       */
  public String getStoreId() {
    return this.storeId;
  }

  public String getUpdatedBy() {
    return this.updatedBy;
  }

  /**
 *    * @return the updatedDate
 *       */
  public Date getUpdatedDate() {
    return this.updatedDate;
  }

  /**
 *    * @return the version
 *       */
  public Long getVersion() {
    return this.version;
  }

  /*
 *    * (non-Javadoc)
 *       * 
 *          * @see java.lang.Object#hashCode()
 *             */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
    return result;
  }

  /**
 *    * @return the markForDelete
 *       */
  public boolean isMarkForDelete() {
    return this.markForDelete;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
 *    * @param createdDate the createdDate to set
 *       */
  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  /**
 *    * @param id the id to set
 *       */
  public void setId(String id) {
    this.id = id;
  }

  /**
 *    * @param markForDelete the markForDelete to set
 *       */
  public void setMarkForDelete(boolean markForDelete) {
    this.markForDelete = markForDelete;
  }

  /**
 *    * @param storeId the storeId to set
 *       */
  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  /**
 *    * @param updatedDate the updatedDate to set
 *       */
  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
  }

  /**
 *    * @param version the version to set
 *       */
  public void setVersion(Long version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return String
        .format(
            "GdnBaseEntity [id=%s, storeId=%s, version=%s, createdDate=%s, updatedDate=%s, markForDelete=%s, toString()=%s]",
            this.id, this.storeId, this.version, this.createdDate, this.updatedDate,
            this.markForDelete, super.toString());
  }



}

