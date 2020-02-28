package com.finlabs.finexa.jasper.report;

import java.awt.BasicStroke;
import java.text.NumberFormat;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

public class CurrentAssetAllocationPieCustomizer implements JRChartCustomizer {

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
			 //piePlot.setLabelLinkStroke(new strok);
			 //piePlot.setExplodePercent(1, 0.30);
			 //piePlot.setExplodePercent(2, 0.30);
			 //piePlot.setExplodePercent(3, 0.30);
			 //piePlot.setExplodePercent(4, 0.30);
		 }
	}

}
