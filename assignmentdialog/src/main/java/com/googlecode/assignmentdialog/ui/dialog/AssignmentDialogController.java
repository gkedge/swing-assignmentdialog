package com.googlecode.assignmentdialog.ui.dialog;

import java.util.List;

import com.googlecode.assignmentdialog.core.AssignmentCompositeModel;
import com.googlecode.assignmentdialog.core.IAssignable;

/**
 * Creates the dialog and controls the assignment between the two tables of the dialog.
 * 
 * @author Kai Winter
 * 
 * @param <T>
 *            the concrete class used in the assignment dialog.
 */
public final class AssignmentDialogController<T> {

	private AssignmentDialog<T> assignmentDialog;

	private AssignmentExecutedListener<T> assignmentExecutedListener;

	void okClicked() {
		if (assignmentExecutedListener != null) {
			List<IAssignable<T>> assignedElements = assignmentDialog.getAssignmentCompositeController()
					.getAssignedElements();
			assignmentExecutedListener.okClicked(assignmentDialog, assignedElements);
		}
	}

	void cancelClicked() {
		if (assignmentExecutedListener != null) {
			assignmentExecutedListener.cancelClicked(assignmentDialog);
		}
	}

	/**
	 * Adds a {@link AssignmentExecutedListener} to the dialog which is fired after the ok or cancel button was clicked.
	 * 
	 * @param assignmentExecutedListener
	 *            the listener.
	 */
	public void addAssignmentExecutedListener(AssignmentExecutedListener<T> assignmentExecutedListener) {
		this.assignmentExecutedListener = assignmentExecutedListener;
	}

	/**
	 * @param assignableCompositeModel
	 *            the assignableCompositeModel to set
	 */
	public void setAssignableCompositeModel(AssignmentCompositeModel<T> assignableCompositeModel) {
		assignmentDialog.getAssignmentCompositeController().setAssignmentCompositeModel(assignableCompositeModel);
	}

	/**
	 * @param assignmentDialog
	 *            the assignmentDialog to set
	 */
	public void setAssignmentDialog(AssignmentDialog<T> assignmentDialog) {
		this.assignmentDialog = assignmentDialog;
	}

	/**
	 * Calls <code>setVisible(true)</code> on the JDialog.
	 */
	public void showDialog() {
		assignmentDialog.setVisible(true);
	}
}
