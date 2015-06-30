package com.googlecode.assignmentdialog.ui.dialog;

import java.util.EventListener;
import java.util.List;

import com.googlecode.assignmentdialog.core.IAssignable;

/**
 * Listener to handle button events of the {@link AssignmentDialog}.
 * 
 * @author Kai Winter
 * 
 * @param <T>
 *            the concrete class used in the assignment dialog.
 */
public interface AssignmentExecutedListener<T> extends EventListener {

	/**
	 * Called after the 'ok' button was executed.
	 * 
	 * @param assignmentDialog
	 *            the dialog. <b>The caller have to take care of disposing it.</b>
	 * @param assignedElements
	 *            the assigned elements in the right table.
	 */
	void okClicked(AssignmentDialog<T> assignmentDialog, List<IAssignable<T>> assignedElements);

	/**
	 * Called after the 'cancel' button was executed.
	 * 
	 * @param assignmentDialog
	 *            the dialog. <b>The caller have to take car of disposing it.</b>
	 */
	void cancelClicked(AssignmentDialog<T> assignmentDialog);
}
