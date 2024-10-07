package com.ibmtoapigee.ibmToApigee.DBOperation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileTrackerRepository extends CrudRepository<FileTracker, String> {

	FileTracker findByfileId(String fileId);

}
