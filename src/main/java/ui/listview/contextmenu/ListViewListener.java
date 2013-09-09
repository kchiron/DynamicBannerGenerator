package ui.listview.contextmenu;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import data.media.element.MediaElement;
import ui.listview.ListViewCell;

public class ListViewListener extends MouseAdapter {
	
	private MediaElement element;
	private ListViewCell parent;
	
	public ListViewListener(ListViewCell parent) {
		this.parent = parent;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.isPopupTrigger())
			doPop(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.isPopupTrigger())
			doPop(e);
	}
	
	public void setElement(MediaElement element) {
		this.element = element;
	}
	
	private void doPop(MouseEvent e) {
		ListViewContextMenu menu = new ListViewContextMenu(element, parent);
		menu.show(e.getComponent(), e.getX(), e.getY());
	}
}
