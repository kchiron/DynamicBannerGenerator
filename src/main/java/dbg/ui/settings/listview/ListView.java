package dbg.ui.settings.listview;

import dbg.data.media.MediaSequence;
import dbg.data.media.element.MediaElement;
import dbg.data.media.element.imported.ImportedMediaElement;
import dbg.ui.settings.panel.SequencePanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class ListView extends JTable {

	private static final long serialVersionUID = 1L;

	private final SequencePanel parent;

	public ListView(MediaSequence elements, final SequencePanel parent) {
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
				if (!e.getValueIsAdjusting())
					checkSelectedRow();
			}
		});
	}

	public void checkSelectedRow() {
		ListSelectionModel lsm = getSelectionModel();

		boolean isTop = false, isBottom = false, isDeletable = false;

		if (!lsm.isSelectionEmpty()) {
			isTop = getSelectedRow() == 0;
			isBottom = getSelectedRow() == getRowCount() - 1;
			isDeletable = getModel().getValueAt(getSelectedRow(), 0) instanceof ImportedMediaElement;
		}
		parent.rowSelected(!lsm.isSelectionEmpty(), isTop, isBottom, isDeletable);
	}

	public MediaSequence getSequence() {
		return ((ListViewModel) getModel()).getSequence();
	}

	public void setSequence(MediaSequence sequence) {
		((ListViewModel) getModel()).setSequence(sequence);
	}

	/**
	 * Swaps the selected row with the one under or above it
	 */
	public void swapSelectedRow(SwapDirection direction) {
		int selectedRow = getSelectedRow();
		int swapPos = selectedRow + (direction == SwapDirection.UP ? -1 : 1);
		if (swapPos >= 0 && swapPos < getRowCount()) {
			((ListViewModel) getModel()).swapRow(selectedRow, swapPos);
			setRowSelectionInterval(swapPos, swapPos);
			requestFocus();
		}
	}

	public enum SwapDirection {UP, DOWN}

	public void removeSelectedRow() {
		int selectedRow = getSelectedRow();
		((ListViewModel) getModel()).removeRow(selectedRow);
		clearSelection();
		requestFocus();
	}

	public void addRow(MediaElement element) {
		((ListViewModel) getModel()).addRow(element);
	}

	public void replaceElement(MediaElement oldElement, MediaElement newElement) {
		((ListViewModel) getModel()).replaceRow(oldElement, newElement);
	}

	public void modifyRow(ImportedMediaElement element) {
		parent.modifyRow(element);
	}
}