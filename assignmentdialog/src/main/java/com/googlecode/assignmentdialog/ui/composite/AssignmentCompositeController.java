package com.googlecode.assignmentdialog.ui.composite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.RowFilter;

import com.googlecode.assignmentdialog.core.AssignmentCompositeModel;
import com.googlecode.assignmentdialog.core.IAssignable;

/**
 * Creates the composite and controls the assignment between the two tables of the dialog.
 * 
 * @author Kai Winter
 * 
 * @param <T>
 *            the concrete class used in the assignment dialog.
 */
public class AssignmentCompositeController<T> {

	private AssignmentComposite<T> assignmentComposite;

	private AssignmentCompositeModel<T> assignmentCompositeModel;

	private Map<Integer, String> currentFilterValuesLeft;
	private Map<Integer, String> currentFilterValuesRight;

	/**
	 * Constructs a new {@link AssignmentCompositeController}.
	 */
	public AssignmentCompositeController() {
		currentFilterValuesLeft = new HashMap<Integer, String>();
		currentFilterValuesRight = new HashMap<Integer, String>();
	}

	/**
	 * Moves all entities from the right table to the left.
	 */
	void moveAllLeft() {
		List<IAssignable<T>> valuesRightVisible = assignmentComposite.getVisibleTableRowValuesRight();
		if (valuesRightVisible.isEmpty()) {
			return;
		}

		assignmentCompositeModel.getTableRowValuesLeft().addAll(valuesRightVisible);
		assignmentCompositeModel.getTableRowValuesRight().removeAll(valuesRightVisible);

		assignmentComposite.refreshTables();
	}

	/**
	 * Moves the selected entities from the right table to the left.
	 */
	void moveLeft() {
		List<IAssignable<T>> selectedValuesRight = assignmentComposite.getSelectedValuesRight();
		if (selectedValuesRight.isEmpty()) {
			return;
		}

		List<IAssignable<T>> valuesLeft = assignmentCompositeModel.getTableRowValuesLeft();
		List<IAssignable<T>> valuesRight = assignmentCompositeModel.getTableRowValuesRight();

		valuesLeft.addAll(selectedValuesRight);
		valuesRight.removeAll(selectedValuesRight);

		assignmentComposite.refreshTables();
	}

	/**
	 * Moves the selected entities from the left table to the right.
	 */
	void moveRight() {
		List<IAssignable<T>> selectedValuesLeft = assignmentComposite.getSelectedValuesLeft();
		if (selectedValuesLeft.isEmpty()) {
			return;
		}

		List<IAssignable<T>> valuesLeft = assignmentCompositeModel.getTableRowValuesLeft();
		List<IAssignable<T>> valuesRight = assignmentCompositeModel.getTableRowValuesRight();

		valuesRight.addAll(selectedValuesLeft);
		valuesLeft.removeAll(selectedValuesLeft);

		assignmentComposite.refreshTables();
	}

	/**
	 * Moves all entities from the left table to the right.
	 */
	void moveAllRight() {
		List<IAssignable<T>> valuesLeftVisible = assignmentComposite.getVisibleTableRowValuesLeft();
		if (valuesLeftVisible.isEmpty()) {
			return;
		}

		assignmentCompositeModel.getTableRowValuesRight().addAll(valuesLeftVisible);
		assignmentCompositeModel.getTableRowValuesLeft().removeAll(valuesLeftVisible);

		assignmentComposite.refreshTables();
	}

	/**
	 * Moves the selected items in the right table to the top.
	 */
	void moveToTop() {
		List<IAssignable<T>> selectedValuesRight = assignmentComposite.getSelectedValuesRight();
		if (selectedValuesRight.isEmpty()) {
			return;
		}

		List<IAssignable<T>> valuesRight = assignmentCompositeModel.getTableRowValuesRight();
		valuesRight.removeAll(selectedValuesRight);
		List<IAssignable<T>> tmpList = new ArrayList<IAssignable<T>>(valuesRight);
		valuesRight.clear();
		valuesRight.addAll(selectedValuesRight);
		valuesRight.addAll(tmpList);

		assignmentComposite.refreshTables();
	}

	/**
	 * Moves the selected items in the right table one position up.
	 */
	void moveUp() {
		List<IAssignable<T>> selectedValuesRight = assignmentComposite.getSelectedValuesRight();
		if (selectedValuesRight.isEmpty()) {
			return;
		}

		List<IAssignable<T>> valuesRight = assignmentCompositeModel.getTableRowValuesRight();

		for (IAssignable<T> selectedValue : selectedValuesRight) {
			int oldIndex = valuesRight.indexOf(selectedValue);
			if (oldIndex == 0) { // already on top
				continue;
			}

			valuesRight.remove(selectedValue);
			valuesRight.add(oldIndex - 1, selectedValue);
		}
		assignmentComposite.refreshTables();
		assignmentComposite.selectValuesRight(selectedValuesRight);
	}

	/**
	 * Moves the selected items in the right table one position down.
	 */
	void moveDown() {
		List<IAssignable<T>> selectedValuesRight = assignmentComposite.getSelectedValuesRight();
		if (selectedValuesRight.isEmpty()) {
			return;
		}

		List<IAssignable<T>> valuesRight = assignmentCompositeModel.getTableRowValuesRight();

		Collections.reverse(selectedValuesRight);
		for (IAssignable<T> selectedValue : selectedValuesRight) {
			int oldIndex = valuesRight.indexOf(selectedValue);
			if (oldIndex == valuesRight.size() - 1) { // already at the end
				continue;
			}
			valuesRight.remove(selectedValue);
			valuesRight.add(oldIndex + 1, selectedValue);
		}
		assignmentComposite.refreshTables();
		assignmentComposite.selectValuesRight(selectedValuesRight);
	}

	/**
	 * Moves the selected items in the right table to the bottom.
	 */
	void moveToBottom() {
		List<IAssignable<T>> valuesRight = assignmentCompositeModel.getTableRowValuesRight();
		List<IAssignable<T>> selectedValuesRight = assignmentComposite.getSelectedValuesRight();
		if (selectedValuesRight.isEmpty()) {
			return;
		}

		valuesRight.removeAll(selectedValuesRight);
		valuesRight.addAll(selectedValuesRight);
		assignmentComposite.refreshTables();
	}

	/**
	 * @param assignmentCompositeModel
	 *            the ssignmentCompositeModel to set
	 */
	public void setAssignmentCompositeModel(AssignmentCompositeModel<T> assignmentCompositeModel) {
		this.assignmentCompositeModel = assignmentCompositeModel;

		// Initialize tables
		assert assignmentComposite != null : "assignmentComposite not initialized!";
		if (assignmentCompositeModel == null) {
			throw new IllegalStateException("No AssignmentConpositeMode set!");
		}
		List<String> tableColumNames = assignmentCompositeModel.getTableColumNames();

		List<IAssignable<T>> tableRowValuesLeft = assignmentCompositeModel.getTableRowValuesLeft();
		AssignmentTableModel<T> assignmentTableModelLeft = getAssignmentTableModelLeft(tableColumNames, tableRowValuesLeft);
		assignmentComposite.setTableModelLeft(assignmentTableModelLeft);

		List<IAssignable<T>> tableRowValuesRight = assignmentCompositeModel.getTableRowValuesRight();
		AssignmentTableModel<T> assignmentTableModelRight = getAssignmentTableModelRight(tableColumNames, tableRowValuesRight);
		assignmentComposite.setTableModelRight(assignmentTableModelRight);
	}

	protected AssignmentTableModel<T> getAssignmentTableModelRight(List<String> tableColumNames, List<IAssignable<T>> tableRowValuesRight) {
		return new AssignmentTableModel<T>(tableColumNames,
				tableRowValuesRight);
	}

	protected AssignmentTableModel<T> getAssignmentTableModelLeft(List<String> tableColumNames, List<IAssignable<T>> tableRowValuesLeft) {
		return new AssignmentTableModel<T>(tableColumNames, tableRowValuesLeft);
	}

	/**
	 * The list of elements which are unassigned (left table entries).
	 * 
	 * @return the elements on the left side of the assignment.
	 */
	public List<IAssignable<T>> getUnassignedElements() {
		List<IAssignable<T>> assignedElements = assignmentCompositeModel.getTableRowValuesLeft();
		return assignedElements;
	}

	/**
	 * The list of elements which are assigned to the right side.
	 * 
	 * @return the elements on the right side of the assignment.
	 */
	public List<IAssignable<T>> getAssignedElements() {
		List<IAssignable<T>> assignedElements = assignmentCompositeModel.getTableRowValuesRight();
		return assignedElements;
	}

	/**
	 * @param assignmentComposite
	 *            the assignmentComposite to set
	 */
	public void setAssignmentComposite(AssignmentComposite<T> assignmentComposite) {
		this.assignmentComposite = assignmentComposite;
	}

	/**
	 * Sets the <code>value</code> of the column with the given <code>columnIndex</code>.
	 * 
	 * @param value
	 *            the new value.
	 * @param columnIndex
	 *            the index of the column.
	 */
	void setLeftFilterValueAt(String value, int columnIndex) {
		currentFilterValuesLeft.put(columnIndex, value);
	}

	/**
	 * Returns the current value of the column with the given <code>columnIndex</code>.
	 * 
	 * @param columnIndex
	 *            the index of the column.
	 * @return the current value.
	 */
	String getLeftFilterValueAt(int columnIndex) {
		return currentFilterValuesLeft.get(columnIndex);
	}

	/**
	 * Sets the <code>value</code> of the column with the given <code>columnIndex</code>.
	 * 
	 * @param value
	 *            the new value.
	 * @param columnIndex
	 *            the index of the column.
	 */
	void setRightFilterValueAt(String value, int columnIndex) {
		currentFilterValuesRight.put(columnIndex, value);
	}

	/**
	 * Returns the current value of the column with the given <code>columnIndex</code>.
	 * 
	 * @param columnIndex
	 *            the index of the column.
	 * @return the current value.
	 */
	String getRightFilterValueAt(int columnIndex) {
		return currentFilterValuesRight.get(columnIndex);
	}

	/**
	 * Called after a value of the left filters were changed.
	 */
	void filterLeftChanged() {
		RowFilter<AssignmentTableModel<T>, Object> andRowFilter = buildRowFilter(currentFilterValuesLeft.entrySet());
		assignmentComposite.setTableRowFilterLeft(andRowFilter);
	}

	/**
	 * Called after a value of the right filters were changed.
	 */
	void filterRightChanged() {
		RowFilter<AssignmentTableModel<T>, Object> andRowFilter = buildRowFilter(currentFilterValuesRight.entrySet());
		assignmentComposite.setTableRowFilterRight(andRowFilter);
	}

	/**
	 * Gets the filter values and creates a {@link RowFilter}.
	 */
	private RowFilter<AssignmentTableModel<T>, Object> buildRowFilter(Set<Entry<Integer, String>> entrySet) {
		Collection<RowFilter<AssignmentTableModel<T>, Object>> filters = new ArrayList<RowFilter<AssignmentTableModel<T>, Object>>();
		for (Entry<Integer, String> columnFilterValue : entrySet) {
			String value = columnFilterValue.getValue();
			if (value == null || value.isEmpty()) {
				continue;
			}

			Integer column = columnFilterValue.getKey();
			// case insensitive regex
			RowFilter<AssignmentTableModel<T>, Object> filter = RowFilter.regexFilter("(?i)" + value, column);
			filters.add(filter);
		}

		RowFilter<AssignmentTableModel<T>, Object> andRowFilter = RowFilter.andFilter(filters);
		return andRowFilter;
	}

	/**
	 * @return the assignmentComposite
	 */
	public AssignmentComposite<T> getAssignmentComposite() {
		return assignmentComposite;
	}
}
