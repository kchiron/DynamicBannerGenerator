package ui.custom;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class UnitJSpinner extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public UnitJSpinner(String unit) {
		this(unit, null, null);
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public UnitJSpinner(String unit, Integer min, Integer max) {
		super();
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		setLayout(flowLayout);
		setOpaque(false);
		
		JSpinner spinNumber = new JSpinner();
		spinNumber.setPreferredSize(new Dimension(60, 22));
		JLabel lblUnit = new JLabel(unit);
		
		if(min != null && max != null) {
			//TODO restrict spinner value to be between min and max
		}
		
		add(spinNumber, BorderLayout.CENTER);
		add(lblUnit, BorderLayout.EAST);
	}
}
