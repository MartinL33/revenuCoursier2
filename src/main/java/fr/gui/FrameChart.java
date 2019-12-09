package fr.gui;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

import fr.modele.Value;

public class FrameChart extends JFrame{


	private static final long serialVersionUID = 8350825883415633916L;

	public FrameChart(CategoryDataset dataset){

		//get local graphics environment
		GraphicsEnvironment graphicsEnvironment=GraphicsEnvironment.getLocalGraphicsEnvironment();

		//get maximum window bounds
		Rectangle maximumWindowBounds=graphicsEnvironment.getMaximumWindowBounds();
		int widthFenetre=(int) maximumWindowBounds.getWidth()-1;
		int heightFenetre=(int) (maximumWindowBounds.getHeight()-1);
		int widthLocaFenetre=0;
		int heightLocaFenetre=0;

		this.setTitle("Chiffre d'affaire par heure regroupé "+Value.regroupageSelected.textCombo);
		
		
		this.setVisible(true);
		
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		
		chartPanel.setBackground(Color.white);
		add(chartPanel);	

		pack();
		
		this.setSize(widthFenetre, heightFenetre);
		this.setLocation(widthLocaFenetre, heightLocaFenetre);
		
		
		
	}


	
	private JFreeChart createChart(CategoryDataset dataset) {
		
		
		
		 JFreeChart chart = ChartFactory.createLineChart(
		         null,
		         "Date",null,
		         dataset,
		         PlotOrientation.VERTICAL,
		         true,true,false);
		 
		 CategoryPlot plot = (CategoryPlot) chart.getPlot();
		 CategoryAxis catAxis = plot.getDomainAxis();
		 catAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_90);
		
		return chart;

	}




}
