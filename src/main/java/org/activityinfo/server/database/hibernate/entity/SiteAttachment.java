package org.activityinfo.server.database.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "siteattachment")
@NamedQueries({
    @NamedQuery(name = "findSiteAttachments", query = "select s from SiteAttachment s where s.blobId = :blobId") })
public class SiteAttachment implements java.io.Serializable {


	private static final long serialVersionUID = -619220161104158193L;
	
	private int siteId;
    private String blobId;
    private String fileName;
    private int uploadedBy;
    private double blobSize;
    private String contentType;
    
   
    public SiteAttachment() {
    }
    
    @Id
    @Column(name = "blobid", nullable = false, length = 255)
	public String getBlobId() {
		return blobId;
	}

	public void setBlobId(String blobId) {
		this.blobId = blobId;
	}

	@Column(name = "siteid", nullable = false, length = 11)
	public int getSiteId() {
		return siteId;
	}


	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

    @Column(name = "filename", nullable = false, length = 255)
	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

    @Column(name = "uploadedby", nullable = false, length = 11)
	public int getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(int uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

    @Column(name = "blobsize", nullable = false)
	public double getBlobSize() {
		return blobSize;
	}

	public void setBlobSize(double blobSize) {
		this.blobSize = blobSize;
	}

    @Column(name = "contenttype", nullable = false, length = 255)
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
    

}
