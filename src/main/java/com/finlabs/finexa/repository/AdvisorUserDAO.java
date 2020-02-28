package com.finlabs.finexa.repository;



import java.util.List;
import org.hibernate.Session;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserSupervisorMapping;
import com.finlabs.finexa.model.ClientMaster;


public interface AdvisorUserDAO{

	public AdvisorUser getAdvisorUserByID(int ID, Session session) throws FinexaDaoException;

	public List<AdvisorUserSupervisorMapping> geSubUser(int userid, Session session) throws FinexaDaoException;

	List<ClientMaster> getClientsByUser(List<Integer> userids, Session session) throws FinexaDaoException;

	
}

