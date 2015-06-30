package com.googlecode.assignmentdialog.core;

import com.googlecode.assignmentdialog.ui.dialog.AssignmentDialog;

/**
 * Interface for objects which should be handled by the {@link AssignmentDialog}. Implementing class wraps &lt;T&gt; to
 * allow them being used in the assignment composite.
 * 
 * @author Kai Winter
 * 
 * @param <T>
 *            The concrete object.
 */
public interface IAssignable<T> {

	/**
	 * @return the wrapped object.
	 */
	T getAssignableObject();

	/**
	 * Returns the String representation for a specific column defined by the <code>columnIndex</code> by mapping the
	 * index to a value of the assignable object.
	 * 
	 * @param columnIndex
	 *            the index of the column.
	 * @return the String representation for the given column index.
	 */
	String getValueAt(int columnIndex);
}
