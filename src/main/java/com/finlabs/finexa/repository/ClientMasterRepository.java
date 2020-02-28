package com.finlabs.finexa.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.finlabs.finexa.model.ClientMaster;

public interface ClientMasterRepository extends JpaRepository<ClientMaster, Integer>, JpaSpecificationExecutor<ClientMaster> {
	
}
