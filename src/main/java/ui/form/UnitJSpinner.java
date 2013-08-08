package ui.form;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	public UnitJSpinner(String unit, final Integer min, final Integer max) {
		super();
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		setLayout(flowLayout);
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		
		final JSpinner spinNumber = new JSpinner();
		spinNumber.setPreferredSize(new Dimension(60, 22));
		JLabel lblUnit = new JLabel(unit);
		
		//Restrict value of the spinner
		if(min != null || max != null) {
			spinNumber.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if(min != null && (Integer)spinNumber.getValue() < min) spinNumber.setValue(min);
					else if(max != null && (Integer)spinNumber.getValue() > max) spinNumber.setValue(max);
				}
			});
		}
		
		add(spinNumber, BorderLayout.CENTER);
		add(lblUnit, BorderLayout.EAST);
	}
}
