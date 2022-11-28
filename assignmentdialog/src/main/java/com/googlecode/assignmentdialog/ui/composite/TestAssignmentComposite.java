package com.googlecode.assignmentdialog.ui.composite;

import com.googlecode.assignmentdialog.core.IAssignable;
import com.googlecode.assignmentdialog.ui.composite.filter.FilterVisible;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TestAssignmentComposite<T> implements AssignmentCompositeIF<T> {

    private AssignmentCompositeController<T> compositeController;

    private Action arrowActionLeft = new AbstractAction() {
        {
            putValue(NAME, "Left Arrow");
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    private Action arrowActionRight = new AbstractAction() {
        {
            putValue(NAME, "Right Arrow");
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    private Action arrowAllActionLeft = new AbstractAction() {
        {
            putValue(NAME, "Left All Arrow");
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    private Action allArrowActionRight = new AbstractAction() {
        {
            putValue(NAME, "Right All Arrow");
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    private Action allUpArrowAction = new AbstractAction() {
        {
            putValue(NAME, "All Up Arrow");
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    private Action allDownArrowAction = new AbstractAction() {
        {
            putValue(NAME, "All Down Arrow");
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    private Action upArrowAction = new AbstractAction() {
        {
            putValue(NAME, "Up Arrow");
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    private Action downArrowAction = new AbstractAction() {
        {
            putValue(NAME, "Down Arrow");
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    private AssignmentTableModel<T> tableModelLeft;

    private AssignmentTableModel<T> tableModelRight;

    private TableRowSorter<AssignmentTableModel<T>> rowSorterLeft;
    private TableRowSorter<AssignmentTableModel<T>> rowSorterRight;

    private final ListSelectionModel tableRowSelectionModelLeft = new DefaultListSelectionModel();
    private final ListSelectionModel tableRowSelectionModelRight = new DefaultListSelectionModel();

    private final TitledBorder titledBorderLeft = new TitledBorder(null, "Available",
            TitledBorder.LEADING, TitledBorder.TOP, null, null);
    private final TitledBorder titledBoarderRight = new TitledBorder(null, "Selected",
            TitledBorder.LEADING, TitledBorder.TOP, null, null);

    private boolean tableLeftSortable;
    private boolean tableRightSortable;
    private Integer sortColumnIndex = null;
    private SortOrder sortColumnOrder;

    /**
     * Create the dialog.
     */
    public TestAssignmentComposite() {

        initListListener();

        setLeftRightButtonIconsVisible(true);
    }

    private static @Nullable ImageIcon getIcon(String oneDown) {
        URL resource = AssignmentComposite.class.getResource(oneDown);
        return resource != null ? new ImageIcon(resource) : null;
    }

    public void clear() {
        tableRowSelectionModelLeft.clearSelection();
        tableRowSelectionModelRight.clearSelection();
    }

    @Override
    public void updateEnablement() {

        arrowActionRight.setEnabled(!getSelectedValuesLeft().isEmpty());
        arrowActionLeft.setEnabled(!getSelectedValuesRight().isEmpty());

        if (tableModelLeft != null) {
            List<IAssignable<T>> assignablesLeft = tableModelLeft.getAssignables();
            allArrowActionRight.setEnabled(!assignablesLeft.isEmpty());
        } else {
            allArrowActionRight.setEnabled(false);
        }

        if (tableModelRight != null) {
            List<IAssignable<T>> assignablesRight = tableModelRight.getAssignables();
            arrowAllActionLeft.setEnabled(!assignablesRight.isEmpty());
        } else {
            arrowAllActionLeft.setEnabled(false);
        }

        // reorder buttons
        boolean selectedRight = getSelectedValuesRight().isEmpty();
        allUpArrowAction.setEnabled(!selectedRight);
        upArrowAction.setEnabled(!selectedRight);
        downArrowAction.setEnabled(!selectedRight);
        allDownArrowAction.setEnabled(!selectedRight);

    }

    /**
     * Sets the dialog controller.
     *
     * @param assignmentCompositeController the controller.
     */
    @Override
    public void setAssignmentCompositeController(AssignmentCompositeController<T> assignmentCompositeController) {
        this.compositeController = assignmentCompositeController;
    }

    /**
     * Sets the {@link AbstractTableModel} of the left table.
     *
     * @param assignmentTableModel the model.
     */
    @Override
    public void setTableModelLeft(final AssignmentTableModel<T> assignmentTableModel) {
        tableModelLeft = assignmentTableModel;
        rowSorterLeft = new TableRowSorter<>(tableModelLeft);
        updateEnablement();

        updateTableSortingLeft(assignmentTableModel);

        if (sortColumnIndex != null && rowSorterLeft != null) {
            rowSorterLeft.setSortKeys(Collections.singletonList(new RowSorter.SortKey(sortColumnIndex, sortColumnOrder)));
        }
    }

    /**
     * Sets the {@link AbstractTableModel} of the right table.
     *
     * @param assignmentTableModel the model.
     */
    @Override
    public void setTableModelRight(AssignmentTableModel<T> assignmentTableModel) {
        tableModelRight = assignmentTableModel;
        rowSorterRight = new TableRowSorter<>(tableModelRight);
        updateEnablement();

        updateTableSortingRight(assignmentTableModel);

        if (sortColumnIndex != null && rowSorterRight != null) {
            rowSorterRight.setSortKeys(Collections.singletonList(new RowSorter.SortKey(sortColumnIndex, sortColumnOrder)));
        }
    }
    
    /**
     * Advises both tables to refresh their contents.
     */
    @Override
    public void refreshTables() {
        tableModelLeft.fireTableDataChanged();
        tableModelRight.fireTableDataChanged();
        updateEnablement();
    }

    /**
     * @return the selected {@link IAssignable}s in the left table (not the unassigned).
     */
    @Override
    public List<IAssignable<T>> getSelectedValuesLeft() {
        int[] selectedRows = new JTable().getSelectedRows();
        return getSelectedItemsFromModel(selectedRows, tableModelLeft);
    }

    /**
     * @return the selected {@link IAssignable}s in the right table (not the assigned).
     */
    @Override
    public List<IAssignable<T>> getSelectedValuesRight() {
        int[] selectedRows = tableRowSelectionModelRight.getSelectedIndices();
        return getSelectedItemsFromModel(selectedRows, tableModelRight);
    }

    @Override
    public List<IAssignable<T>> getSelectedItemsFromModel(int[] selectedRows, AssignmentTableModel<T> tableModel) {
        if (selectedRows.length == 0) {
            return Collections.emptyList();
        }

        List<IAssignable<T>> assignables = tableModel.getAssignables();
        List<IAssignable<T>> selectedItems = new ArrayList<>();

        for (int rowInModel : selectedRows) {
            IAssignable<T> object = assignables.get(rowInModel);
            selectedItems.add(object);
        }
        return selectedItems;
    }

    /**
     * Sets the table selection in the right table to the given {@link IAssignable}s.
     *
     * @param selectedValues the values to select.
     */
    @Override
    public void selectValuesRight(List<IAssignable<T>> selectedValues) {
        List<IAssignable<T>> assignables = tableModelRight.getAssignables();

        for (IAssignable<T> value : selectedValues) {
            int rowIndex = assignables.indexOf(value);
            tableRowSelectionModelRight.setSelectionInterval(rowIndex, -1);
        }
    }

    @Override
    public void selectValuesLeft(List<IAssignable<T>> selectedValues) {
        List<IAssignable<T>> assignables = tableModelLeft.getAssignables();

        for (IAssignable<T> value : selectedValues) {
            int rowIndex = assignables.indexOf(value);
            tableRowSelectionModelRight.setSelectionInterval(rowIndex, -1);
        }
    }

    /**
     * Sets the title of the left table.
     *
     * @param tableLeftTitle the title of the left table.
     */
    @Override
    public void setTableTitleLeft(String tableLeftTitle) {
        titledBorderLeft.setTitle(tableLeftTitle);
    }

    /**
     * Sets the title of the right table.
     *
     * @param tableRightTitle the title of the right table.
     */
    @Override
    public void setTableTitleRight(String tableRightTitle) {
        titledBoarderRight.setTitle(tableRightTitle);
    }

    /**
     * @param text the text of the 'one left' button.
     */
    @Override
    public void setButtonTextToLeft(String text) {
        arrowActionLeft.putValue(Action.NAME, text);
    }

    /**
     * @param text the text of the 'one right' button.
     */
    @Override
    public void setButtonTextToRight(String text) {
        arrowActionRight.putValue(Action.NAME, text);
    }

    /**
     * @param text the text of the 'all to left' button.
     */
    @Override
    public void setButtonTextAllToLeft(String text) {
        arrowAllActionLeft.putValue(Action.NAME, text);
    }

    /**
     * @param text the text of the 'all to right' button.
     */
    @Override
    public void setButtonTextAllToRight(String text) {
        allArrowActionRight.putValue(Action.NAME, text);
    }

    /**
     * Sets if the left/right buttons should display the default icons.
     *
     * @param leftRightButtonIconsVisible if true the left/right buttons will display the default buttons. If false, alternative a text should
     *                                    be set for each button.
     * @see #setButtonTextToLeft(String)
     * @see #setButtonTextToRight(String)
     * @see #setButtonTextAllToLeft(String)
     * @see #setButtonTextAllToRight(String)
     */
    @Override
    public void setLeftRightButtonIconsVisible(boolean leftRightButtonIconsVisible) {
        if (leftRightButtonIconsVisible) {
            arrowAllActionLeft.putValue(Action.LARGE_ICON_KEY, getIcon(AssignmentCompositeIF.ALL_RIGHT));
            arrowActionLeft.putValue(Action.LARGE_ICON_KEY, getIcon(AssignmentCompositeIF.ONE_RIGHT));
            arrowActionRight.putValue(Action.LARGE_ICON_KEY, getIcon(AssignmentCompositeIF.ONE_LEFT));
            allArrowActionRight.putValue(Action.LARGE_ICON_KEY, getIcon(AssignmentCompositeIF.ALL_LEFT));
        } else {
            arrowActionLeft.putValue(Action.LARGE_ICON_KEY, null);
            arrowActionRight.putValue(Action.LARGE_ICON_KEY, null);
            arrowAllActionLeft.putValue(Action.LARGE_ICON_KEY, null);
            allArrowActionRight.putValue(Action.LARGE_ICON_KEY, null);
        }
    }

    /**
     * Configures over which table a filter should be shown.
     *
     * @param visibleFilter a {@link FilterVisible}.
     */
    @Override
    public void setVisibleFilter(FilterVisible visibleFilter) {
        // Refresh tables as setVisibleFilters could be called after the model is already set
        updateTableSortingLeft(tableModelLeft);
        updateTableSortingRight(tableModelRight);
    }

    /**
     * Sets the {@link RowFilter} for the left filter.
     *
     * @param rowFilter the {@link RowFilter}.
     */
    @Override
    public void setTableRowFilterLeft(RowFilter<AssignmentTableModel<T>, Object> rowFilter) {
        rowSorterLeft.setRowFilter(rowFilter);
    }

    /**
     * Sets the {@link RowFilter} for the right filter.
     *
     * @param rowFilter the {@link RowFilter}.
     */
    @Override
    public void setTableRowFilterRight(RowFilter<AssignmentTableModel<T>, Object> rowFilter) {
        rowSorterRight.setRowFilter(rowFilter);
    }

    /**
     * @param tableLeftSortable the tableLeftSortable to set
     */
    @Override
    public void setTableSortableLeft(boolean tableLeftSortable) {
        this.tableLeftSortable = tableLeftSortable;
        updateTableSortingLeft(tableModelLeft);
    }

    /**
     * @param tableRightSortable the tableRightSortable to set
     */
    @Override
    public void setTableSortableRight(boolean tableRightSortable) {
        this.tableRightSortable = tableRightSortable;
        updateTableSortingRight(tableModelRight);
    }

    /**
     * Sets the sorting for the column with the given <code>columnIndex</code>.
     *
     * @param columnIndex the column.
     * @param sortOrder   the {@link SortOrder}.
     */
    @Override
    public void setSorting(Integer columnIndex, SortOrder sortOrder) {
        this.sortColumnIndex = columnIndex;
        this.sortColumnOrder = sortOrder;

        if (rowSorterLeft != null) {
            rowSorterLeft.setSortKeys(Collections.singletonList(new RowSorter.SortKey(columnIndex, sortOrder)));
        }

        if (rowSorterRight != null) {
            rowSorterRight.setSortKeys(Collections.singletonList(new RowSorter.SortKey(columnIndex, sortOrder)));
        }
    }

    /**
     * @return only the visible rows (in regards to the filter) of the left table.
     */
    @Override
    public List<IAssignable<T>> getVisibleTableRowValuesLeft() {
        return getVisibleTableRowValues(tableModelLeft, rowSorterLeft);
    }

    /**
     * @return only the visible rows (in regards to the filter) of the right table.
     */
    @Override
    public List<IAssignable<T>> getVisibleTableRowValuesRight() {
        return getVisibleTableRowValues(tableModelRight, rowSorterRight);
    }

    @Override
    public List<IAssignable<T>> getVisibleTableRowValues(AssignmentTableModel<T> tableModel, TableRowSorter<?> rowSorter) {
        List<IAssignable<T>> assignables = tableModel.getAssignables();

        if (rowSorter == null) {
            // no sorter so there can't be a filter, return whole list
            return assignables;
        }

        RowFilter<?, ?> rowFilter = rowSorter.getRowFilter();
        if (rowFilter == null) {
            // list not filtered, return whole list
            return assignables;
        }

        int rowCount = tableModel.getRowCount();
        List<IAssignable<T>> visibleAssignables = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            IAssignable<T> iAssignable = assignables.get(i);
            visibleAssignables.add(iAssignable);
        }

        return visibleAssignables;
    }
}
