package com.googlecode.assignmentdialog.ui.composite;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.googlecode.assignmentdialog.core.AssignmentCompositeModel;
import com.googlecode.assignmentdialog.core.IAssignable;

/**
 * {@link AbstractTableModel} which evaluates the {@link AssignmentCompositeModel} to render the columns and their
 * values properly.
 * 
 * @author Kai Winter
 * 
 * @param <T>
 *            the concrete class used in the assignment dialog.
 */
@SuppressWarnings("serial")
public final class AssignmentTableModel<T> extends AbstractTableModel {

	// The names of the columns.
	private final List<String> tableColumNames;

	// The actual values which are shown in the table.
	private final List<IAssignable<T>> assignables;

	/**
	 * Constructs a new {@link AbstractTableModel}.
	 * 
	 * @param tableColumNames
	 *            {@link List} of column names
	 * @param assignables
	 *            {@link List} of {@link IAssignable}s which will be shown in the table.
	 */
	public AssignmentTableModel(List<String> tableColumNames, List<IAssignable<T>> assignables) {
		this.tableColumNames = tableColumNames;
		this.assignables = assignables;
	}

	@Override
	public int getRowCount() {
		return assignables.size();
	}

	@Override
	public int getColumnCount() {
		return tableColumNames.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		IAssignable<T> assignable = assignables.get(rowIndex);
		String valueAt = assignable.getValueAt(columnIndex);
		return valueAt;
	}

	@Override
	public String getColumnName(int columnNumber) {
		assert tableColumNames.size() > columnNumber;

		String columnLabel = tableColumNames.get(columnNumber);
		return columnLabel;
	}

	/**
	 * @return the assignables
	 */
	public List<IAssignable<T>> getAssignables() {
		return assignables;
	}

	public void setAssignables(List<IAssignable<T>> assignables) {
		this.assignables.clear();
		this.assignables.addAll(assignables);
	}

}
