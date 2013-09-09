package ui.listview;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.media.MediaSequence;
import data.media.element.MediaElement;
import data.media.element.imported.InportedMediaElement;
import ui.panel.SequencePanel;

public class ListView extends JTable {

	private static final long serialVersionUID = 1L;

	private final SequencePanel parent;

	public ListView (MediaSequence elements, final SequencePanel parent) {
		super(new ListViewModel(elements));

		this.parent = parent;

		setDefaultRenderer(MediaElement.class, new ListViewCell(this));
		setDefaultEditor(MediaElement.class, new ListViewCell(this));
		setRowHeight(35);

		setGridColor(Color.white);

		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setTableHeader(null);

		setColumnSelectionAllowed(false);
		setRowSelectionAllowed(true);

		//Update SequencePanel buttons
		getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) 
					checkSelectedRow();
			}
		});	
	}
	
	public void checkSelectedRow() {
		ListSelectionModel lsm = getSelectionModel();
		
		boolean isTop = false, isBottom = false, isDeletable = false;
		
		if(!lsm.isSelectionEmpty()) {
			isTop = getSelectedRow()==0;
			isBottom = getSelectedRow()==getRowCount()-1;
			isDeletable = getModel().getValueAt(getSelectedRow(), 0) instanceof InportedMediaElement;
		}
		parent.rowSelected(!lsm.isSelectionEmpty(), isTop, isBottom, isDeletable);
	}
	
	public MediaSequence getSequence() {
		return ((ListViewModel)getModel()).getSequence();
	}
	
	public void setSequence(MediaSequence sequence) {
		((ListViewModel)getModel()).setSequence(sequence);
	}
	
	/**
	 * Swaps the selected row with the one under or above it
	 */
	public void swapSelectedRow(SwapDirection direction) {
		int selectedRow = getSelectedRow();		
		int swapPos = selectedRow + (direction == SwapDirection.UP ? -1 : 1);
		if(swapPos >= 0 && swapPos < getRowCount()) {
			((ListViewModel)getModel()).swapRow(selectedRow, swapPos);
			setRowSelectionInterval(swapPos, swapPos);
			requestFocus();
		}
	}

	public enum SwapDirection{UP, DOWN}

	public void removeSelectedRow() {
		int selectedRow = getSelectedRow();	
		((ListViewModel)getModel()).removeRow(selectedRow);
		clearSelection();
		requestFocus();
	}
	
	public void addRow(MediaElement element) {
		((ListViewModel)getModel()).addRow(element);
	}

	public void replaceElement(MediaElement oldElement, MediaElement newElement) {
		((ListViewModel)getModel()).replaceRow(oldElement, newElement);
	}

	public void modifyRow(InportedMediaElement element) {
		parent.modifyRow(element);
	}
}