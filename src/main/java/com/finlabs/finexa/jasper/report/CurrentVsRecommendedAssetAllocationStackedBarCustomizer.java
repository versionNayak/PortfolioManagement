package com.finlabs.finexa.jasper.report;

import java.awt.Font;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

public class CurrentVsRecommendedAssetAllocationStackedBarCustomizer implements JRChartCustomizer {

	@SuppressWarnings("deprecation")
	@Override
	public void customize(JFreeChart chart, JRChart jasperChart) {
		// TODO Auto-generated method stub
		CategoryPlot categoryPlot = chart.getCategoryPlot();
		BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
		
		if(chart.getLegend() != null) {
		   chart.getLegend().setBorder(0.0, 0.0, 0.0, 0.0);	
		}
		
		renderer.setItemMargin(-1);
		categoryPlot.setRenderer(renderer);
		
		CategoryAxis domainAxis = categoryPlot.getDomainAxis();
		domainAxis.setCategoryMargin(0.1f);
		domainAxis.setMaximumCategoryLabelLines(2);
		
		renderer.setItemLabelAnchorOffset(0.1);
		renderer.setMaximumBarWidth(1f);
		renderer.setItemMargin(-1f);
		
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		Font itemLabelFont = renderer.getBaseItemLabelFont();
		renderer.setBaseItemLabelFont(itemLabelFont.deriveFont(new Float(11.0)));
		renderer.setBaseItemLabelsVisible(true);
		renderer.setItemLabelsVisible(true);
				
		categoryPlot.setRangeGridlinesVisible(false);
		categoryPlot.setDomainGridlinesVisible(false);
	}

}
