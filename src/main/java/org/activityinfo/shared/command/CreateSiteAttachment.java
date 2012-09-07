package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.VoidResult;

public class CreateSiteAttachment implements MutatingCommand<VoidResult> {


	private static final long serialVersionUID = 1008206027004197062L;
	
	private int siteId;
    private String blobId;
    private String fileName;
    private int uploadedBy;
    private double blobSize;
    private String contentType;
	
    public CreateSiteAttachment(){
    	
    }
    
    public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public String getBlobId() {
		return blobId;
	}
	public void setBlobId(String blodId) {
		this.blobId = blodId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(int uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public double getBlobSize() {
		return blobSize;
	}

	public void setBlobSize(double blobSize) {
		this.blobSize = blobSize;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	} 
    
}
