package com.googlecode.assignmentdialog.samples.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.UIManager;

import com.googlecode.assignmentdialog.core.AssignmentCompositeModel;
import com.googlecode.assignmentdialog.core.IAssignable;
import com.googlecode.assignmentdialog.samples.PersonTo;
import com.googlecode.assignmentdialog.samples.PersonToAssignable;
import com.googlecode.assignmentdialog.ui.composite.filter.FilterVisible;
import com.googlecode.assignmentdialog.ui.dialog.AssignmentDialog;
import com.googlecode.assignmentdialog.ui.dialog.AssignmentDialogController;
import com.googlecode.assignmentdialog.ui.dialog.AssignmentDialogFactory;

/**
 * Demonstrates the usage of the {@link AssignmentDialogFactory} to create a {@link AssignmentDialog}.
 * 
 * @author Kai Winter
 */
public final class AssignmentDialogDemo {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		// Create data
		List<String> tableColumNames = Arrays.asList("Firstname", "Lastname");
		List<IAssignable<PersonTo>> tableRowValuesLeft = new ArrayList<IAssignable<PersonTo>>();
		tableRowValuesLeft.add(new PersonToAssignable(new PersonTo("Firstname 1", "Lastname 3")));
		tableRowValuesLeft.add(new PersonToAssignable(new PersonTo("Firstname 2", "Lastname 2")));
		tableRowValuesLeft.add(new PersonToAssignable(new PersonTo("Firstname 3", "Lastname 1")));

		// Create model
		AssignmentCompositeModel<PersonTo> model = new AssignmentCompositeModel<PersonTo>(tableColumNames,
				tableRowValuesLeft);

		// Create dialog instance using factory
		AssignmentDialogFactory<PersonTo> dialogFactory = new AssignmentDialogFactory<PersonTo>();
		AssignmentDialogController<PersonTo> dialogController = dialogFactory //
				.setDialogTitle("Dialog Demo") //
				.setAssignmentCompositeModel(model) //
				.setVisibleFilter(FilterVisible.BOTH) //
				.setTableRightTitle("Assigned") //
				.create();

		// Show dialog
		dialogController.showDialog();
	}
}
