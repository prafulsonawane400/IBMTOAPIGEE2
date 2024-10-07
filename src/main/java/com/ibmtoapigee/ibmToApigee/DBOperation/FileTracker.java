package com.ibmtoapigee.ibmToApigee.DBOperation;

import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fileTracker")
public class FileTracker {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fileUniqueId;

	private String fileId;

	private String fileName;

	private Timestamp createdDate;

	private boolean isDeployedToApigee;

	private boolean isUploadToApigee;

	private String logs;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
	public Long getFileUniqueId() {
		return fileUniqueId;
	}

	public void setFileUniqueId(Long fileUniqueId) {
		this.fileUniqueId = fileUniqueId;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public boolean isDeployedToApigee() {
		return isDeployedToApigee;
	}

	public void setDeployedToApigee(boolean isDeployedToApigee) {
		this.isDeployedToApigee = isDeployedToApigee;
	}

	public boolean isUploadToApigee() {
		return isUploadToApigee;
	}

	public void setUploadToApigee(boolean isUploadToApigee) {
		this.isUploadToApigee = isUploadToApigee;
	}

	public String getLogs() {
		return logs;
	}

	public void setLogs(String logs) {
		this.logs = logs;
	}

}
