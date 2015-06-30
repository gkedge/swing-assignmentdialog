package com.googlecode.assignmentdialog.ui.composite.filter;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * {@link TableModel} for the table filter rows.
 * 
 * @author Kai Winter
 */
@SuppressWarnings("serial")
public abstract class FilterTableModel extends AbstractTableModel {

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public abstract void setValueAt(Object aValue, int rowIndex, int columnIndex);
}
