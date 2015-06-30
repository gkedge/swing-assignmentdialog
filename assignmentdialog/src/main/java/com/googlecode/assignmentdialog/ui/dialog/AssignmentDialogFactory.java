package com.googlecode.assignmentdialog.ui.dialog;

import java.awt.Frame;

import javax.swing.SortOrder;

import com.googlecode.assignmentdialog.core.AssignmentCompositeModel;
import com.googlecode.assignmentdialog.ui.composite.AssignmentComposite;
import com.googlecode.assignmentdialog.ui.composite.LeftRightButtonsPosition;
import com.googlecode.assignmentdialog.ui.composite.LeftRightButtonsVisible;
import com.googlecode.assignmentdialog.ui.composite.filter.FilterVisible;

/**
 * Factory for creating an {@link AssignmentDialog}.
 * 
 * @author Kai Winter
 * 
 * @param <T>
 *            the concrete class used in the assignment dialog.
 */
public final class AssignmentDialogFactory<T> {

	// Dialogs default values
	private Frame parent = null;
	private boolean modal = true;
	private AssignmentCompositeModel<T> assignmentCompositeModel = null;
	private AssignmentExecutedListener<T> assignmentExecutedListener = null;
	private FilterVisible visibleFilter = FilterVisible.NONE;
	private boolean showLeftRightButtonIcons = true;
	private LeftRightButtonsPosition leftRightButtonsPosition = LeftRightButtonsPosition.MIDDLE;
	private LeftRightButtonsVisible leftRightButtonsVisible = LeftRightButtonsVisible.ALL_BUTTONS;
	private boolean reorderEnabled = true;
	private String tableTitleLeft = "Available";
	private String tableTitleRight = "Selected";
	private String buttonTextAllToLeft = null;
	private String buttonTextAllToRight = null;
	private String buttonTextToLeft = null;
	private String buttonTextToRight = null;
	private String dialogTitle = null;
	private boolean tablesSortable = true;
	private Integer sortedColumnIndex = null;
	private SortOrder sortedColumnOrder;

	// Position and size
	private int x = 0;
	private int y = 0;
	private int width = 600;
	private int height = 400;

	/**
	 * @param parent
	 *            {@link Frame} which should be the parent of the {@link AssignmentDialog}.
	 * @return the {@link AssignmentDialogFactory}
	 */
	public AssignmentDialogFactory<T> setParent(Frame parent) {
		this.parent = parent;
		return this;
	}

	/**
	 * @param modal
	 *            if true the {@link AssignmentDialog} will be set to model.
	 * @return the {@link AssignmentDialogFactory}
	 */
	public AssignmentDialogFactory<T> setModal(boolean modal) {
		this.modal = modal;
		return this;
	}

	/**
	 * Creates the {@link AssignmentDialogController} which was previously configured by the
	 * {@link AssignmentDialogFactory}.
	 * 
	 * @return an {@link AssignmentDialogController} instance.
	 */
	public AssignmentDialogController<T> create() {
		// Create dialog
		AssignmentDialog<T> assignmentDialog = new AssignmentDialog<T>(parent, modal);
		AssignmentDialogController<T> assignmentController = new AssignmentDialogController<T>();

		// Configure dialog
		assignmentDialog.setTitle(dialogTitle);

		// Configure assignment composite
		AssignmentComposite<T> assignmentComposite = assignmentDialog.getAssignmentCompositeController()
				.getAssignmentComposite();
		assignmentComposite.setVisibleFilter(visibleFilter);
		assignmentComposite.setLeftRightButtonIconsVisible(showLeftRightButtonIcons);
		assignmentComposite.setLeftRightButtonsPosition(leftRightButtonsPosition);
		assignmentComposite.setLeftRightButtonsVisible(leftRightButtonsVisible);
		assignmentComposite.setReorderEnabled(reorderEnabled);
		assignmentComposite.setTableTitleLeft(tableTitleLeft);
		assignmentComposite.setTableTitleRight(tableTitleRight);
		if (buttonTextAllToLeft != null) {
			assignmentComposite.setButtonTextAllToLeft(buttonTextAllToLeft);
		}
		if (buttonTextAllToRight != null) {
			assignmentComposite.setButtonTextAllToRight(buttonTextAllToRight);
		}
		if (buttonTextToLeft != null) {
			assignmentComposite.setButtonTextToLeft(buttonTextToLeft);
		}
		if (buttonTextToRight != null) {
			assignmentComposite.setButtonTextToRight(buttonTextToRight);
		}
		assignmentComposite.setTableSortableLeft(tablesSortable);
		assignmentComposite.setTableSortableRight(tablesSortable);
		if (sortedColumnIndex != null) {
			assignmentComposite.setSorting(sortedColumnIndex, sortedColumnOrder);
		}

		// Wire dialog with controller
		assignmentDialog.setDialogController(assignmentController);
		assignmentController.setAssignmentDialog(assignmentDialog);

		// Set data and add listener
		assignmentController.setAssignableCompositeModel(assignmentCompositeModel);
		assignmentController.addAssignmentExecutedListener(assignmentExecutedListener);

		// Set bounds
		assignmentDialog.setBounds(x, y, width, height);

		return assignmentController;
	}

	/**
	 * 
	 * @param assignmentCompositeModel
	 *            the {@link AssignmentCompositeModel} to set.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setAssignmentCompositeModel(AssignmentCompositeModel<T> assignmentCompositeModel) {
		this.assignmentCompositeModel = assignmentCompositeModel;
		return this;
	}

	/**
	 * @param assignmentExecutedListener
	 *            an {@link AssignmentExecutedListener}.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setAssignmentExecutedListener(
			AssignmentExecutedListener<T> assignmentExecutedListener) {
		this.assignmentExecutedListener = assignmentExecutedListener;
		return this;
	}

	/**
	 * @param visibleFilter
	 *            an {@link FilterVisible}.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setVisibleFilter(FilterVisible visibleFilter) {
		this.visibleFilter = visibleFilter;
		return this;
	}

	/**
	 * @param showLeftRightButtonIcons
	 *            if set to true the left/right buttons will show pre-configured images.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setShowLeftRightButtonIcons(boolean showLeftRightButtonIcons) {
		this.showLeftRightButtonIcons = showLeftRightButtonIcons;
		return this;
	}

	/**
	 * @param leftRightButtonsPosition
	 *            a {@link LeftRightButtonsPosition}.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setLeftRightButtonsPosition(LeftRightButtonsPosition leftRightButtonsPosition) {
		this.leftRightButtonsPosition = leftRightButtonsPosition;
		return this;
	}

	/**
	 * @param leftRightButtonsVisible
	 *            a {@link LeftRightButtonsVisible}.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setLeftRightButtonsVisible(LeftRightButtonsVisible leftRightButtonsVisible) {
		this.leftRightButtonsVisible = leftRightButtonsVisible;
		return this;
	}

	/**
	 * @param reorderEnabled
	 *            if set to false the buttons for moving items up/down in the right table are not shown.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setReorderEnabled(boolean reorderEnabled) {
		this.reorderEnabled = reorderEnabled;
		return this;
	}

	/**
	 * @param tableLeftTitle
	 *            the title of the left table.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setTableLeftTitle(String tableLeftTitle) {
		this.tableTitleLeft = tableLeftTitle;
		return this;
	}

	/**
	 * @param tableRightTitle
	 *            the title of the right table.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setTableRightTitle(String tableRightTitle) {
		this.tableTitleRight = tableRightTitle;
		return this;
	}

	/**
	 * @param buttonTextAllToLeft
	 *            the text which will be displayed on the button 'All to left'.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setButtonTextAllToLeft(String buttonTextAllToLeft) {
		this.buttonTextAllToLeft = buttonTextAllToLeft;
		return this;
	}

	/**
	 * @param buttonTextAllToRight
	 *            the text which will be displayed on the button 'All to right'.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setButtonTextAllToRight(String buttonTextAllToRight) {
		this.buttonTextAllToRight = buttonTextAllToRight;
		return this;
	}

	/**
	 * @param buttonTextToLeft
	 *            the text which will be displayed on the button 'Selected to left'.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setButtonTextToLeft(String buttonTextToLeft) {
		this.buttonTextToLeft = buttonTextToLeft;
		return this;
	}

	/**
	 * @param buttonTextToRight
	 *            the text which will be displayed on the button 'Selected to left'.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setButtonTextToRight(String buttonTextToRight) {
		this.buttonTextToRight = buttonTextToRight;
		return this;
	}

	/**
	 * @param dialogTitle
	 *            the title of the {@link AssignmentDialog}.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
		return this;
	}

	/**
	 * @param tablesSortable
	 *            if true both tables are sortable by clicking their headers.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setTablesSortable(boolean tablesSortable) {
		this.tablesSortable = tablesSortable;
		return this;
	}

	/**
	 * Advises the dialog to sort the column with the given index on start. The index is 0-based and defaults to
	 * <code>null</code>. If <code>null</code> no column will get sorted. Setting a sorted column implicitly calls
	 * {@link #setTablesSortable(true)}.
	 * 
	 * @param sortedColumnIndex
	 *            the index of the column to sort.
	 * @param sortOrder
	 *            the {@link SortOrder}.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setSortedColumn(Integer sortedColumnIndex, SortOrder sortOrder) {
		setTablesSortable(true); // for doing this the table must be sortable.
		this.sortedColumnIndex = sortedColumnIndex;
		this.sortedColumnOrder = sortOrder;
		return this;
	}

	/**
	 * Set the position and size of the dialog.
	 * 
	 * @param x
	 *            the x position on the screen.
	 * @param y
	 *            the y position on the screen.
	 * @param width
	 *            the width of the dialog.
	 * @param height
	 *            the height of the dialog.
	 * @return the {@link AssignmentDialogFactory}.
	 */
	public AssignmentDialogFactory<T> setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		return this;
	}
}
