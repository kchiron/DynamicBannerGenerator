package ui.custom.listview;

import java.awt.Color;
import java.util.List;

import javax.swing.JTable;

public class ListView extends JTable {
	
	private static final long serialVersionUID = 1L;

	public ListView (List<Sequence> elements) {
		super(new ListViewModel(elements));

		setDefaultRenderer(Sequence.class, new ListViewCell());
		setDefaultEditor(Sequence.class, new ListViewCell());
		setRowHeight(35);
		
		setGridColor(Color.white);
		
		setTableHeader(null);
	}

}