package com.googlecode.assignmentdialog.ui.dialog;

/**
 * Abstract adapter class which provides a common handling of the {@link AssignmentDialog} cancel event.
 * 
 * @author Kai Winter
 * 
 * @param <T>
 *            the concrete class used in the assignment dialog.
 * 
 */
public abstract class AssignmentExecutedAdapter<T> implements AssignmentExecutedListener<T> {

	@Override
	public void cancelClicked(AssignmentDialog<T> assignmentDialog) {
		assignmentDialog.dispose();
	}
}
