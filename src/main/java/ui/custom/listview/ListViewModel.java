package ui.custom.listview;

import java.util.List;
import java.lang.reflect.ParameterizedType;

import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

public class ListViewModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private List<Sequence> elements;
	
	public ListViewModel(List<Sequence> elements) {
		this.elements = elements;
	}
	
	public int getRowCount() {
		return (elements != null) ? elements.size() : 0;
	}
	
	public int getColumnCount() {
		return 1;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		return (elements != null) ? elements.get(rowIndex) : null;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Sequence.class;
	}
	
	@Override
	public String getColumnName(int paramInt) {
		return "Elements";
	}
	
	@Override
	public boolean isCellEditable(int paramInt1, int paramInt2) {
		return true;
	}
	
	public void addRow(Sequence element) {
		elements.add(element);
	}
}
