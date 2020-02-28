package com.finlabs.finexa.repository.impl;





import java.util.List;

import org.hibernate.Session;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserSupervisorMapping;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.repository.AdvisorUserDAO;



//@Repository
public class AdvisorUserDaoImpl implements AdvisorUserDAO {

	@Override
	public AdvisorUser getAdvisorUserByID(int advisorUserID, Session session) throws FinexaDaoException {
		
			try {
				AdvisorUser advisorUser = (AdvisorUser) session.get(AdvisorUser.class, new Integer(advisorUserID));
				return advisorUser;

			} catch (Exception e) {
				throw new FinexaDaoException(e.getMessage());

			}

		}	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AdvisorUserSupervisorMapping> geSubUser(int userid, Session session) throws FinexaDaoException {
		try {
			List<AdvisorUserSupervisorMapping> advisorUserSupervisorParentList = null;
			advisorUserSupervisorParentList = (List<AdvisorUserSupervisorMapping>) session.createQuery("SELECT asm FROM AdvisorUserSupervisorMapping asm WHERE asm.advisorUser2.id = :userid")
					.setInteger("userid", userid).list();
			// session.clear();
			return advisorUserSupervisorParentList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ClientMaster> getClientsByUser(List<Integer> userids, Session session) throws FinexaDaoException {
		try {
			List<ClientMaster> ClientMasterList = null;
			ClientMasterList = (List<ClientMaster>) session.createQuery("SELECT cm FROM ClientMaster cm WHERE cm.advisorUser.id in :userid")
					.setParameterList("userid", userids).list();
			// session.clear();
			return ClientMasterList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}
	
	
	
}
