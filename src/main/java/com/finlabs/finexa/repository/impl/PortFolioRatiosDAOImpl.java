package com.finlabs.finexa.repository.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.genericDao.GenericDao;
import com.finlabs.finexa.model.MasterWealthRatioComment;
import com.finlabs.finexa.repository.PortFolioRatiosDAO;

@Repository
public class PortFolioRatiosDAOImpl implements PortFolioRatiosDAO {
	/*
	 * @Autowired GenericDao genericDao;
	 */

	public List<MasterWealthRatioComment> geMasterWealthCommentList(Session session) throws FinexaDaoException {
		List<MasterWealthRatioComment> masterWealthCommentList = null;
		try {
			GenericDao genericDao = new GenericDao();
			masterWealthCommentList = genericDao.loadAll(MasterWealthRatioComment.class, session);

			return masterWealthCommentList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

}
