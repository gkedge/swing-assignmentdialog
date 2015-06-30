package com.googlecode.assignmentdialog.samples.composite;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.googlecode.assignmentdialog.core.AssignmentCompositeModel;
import com.googlecode.assignmentdialog.core.IAssignable;
import com.googlecode.assignmentdialog.samples.PersonTo;
import com.googlecode.assignmentdialog.samples.PersonToAssignable;
import com.googlecode.assignmentdialog.ui.composite.AssignmentComposite;
import com.googlecode.assignmentdialog.ui.composite.AssignmentCompositeController;
import com.googlecode.assignmentdialog.ui.composite.filter.FilterVisible;

/**
 * Demonstrates the integration of the {@link AssignmentComposite} in a custom application.
 *
 * @author Kai Winter
 */
@SuppressWarnings("serial")
public class AssignmentCompositeDemo extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					AssignmentCompositeDemo frame = new AssignmentCompositeDemo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AssignmentCompositeDemo() {
		setTitle("Composite Demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// Create data
		List<String> tableColumNames = Arrays.asList("Firstname", "Lastname");
		List<IAssignable<PersonTo>> tableRowValuesLeft = new ArrayList<IAssignable<PersonTo>>();
		tableRowValuesLeft.add(new PersonToAssignable(new PersonTo("Firstname 1", "Lastname 3")));
		tableRowValuesLeft.add(new PersonToAssignable(new PersonTo("Firstname 2", "Lastname 2")));
		tableRowValuesLeft.add(new PersonToAssignable(new PersonTo("Firstname 3", "Lastname 1")));

		// Create model
		AssignmentCompositeModel<PersonTo> model = new AssignmentCompositeModel<PersonTo>(tableColumNames, tableRowValuesLeft);

		// Create and configure Composite
		AssignmentComposite<PersonTo> assignmentComposite = new AssignmentComposite<PersonTo>();
		assignmentComposite.setTableTitleRight("Assigned");
		assignmentComposite.setVisibleFilter(FilterVisible.BOTH);

		// Create and configure Composite Controller
		AssignmentCompositeController<PersonTo> assignmentCompositeController = new AssignmentCompositeController<PersonTo>();
		assignmentCompositeController.setAssignmentComposite(assignmentComposite);
		assignmentCompositeController.setAssignmentCompositeModel(model);

		// Wire composite and controller
		assignmentComposite.setAssignmentCompositeController(assignmentCompositeController);

		contentPane.add(assignmentComposite, BorderLayout.CENTER);
	}

}
