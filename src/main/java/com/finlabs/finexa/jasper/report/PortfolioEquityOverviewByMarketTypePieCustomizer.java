package com.finlabs.finexa.jasper.report;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

public class PortfolioEquityOverviewByMarketTypePieCustomizer implements JRChartCustomizer {

	@Override
	public void customize(JFreeChart chart, JRChart jasperChart) {
		// TODO Auto-generated method stub
		if(chart.getLegend() != null) {
		   chart.getLegend().setBorder(0.0, 0.0, 0.0, 0.0);	
		   //chart.getLegend().setMargin(40, 0, 0, 0);
		 }
			
		 Plot plot = chart.getPlot();
		 if (plot instanceof PiePlot) {
			 PiePlot piePlot = (PiePlot) plot;
			 piePlot.setLabelOutlineStroke(null);
			 piePlot.setLabelShadowPaint(null);
			 piePlot.setShadowXOffset(0);
			 piePlot.setShadowYOffset(0);
			 //piePlot.setExplodePercent(1, 0.30);
			 //piePlot.setExplodePercent(2, 0.30);
			 //piePlot.setExplodePercent(3, 0.30);
			 //piePlot.setExplodePercent(4, 0.30);
		 }
	}

}
