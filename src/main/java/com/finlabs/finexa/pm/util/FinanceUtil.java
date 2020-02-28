package com.finlabs.finexa.pm.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserSupervisorMapping;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.repository.AdvisorUserDAO;
import org.hibernate.Session;
public class FinanceUtil {

	public static double pmt(double r, double n, double p, double f, boolean t) {
		double retval = 0;
		if (r == 0) {
			retval = -1 * (f + p) / n;
		} else {
			double r1 = r + 1;
			retval = (f + p * Math.pow(r1, n)) * r / ((t ? r1 : 1) * (1 - Math.pow(r1, n)));
		}
		return retval;
	}

	public static double roundUpAmount(double amount) {
		double roundUpAmount = Math.round((amount * 100)) / 100.0;
		return roundUpAmount;
	}

	public static double STDEV(List<Double> medianList) {

		double stdev = 0;

		// Calculate mean
		double sum = 0.0;
		double mean = 0.0;
		for (Double a : medianList) {
			sum += a;

		}
		mean = sum / medianList.size();

		// Calculate Variance
		double temp = 0;
		double variance = 0;
		for (Double a : medianList)
			temp += (a - mean) * (a - mean);
		variance = temp / (medianList.size() - 1);

		stdev = Math.sqrt(variance);
		return stdev;

	}

	public static double AVERAGE(List<Double> listOfValues) {

		double average = 0.0;

		// Calculate average
		double sum = 0.0;
		int size = listOfValues.size();
		for (Double a : listOfValues) {
			sum += a;
		}
		average = sum / size;

		return average;

	}

	public static double YEARFRAC(Date startDate, Date maturityDate, int basis) {
		long diff = maturityDate.getTime() - startDate.getTime();

		long daysTotal = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

		double interestReceived = 0.0;
		if (basis == 1) {
			interestReceived = (daysTotal / (double) 365.3310);
		}

		return interestReceived;
	}

	public static double SLOPE(Double[] x, Double[] y) {
		Double slope;

		if (x.length != y.length) {
			throw new IllegalArgumentException("array lengths are not equal");
		}
		int n = x.length;

		// first pass
		double sumx = 0.0, sumy = 0.0;
		for (int i = 0; i < n; i++) {
			sumx += x[i];
			sumy += y[i];
		}
		double xbar = sumx / n;
		double ybar = sumy / n;

		// second pass: compute summary statistics
		double xxbar = 0.0, xybar = 0.0;
		for (int i = 0; i < n; i++) {
			xxbar += (x[i] - xbar) * (x[i] - xbar);
			xybar += (x[i] - xbar) * (y[i] - ybar);
		}
		slope = xybar / xxbar;

		return slope;
	}

	public List<ClientMaster> getClientMasterList(AdvisorUser advisorUser,AdvisorUserDAO advisorUserDAO,Session session){
		List<Integer> idList = new ArrayList<Integer>();
		List<ClientMaster> clientMasterList = null;
		try {
			// Admin
			idList.add(advisorUser.getId());
			System.out.println("idList "+idList);
		
			List<AdvisorUserSupervisorMapping> advisorUserSupervisorParentList;
			// Head
			advisorUserSupervisorParentList = advisorUserDAO.geSubUser(advisorUser.getId(), session);
			
			List<AdvisorUserSupervisorMapping> advisorUserSupervisorChildList = null;
			if (advisorUserSupervisorParentList != null && !advisorUserSupervisorParentList.isEmpty()) {
				for (AdvisorUserSupervisorMapping head : advisorUserSupervisorParentList) {
					idList.add(head.getAdvisorUser1().getId());
					System.out.println("idList "+idList);
					// BM
					advisorUserSupervisorChildList = advisorUserDAO.geSubUser(head.getAdvisorUser1().getId(), session);
					System.out.println(advisorUserSupervisorChildList);
					if (advisorUserSupervisorChildList!= null && !advisorUserSupervisorChildList.isEmpty()) {
						for (AdvisorUserSupervisorMapping advisorUserSupervisorBM : advisorUserSupervisorChildList) {
							idList.add(advisorUserSupervisorBM.getAdvisorUser1().getId());
							System.out.println("idList "+idList);
							// RM/SB
							advisorUserSupervisorChildList = advisorUserDAO.geSubUser(advisorUserSupervisorBM.getAdvisorUser1().getId(), session);
							if (advisorUserSupervisorChildList!= null && !advisorUserSupervisorChildList.isEmpty()) {
								for (AdvisorUserSupervisorMapping advisorUserSupervisorRM : advisorUserSupervisorChildList) {
									idList.add(advisorUserSupervisorRM.getAdvisorUser1().getId());
									System.out.println("idList "+idList);
								}
							}
						}
					}
				}
			}
			System.out.println("idList "+idList.size());
	    clientMasterList = advisorUserDAO.getClientsByUser(idList, session);
		System.out.println("clientMasterList "+clientMasterList);
		} catch (FinexaDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientMasterList;
	}
	
	@SuppressWarnings("null")
	public List<Integer> getUserList(AdvisorUser advisorUser,AdvisorUserDAO advisorUserDAO,Session session){
		List<Integer> idList = new ArrayList<Integer>();
		List<ClientMaster> clientMasterList = null;
		try {
		// Admin
		idList.add(advisorUser.getId());
		System.out.println("idList "+idList);
	
		List<AdvisorUserSupervisorMapping> advisorUserSupervisorParentList;
		// Head
		advisorUserSupervisorParentList = advisorUserDAO.geSubUser(advisorUser.getId(), session);
		
		List<AdvisorUserSupervisorMapping> advisorUserSupervisorChildList = null;
		if (advisorUserSupervisorParentList != null && !advisorUserSupervisorParentList.isEmpty()) {
			for (AdvisorUserSupervisorMapping head : advisorUserSupervisorParentList) {
				idList.add(head.getAdvisorUser1().getId());
				System.out.println("idList "+idList);
				// BM
				advisorUserSupervisorChildList = advisorUserDAO.geSubUser(head.getAdvisorUser1().getId(), session);
				System.out.println(advisorUserSupervisorChildList);
				if (advisorUserSupervisorChildList!= null && !advisorUserSupervisorChildList.isEmpty()) {
					for (AdvisorUserSupervisorMapping advisorUserSupervisorBM : advisorUserSupervisorChildList) {
						idList.add(advisorUserSupervisorBM.getAdvisorUser1().getId());
						System.out.println("idList "+idList);
						// RM/SB
						advisorUserSupervisorChildList = advisorUserDAO.geSubUser(advisorUserSupervisorBM.getAdvisorUser1().getId(), session);
						if (advisorUserSupervisorChildList!= null && !advisorUserSupervisorChildList.isEmpty()) {
							for (AdvisorUserSupervisorMapping advisorUserSupervisorRM : advisorUserSupervisorChildList) {
								idList.add(advisorUserSupervisorRM.getAdvisorUser1().getId());
								System.out.println("idList "+idList);
							}
						}
					}
				}
			}
		}
		System.out.println("idList "+idList.size());

		} catch (FinexaDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idList;
	}

}
