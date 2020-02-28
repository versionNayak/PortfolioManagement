package com.finlabs.finexa.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AdvisorProductReco;

public interface AdvisorProductRecoRepository extends JpaRepository<AdvisorProductReco, Integer>{
	
	@Query("select max(adv.recoSaveDate) from AdvisorProductReco adv where adv.advisorUser.id = :advisorID and adv.clientMaster.id = :clientID "
			+ "and adv.module= :module")
			public Date getMaxDateOfSavedProductRecoPM(@Param("advisorID") int advisorID, @Param("clientID") int clientID, @Param("module") String module);
	
	@Query("select adv.productPlan from AdvisorProductReco adv where adv.advisorUser.id = :advisorID and adv.clientMaster.id = :clientID "
			+ "and adv.module= :module and adv.recoSaveDate = :recoSaveDate")
			public String getLastSavedProductPlan(@Param("advisorID") int advisorID, @Param("clientID") int clientID, @Param("module") String module,  @Param("recoSaveDate") Date recoSaveDate);

}
