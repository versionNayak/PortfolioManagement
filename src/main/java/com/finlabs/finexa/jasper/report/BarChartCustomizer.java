package com.finlabs.finexa.jasper.report;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

public class BarChartCustomizer implements JRChartCustomizer {
	
	@Override
	public void customize(JFreeChart chart, JRChart jasperChart) {
		
		CategoryPlot categoryPlot = chart.getCategoryPlot();
		BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
		 
		renderer.setItemMargin(-0.5);
		categoryPlot.setRenderer(renderer);
		 
		CategoryAxis domainAxis = categoryPlot.getDomainAxis();
		domainAxis.setCategoryMargin(0.3f);
		domainAxis.setMaximumCategoryLabelLines(2);

		renderer.setItemLabelAnchorOffset(10);
		renderer.setBaseItemLabelsVisible(true);
		renderer.setMaximumBarWidth(1f);
		renderer.setItemMargin(-1f);
		
		categoryPlot.setRangeGridlinesVisible(false);
		categoryPlot.setDomainGridlinesVisible(false);
	}

}
