package com.googlecode.assignmentdialog.ui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import com.googlecode.assignmentdialog.ui.composite.AssignmentComposite;
import com.googlecode.assignmentdialog.ui.composite.AssignmentCompositeController;

/**
 * Dialog which shows the {@link AssignmentComposite} in a {@link JDialog} and provides buttons for accepting or
 * canceling the selection. This class uses the {@link AssignmentComposite} which can be used stand-alone also for
 * creating an own Dialog.
 * <p>
 * <b>Hint:</b> For creating an {@link AssignmentDialog} there is the {@link AssignmentDialogFactory}.
 * </p>
 * 
 * @author Kai Winter
 * 
 * @param <T>
 *            the concrete class used in the assignment dialog.
 */
@SuppressWarnings("serial")
public final class AssignmentDialog<T> extends JDialog {
	private AssignmentDialogController<T> controller;
	private AssignmentCompositeController<T> assignmentCompositeController;

	/**
	 * Creates an AssignmentDialog.
	 */
	public AssignmentDialog() {
		this(null, false);
	}

	/**
	 * Creates an AssignmentDialog.
	 * 
	 * @param parent
	 *            the parent {@link JFrame} could be <code>null</code>.
	 * @param modal
	 *            if true the dialog will be opened in modal state.
	 */
	public AssignmentDialog(Frame parent, boolean modal) {
		super(parent, modal);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setBounds(100, 100, 600, 400);
		getContentPane().setLayout(new BorderLayout());
		{
			assignmentCompositeController = new AssignmentCompositeController<T>();
			AssignmentComposite<T> assignmentComposite = new AssignmentComposite<T>();
			assignmentCompositeController.setAssignmentComposite(assignmentComposite);
			assignmentComposite.setAssignmentCompositeController(assignmentCompositeController);

			getContentPane().add(assignmentComposite, BorderLayout.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						controller.okClicked();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						controller.cancelClicked();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		// Close dialog on ESC
		installEscapeCloseOperation(this);
	}

	private void installEscapeCloseOperation(final JDialog dialog) {
		Action dispatchClosing = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
			}
		};

		KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		String dispatchWindowClosingActionMapKey = "WINDOW_CLOSING";

		JRootPane root = dialog.getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, dispatchWindowClosingActionMapKey);
		root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
	}

	/**
	 * Sets the dialog controller.
	 * 
	 * @param controller
	 *            the controller.
	 */
	public void setDialogController(AssignmentDialogController<T> controller) {
		this.controller = controller;
	}

	/**
	 * @return the assignmentCompositeController
	 */
	public AssignmentCompositeController<T> getAssignmentCompositeController() {
		return assignmentCompositeController;
	}
}
