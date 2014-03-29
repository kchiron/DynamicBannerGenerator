package dbg.ui.form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Enhanced integer JSpinner with the possibility of a unit displayed and the possibility of fixing min and max bounds.
 * @author gcornut
 */
public class UnitJSpinner extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JSpinner spinner;
	
	private final ArrayList<ActionListener> customListeners;

	/**
	 * @wbp.parser.constructor
	 */
	public UnitJSpinner(String unit, final Integer min, final Integer max) {
		super(new FlowLayout());
		
		this.customListeners = new ArrayList<>(1);
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		setLayout(flowLayout);
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		
		spinner = new JSpinner();
		spinner.setPreferredSize(new Dimension(60, 22));
		add(spinner, BorderLayout.CENTER);
		
		if(unit != null && !unit.isEmpty()) {
			JLabel lblUnit = new JLabel(unit);
			add(lblUnit, BorderLayout.EAST);	
		}
		
		//Restrict value of the spinner
		if(min != null || max != null) {
			if(min != null) spinner.setValue(min);
			spinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if(min != null && (Integer)spinner.getValue() < min) spinner.setValue(min);
					else if(max != null && (Integer)spinner.getValue() > max) spinner.setValue(max);
					
					for(ActionListener listener : customListeners)
						listener.actionPerformed(new ActionEvent(spinner, ActionEvent.ACTION_PERFORMED, "state changed"));
				}
			});
		}
	}
	
	public UnitJSpinner(final Integer min, final Integer max) {
		this(null, min, max);
	}
	
	public UnitJSpinner(String unit) {
		this(unit, null, null);
	}
	
	public JSpinner getSpinner() {
		return spinner;
	}
	
	public int getValue() {
		return (int) spinner.getValue();
	}

	public void setValue(int displayTime) {
		spinner.setValue(displayTime);
	}
	
	public void addActionListener(ActionListener listener) {
		customListeners.add(listener);
	}
	
}
