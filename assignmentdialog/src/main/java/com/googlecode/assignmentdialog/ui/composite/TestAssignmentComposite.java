/*
 * DISTRIBUTION STATEMENT D. Further dissemination only as directed by (Program Manager,
 * PMS 406) (2022) or higher DoD authority.
 *
 * This software was developed by the Department of the Navy, NAVSEA Unmanned and Small
 * Combatants. It is provided under the terms of use found in the LICENSE file at the
 * source code root directory.
 */

package com.googlecode.assignmentdialog.ui.composite;

import com.googlecode.assignmentdialog.core.AbstractAssignable;
import com.googlecode.assignmentdialog.core.IAssignable;
import com.googlecode.assignmentdialog.ui.composite.filter.FilterVisible;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


public class TestAssignmentComposite<T> implements AssignmentCompositeIF<T> {

    private final ListSelectionModel tableRowSelectionModelLeft = new DefaultListSelectionModel();
    private final ListSelectionModel tableRowSelectionModelRight = new DefaultListSelectionModel();
    private final TitledBorder titledBorderLeft = new TitledBorder(null, "Available",
            TitledBorder.LEADING, TitledBorder.TOP, null, null);
    private final TitledBorder titledBoarderRight = new TitledBorder(null, "Selected",
            TitledBorder.LEADING, TitledBorder.TOP, null, null);
    private final EnumMap<ButtonAction, TestButtonAction> buttonActionToActionMap;

    private final List<ButtonAction> buttonActionCommands = new LinkedList<>();

    private AssignmentCompositeController<T> compositeController;
    private AssignmentTableModel<T> tableModelLeft;
    private AssignmentTableModel<T> tableModelRight;
    private TableRowSorter<AssignmentTableModel<T>> rowSorterLeft;
    private TableRowSorter<AssignmentTableModel<T>> rowSorterRight;
    private boolean tableLeftSortable;
    private boolean tableRightSortable;
    private Integer sortColumnIndex = null;
    private SortOrder sortColumnOrder;

    {
        buttonActionToActionMap = EnumSet.allOf(ButtonAction.class).stream()
                .map(buttonAction -> new AbstractMap.SimpleEntry<>(buttonAction, new TestButtonAction(buttonAction)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a, () -> new EnumMap<>(ButtonAction.class)));

        initListListener();

        setLeftRightButtonIconsVisible(true);

        EnumSet.allOf(ButtonAction.class).forEach(buttonAction -> buttonActionToActionMap.get(buttonAction).engagePropertyChangeListening());

    }

    public void click(ButtonAction buttonAction) {
        buttonActionToActionMap.get(buttonAction).click();
    }

    public Map<String, Object> getPropertyChanges(ButtonAction buttonAction) {
        return buttonActionToActionMap.get(buttonAction).getPropertyChanges();
    }

    public TestButtonAction getAction(ButtonAction buttonAction) {
        return buttonActionToActionMap.get(buttonAction);
    }

    @Override
    public void initListListener() {
        ListSelectionListener tableSelectionListener = e -> updateEnablement();
        addLeftTableRowSelectionListener(tableSelectionListener);
        addRightTableRowSelectionListener(tableSelectionListener);
    }

    public void addLeftTableRowSelectionListener(ListSelectionListener leftTableRowSelectionListener) {
        tableRowSelectionModelLeft.addListSelectionListener(leftTableRowSelectionListener);
    }

    public void addRightTableRowSelectionListener(ListSelectionListener rightTableRowSelectionListener) {
        tableRowSelectionModelRight.addListSelectionListener(rightTableRowSelectionListener);
    }

    private static @Nullable ImageIcon getIcon(String oneDown) {
        URL resource = AssignmentComposite.class.getResource(oneDown);
        return resource != null ? new ImageIcon(resource) : null;
    }

    @Override
    public void updateEnablement() {

        buttonActionToActionMap.get(ButtonAction.RIGHT_ARROW).setEnabled(!getSelectedValuesLeft().isEmpty());
        buttonActionToActionMap.get(ButtonAction.LEFT_ARROW).setEnabled(!getSelectedValuesRight().isEmpty());

        Action leftAllArrowAction = buttonActionToActionMap.get(ButtonAction.LEFT_ALL_ARROW);
        if (tableModelLeft != null) {
            List<IAssignable<T>> assignablesLeft = tableModelLeft.getAssignables();
            leftAllArrowAction.setEnabled(!assignablesLeft.isEmpty());
        } else {
            leftAllArrowAction.setEnabled(false);
        }

        Action rightAllArrowAction = buttonActionToActionMap.get(ButtonAction.RIGHT_ALL_ARROW);
        if (tableModelRight != null) {
            List<IAssignable<T>> assignablesRight = tableModelRight.getAssignables();
            rightAllArrowAction.setEnabled(!assignablesRight.isEmpty());
        } else {
            rightAllArrowAction.setEnabled(false);
        }

        // reorder buttons
        boolean selectedRight = getSelectedValuesRight().isEmpty();
        buttonActionToActionMap.get(ButtonAction.UP_ALL_ARROW).setEnabled(!selectedRight);
        buttonActionToActionMap.get(ButtonAction.UP_ARROW).setEnabled(!selectedRight);
        buttonActionToActionMap.get(ButtonAction.DOWN_ARROW).setEnabled(!selectedRight);
        buttonActionToActionMap.get(ButtonAction.DOWN_ALL_ARROW).setEnabled(!selectedRight);
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
        if (tableModelLeft.getRowCount() == 0) {
            tableRowSelectionModelLeft.clearSelection();
        }
        tableModelRight.fireTableDataChanged();
        if (tableModelRight.getRowCount() == 0) {
            tableRowSelectionModelRight.clearSelection();
        }
        updateEnablement();
    }

    @Override
    public void refreshTables(AbstractAssignable<T> reselectData) {
        tableModelLeft.fireTableDataChanged();
        tableModelRight.fireTableDataChanged();
        if (reselectData != null) {
            List<IAssignable<T>> selectedValues = List.of(reselectData);
            selectValuesRight(selectedValues);
            selectValuesLeft(selectedValues);
        }
        updateEnablement();
    }

    /**
     * @return the selected {@link IAssignable}s in the left table (not the unassigned).
     */
    @Override
    public List<IAssignable<T>> getSelectedValuesLeft() {
        int[] selectedRows = tableRowSelectionModelLeft.getSelectedIndices();
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
            tableRowSelectionModelRight.setSelectionInterval(rowIndex, rowIndex);
        }
    }

    @Override
    public void selectValuesLeft(List<IAssignable<T>> selectedValues) {
        List<IAssignable<T>> assignables = tableModelLeft.getAssignables();

        for (IAssignable<T> value : selectedValues) {
            int rowIndex = assignables.indexOf(value);
            tableRowSelectionModelLeft.setSelectionInterval(rowIndex, rowIndex);
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
        buttonActionToActionMap.get(ButtonAction.LEFT_ARROW).putValue(Action.NAME, text);
    }

    /**
     * @param text the text of the 'one right' button.
     */
    @Override
    public void setButtonTextToRight(String text) {
        buttonActionToActionMap.get(ButtonAction.RIGHT_ARROW).putValue(Action.NAME, text);
    }

    /**
     * @param text the text of the 'all to left' button.
     */
    @Override
    public void setButtonTextAllToLeft(String text) {
        buttonActionToActionMap.get(ButtonAction.LEFT_ALL_ARROW).putValue(Action.NAME, text);
    }

    /**
     * @param text the text of the 'all to right' button.
     */
    @Override
    public void setButtonTextAllToRight(String text) {
        buttonActionToActionMap.get(ButtonAction.RIGHT_ALL_ARROW).putValue(Action.NAME, text);
    }

    /**
     * Sets if the left/right buttons should display the default icons.
     *
     * @param leftRightButtonIconsVisible if true the left/right buttons will display the default buttons. If false,
     *                                    alternative a text should
     *                                    be set for each button.
     * @see #setButtonTextToLeft(String)
     * @see #setButtonTextToRight(String)
     * @see #setButtonTextAllToLeft(String)
     * @see #setButtonTextAllToRight(String)
     */
    @Override
    public void setLeftRightButtonIconsVisible(boolean leftRightButtonIconsVisible) {
        Action leftAllArrowAction = buttonActionToActionMap.get(ButtonAction.LEFT_ALL_ARROW);
        Action leftArrowAction = buttonActionToActionMap.get(ButtonAction.LEFT_ARROW);
        Action rightAllArrowAction = buttonActionToActionMap.get(ButtonAction.RIGHT_ALL_ARROW);
        Action rightArrowAction = buttonActionToActionMap.get(ButtonAction.RIGHT_ARROW);
        if (leftRightButtonIconsVisible) {
            leftAllArrowAction.putValue(Action.LARGE_ICON_KEY, getIcon(AssignmentCompositeIF.ALL_LEFT));
            leftArrowAction.putValue(Action.LARGE_ICON_KEY, getIcon(AssignmentCompositeIF.ONE_RIGHT));
            rightAllArrowAction.putValue(Action.LARGE_ICON_KEY, getIcon(AssignmentCompositeIF.ALL_RIGHT));
            rightArrowAction.putValue(Action.LARGE_ICON_KEY, getIcon(AssignmentCompositeIF.ONE_LEFT));
        } else {
            leftAllArrowAction.putValue(Action.LARGE_ICON_KEY, null);
            leftArrowAction.putValue(Action.LARGE_ICON_KEY, null);
            rightAllArrowAction.putValue(Action.LARGE_ICON_KEY, null);
            rightArrowAction.putValue(Action.LARGE_ICON_KEY, null);
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
     * @return only the visible rows (regarding the filter) of the left table.
     */
    @Override
    public List<IAssignable<T>> getVisibleTableRowValuesLeft() {
        return getVisibleTableRowValues(tableModelLeft, rowSorterLeft);
    }

    /**
     * @return only the visible rows (regarding the filter) of the right table.
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


    public void clear() {
        tableRowSelectionModelLeft.clearSelection();
        tableRowSelectionModelRight.clearSelection();
        buttonActionToActionMap.values().forEach(TestButtonAction::clear);
    }

    public List<ButtonAction> getActionCommands() {
        return buttonActionCommands;
    }

    public enum ButtonAction {
        DOWN_ARROW,
        DOWN_ALL_ARROW,
        LEFT_ARROW,
        LEFT_ALL_ARROW,
        RIGHT_ARROW,
        RIGHT_ALL_ARROW,
        UP_ARROW,
        UP_ALL_ARROW,

        UNKNOWN;

        @Override
        public String toString() {
            return snakeToCapitalizedWords(name());
        }

        public static String snakeToCapitalizedWords(String snake) {
            return snakeToCapitalizedWords(null, null, snake);
        }

        public static String snakeToCapitalizedWords(@Nullable String prefix, @Nullable String suffix,
                                                     String snake) {
            if (snake.isBlank()) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            if (prefix != null && !prefix.isBlank()) {
                prefix = prefix.trim();
                sb.append(prefix).append(" ");
            }
            String[] words = snake.split("_");
            String separator = "";
            for (String word : words) {
                sb.append(separator);
                sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase());
                separator = " ";
            }
            if (suffix != null && !suffix.isBlank()) {
                suffix = suffix.trim();
                sb.append(" ").append(suffix);
            }
            return sb.toString();
        }

        public static ButtonAction find(String name) {
            for (ButtonAction buttonAction : EnumSet.allOf(ButtonAction.class)) {
                if (buttonAction.name().equals(name)) {
                    return buttonAction;
                }
            }
            return UNKNOWN;
        }
    }

    public class TestButtonAction extends AbstractAction {
        public static final String BUTTON_ACTION = "button-action";
        private final Map<String, Object> propertyChanges = new LinkedHashMap<>();

        public TestButtonAction(ButtonAction buttonAction) {
            putValue(NAME, buttonAction.toString());
            putValue(ACTION_COMMAND_KEY, buttonAction.name());
            putValue(BUTTON_ACTION, buttonAction);
            setEnabled(false);
        }

        public void engagePropertyChangeListening() {
            addPropertyChangeListener(evt -> propertyChanges.put(evt.getPropertyName(), evt.getNewValue()));
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            Action action = (Action) ae.getSource();
            ButtonAction buttonAction = (ButtonAction) action.getValue(BUTTON_ACTION);
            buttonActionCommands.add(buttonAction);
            switch (buttonAction) {
                case RIGHT_ARROW:
                    compositeController.moveRight();
                    break;
                default:
            }
        }

        public void click() {
            actionPerformed(new ActionEvent(this, 0, (String) getValue(ACTION_COMMAND_KEY)));
        }

        public Map<String, Object> getPropertyChanges() {
            return Map.copyOf(propertyChanges);
        }

        public void clear() {
            propertyChanges.clear();
        }
    }
}
