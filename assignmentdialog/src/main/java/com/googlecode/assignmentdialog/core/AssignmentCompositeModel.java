package com.googlecode.assignmentdialog.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.googlecode.assignmentdialog.ui.dialog.AssignmentDialogController;

/**
 * Stores the configuration for a {@link AssignmentDialogController}.
 * 
 * @author Kai Winter
 * 
 * @param <T>
 *            the concrete class used in the assignment dialog.
 */
public final class AssignmentCompositeModel<T> {

	// TODO KW: replace String List by configuration object to store additional column metadata like column widths etc?
	private final List<String> tableColumNames;

	private final List<IAssignable<T>> tableRowValuesLeft;
	private final List<IAssignable<T>> tableRowValuesRight;

	/**
	 * Constructs a new {@link AssignmentCompositeModel} with a set left table and an empty right table.
	 * 
	 * @param tableColumNames
	 *            {@link List} of column names
	 * @param tableRowValuesLeft
	 *            {@link List} of {@link IAssignable}s which will be shown in the <b>left</b> table.
	 */
	public AssignmentCompositeModel(List<String> tableColumNames, List<IAssignable<T>> tableRowValuesLeft) {
		this(tableColumNames, tableRowValuesLeft, new ArrayList<IAssignable<T>>());
	}

	/**
	 * Constructs a new {@link AssignmentCompositeModel} with a set left and right table.
	 * 
	 * @param tableColumNames
	 *            {@link List} of column names
	 * @param tableRowValuesLeft
	 *            {@link List} of {@link IAssignable}s which will be shown in the <b>left</b> table.
	 * @param tableRowValuesRight
	 *            {@link List} of {@link IAssignable}s which will be shown in the <b>right</b> table.
	 */
	public AssignmentCompositeModel(List<String> tableColumNames, List<IAssignable<T>> tableRowValuesLeft,
			List<IAssignable<T>> tableRowValuesRight) {
		this.tableColumNames = new ArrayList<String>(tableColumNames);
		this.tableRowValuesLeft = new ArrayList<IAssignable<T>>(tableRowValuesLeft);
		this.tableRowValuesRight = new ArrayList<IAssignable<T>>(tableRowValuesRight);
	}

	/**
	 * @return the tableColumNames
	 */
	public List<String> getTableColumNames() {
		return Collections.unmodifiableList(tableColumNames);
	}

	/**
	 * @return the tableRowValues which are shown in the left table.
	 */
	public List<IAssignable<T>> getTableRowValuesLeft() {
		return tableRowValuesLeft;
	}

	/**
	 * @return the tableRowValues which are shown in the right table.
	 */
	public List<IAssignable<T>> getTableRowValuesRight() {
		return tableRowValuesRight;
	}

}
