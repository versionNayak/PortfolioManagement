package com.finlabs.finexa.repository;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.MasterWealthRatioComment;

public interface PortFolioRatiosDAO {

	public List<MasterWealthRatioComment> geMasterWealthCommentList(Session session) throws FinexaDaoException;
}
