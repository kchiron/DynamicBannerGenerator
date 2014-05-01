package dbg.ui.settings.listview;

import dbg.data.media.MediaSequence;
import dbg.data.media.element.MediaElement;

import javax.swing.table.AbstractTableModel;
import java.util.Collections;

public class ListViewModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private MediaSequence sequence;

	public ListViewModel(MediaSequence sequence) {
		this.sequence = sequence;
	}

	public int getRowCount() {
		return (sequence != null) ? sequence.size() : 0;
	}

	public int getColumnCount() {
		return 1;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return (sequence != null) ? sequence.get(rowIndex) : null;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return MediaElement.class;
	}

	@Override
	public String getColumnName(int paramInt) {
		return "Elements";
	}

	@Override
	public boolean isCellEditable(int paramInt1, int paramInt2) {
		return true;
	}

	public void addRow(MediaElement mediaElement) {
		sequence.add(mediaElement);
		fireTableStructureChanged();
	}

	public void removeRow(int index) {
		sequence.remove(index);
		fireTableStructureChanged();
	}

	public void swapRow(int row, int nextRow) {
		Collections.swap(sequence, row, nextRow);
		fireTableStructureChanged();
	}

	public MediaSequence getSequence() {
		return sequence;
	}

	public void setSequence(MediaSequence sequence) {
		this.sequence = sequence;
		fireTableStructureChanged();
	}

	public void replaceRow(MediaElement oldElement, MediaElement newElement) {
		sequence.set(sequence.indexOf(oldElement), newElement);
		fireTableStructureChanged();
	}
}
