/*
 * DISTRIBUTION STATEMENT D. Further dissemination only as directed by (Program Manager,
 * PMS 406) (2022) or higher DoD authority.
 *
 * This software was developed by the Department of the Navy, NAVSEA Unmanned and Small
 * Combatants. It is provided under the terms of use found in the LICENSE file at the
 * source code root directory.
 */

package com.googlecode.assignmentdialog.ui.composite;

import com.googlecode.assignmentdialog.core.AssignmentCompositeModel;
import com.googlecode.assignmentdialog.core.IAssignable;

import javax.swing.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * Creates the composite and controls the assignment between the two tables of the dialog.
 *
 * @param <T> the concrete class used in the assignment dialog.
 * @author Kai Winter
 */
public class AssignmentCompositeController<T> {

    private AssignmentCompositeIF<T> assignmentComposite;

    private AssignmentCompositeModel<T> assignmentCompositeModel;

    private Map<Integer, String> currentFilterValuesLeft;
    private Map<Integer, String> currentFilterValuesRight;

    /**
     * Constructs a new {@link AssignmentCompositeController}.
     */
    public AssignmentCompositeController() {
        currentFilterValuesLeft = new HashMap<Integer, String>();
        currentFilterValuesRight = new HashMap<Integer, String>();
    }

    /**
     * Moves all entities from the right table to the left.
     */
    void moveAllLeft() {
        List<IAssignable<T>> valuesRightVisible = assignmentComposite.getVisibleTableRowValuesRight();
        if (valuesRightVisible.isEmpty()) {
            return;
        }

        assignmentCompositeModel.getTableRowValuesLeft().addAll(valuesRightVisible);
        assignmentCompositeModel.getTableRowValuesRight().removeAll(valuesRightVisible);

        assignmentComposite.refreshTables();
    }

    /**
     * Moves the selected entities from the right table to the left.
     */
    void moveLeft() {
        List<IAssignable<T>> selectedValuesRight = assignmentComposite.getSelectedValuesRight();
        if (selectedValuesRight.isEmpty()) {
            return;
        }

        List<IAssignable<T>> valuesLeft = assignmentCompositeModel.getTableRowValuesLeft();
        List<IAssignable<T>> valuesRight = assignmentCompositeModel.getTableRowValuesRight();

        valuesLeft.addAll(selectedValuesRight);
        valuesRight.removeAll(selectedValuesRight);

        assignmentComposite.refreshTables();
    }

    /**
     * Moves the selected entities from the left table to the right.
     */
    void moveRight() {
        List<IAssignable<T>> selectedValuesLeft = assignmentComposite.getSelectedValuesLeft();
        if (selectedValuesLeft.isEmpty()) {
            return;
        }

        List<IAssignable<T>> valuesLeft = assignmentCompositeModel.getTableRowValuesLeft();
        List<IAssignable<T>> valuesRight = assignmentCompositeModel.getTableRowValuesRight();

        valuesRight.addAll(selectedValuesLeft);
        valuesLeft.removeAll(selectedValuesLeft);
        assignmentComposite.refreshTables();
    }

    /**
     * Moves all entities from the left table to the right.
     */
    void moveAllRight() {
        List<IAssignable<T>> valuesLeftVisible = assignmentComposite.getVisibleTableRowValuesLeft();
        if (valuesLeftVisible.isEmpty()) {
            return;
        }

        assignmentCompositeModel.getTableRowValuesRight().addAll(valuesLeftVisible);
        assignmentCompositeModel.getTableRowValuesLeft().removeAll(valuesLeftVisible);

        assignmentComposite.refreshTables();
    }

    /**
     * Moves the selected items in the right table to the top.
     */
    void moveToTop() {
        List<IAssignable<T>> selectedValuesRight = assignmentComposite.getSelectedValuesRight();
        if (selectedValuesRight.isEmpty()) {
            return;
        }

        List<IAssignable<T>> valuesRight = assignmentCompositeModel.getTableRowValuesRight();
        valuesRight.removeAll(selectedValuesRight);
        List<IAssignable<T>> tmpList = new ArrayList<>(valuesRight);
        valuesRight.clear();
        valuesRight.addAll(selectedValuesRight);
        valuesRight.addAll(tmpList);

        assignmentComposite.refreshTables();
    }

    /**
     * Moves the selected items in the right table one position up.
     */
    void moveUp() {
        List<IAssignable<T>> selectedValuesRight = assignmentComposite.getSelectedValuesRight();
        if (selectedValuesRight.isEmpty()) {
            return;
        }

        List<IAssignable<T>> valuesRight = assignmentCompositeModel.getTableRowValuesRight();

        for (IAssignable<T> selectedValue : selectedValuesRight) {
            int oldIndex = valuesRight.indexOf(selectedValue);
            if (oldIndex == 0) { // already on top
                continue;
            }

            valuesRight.remove(selectedValue);
            valuesRight.add(oldIndex - 1, selectedValue);
        }
        assignmentComposite.refreshTables();
        assignmentComposite.selectValuesRight(selectedValuesRight);
    }

    /**
     * Moves the selected items in the right table one position down.
     */
    void moveDown() {
        List<IAssignable<T>> selectedValuesRight = assignmentComposite.getSelectedValuesRight();
        if (selectedValuesRight.isEmpty()) {
            return;
        }

        List<IAssignable<T>> valuesRight = assignmentCompositeModel.getTableRowValuesRight();

        Collections.reverse(selectedValuesRight);
        for (IAssignable<T> selectedValue : selectedValuesRight) {
            int oldIndex = valuesRight.indexOf(selectedValue);
            if (oldIndex == valuesRight.size() - 1) { // already at the end
                continue;
            }
            valuesRight.remove(selectedValue);
            valuesRight.add(oldIndex + 1, selectedValue);
        }
        assignmentComposite.refreshTables();
        assignmentComposite.selectValuesRight(selectedValuesRight);
    }

    /**
     * Moves the selected items in the right table to the bottom.
     */
    void moveToBottom() {
        List<IAssignable<T>> valuesRight = assignmentCompositeModel.getTableRowValuesRight();
        List<IAssignable<T>> selectedValuesRight = assignmentComposite.getSelectedValuesRight();
        if (selectedValuesRight.isEmpty()) {
            return;
        }

        valuesRight.removeAll(selectedValuesRight);
        valuesRight.addAll(selectedValuesRight);
        assignmentComposite.refreshTables();
    }

    /**
     * @param assignmentCompositeModel the ssignmentCompositeModel to set
     */
    public void setAssignmentCompositeModel(AssignmentCompositeModel<T> assignmentCompositeModel) {
        this.assignmentCompositeModel = assignmentCompositeModel;

        // Initialize tables
        assert assignmentComposite != null : "assignmentComposite not initialized!";
        if (assignmentCompositeModel == null) {
            throw new IllegalStateException("No AssignmentCompositeMode set!");
        }
        List<String> tableColumNames = assignmentCompositeModel.getTableColumNames();

        List<IAssignable<T>> tableRowValuesLeft = assignmentCompositeModel.getTableRowValuesLeft();
        AssignmentTableModel<T> assignmentTableModelLeft = getAssignmentTableModelLeft(tableColumNames, tableRowValuesLeft);
        assignmentComposite.setTableModelLeft(assignmentTableModelLeft);

        List<IAssignable<T>> tableRowValuesRight = assignmentCompositeModel.getTableRowValuesRight();
        AssignmentTableModel<T> assignmentTableModelRight = getAssignmentTableModelRight(tableColumNames, tableRowValuesRight);
        assignmentComposite.setTableModelRight(assignmentTableModelRight);
    }

    protected AssignmentTableModel<T> getAssignmentTableModelLeft(List<String> tableColumNames, List<IAssignable<T>> tableRowValuesLeft) {
        return new AssignmentTableModel<>(tableColumNames, tableRowValuesLeft);
    }

    protected AssignmentTableModel<T> getAssignmentTableModelRight(List<String> tableColumNames, List<IAssignable<T>> tableRowValuesRight) {
        return new AssignmentTableModel<>(tableColumNames, tableRowValuesRight);
    }

    /**
     * The list of elements which are unassigned (left table entries).
     *
     * @return the elements on the left side of the assignment.
     */
    public List<IAssignable<T>> getUnassignedElements() {
        return assignmentCompositeModel.getTableRowValuesLeft();
    }

    /**
     * The list of elements which are assigned to the right side.
     *
     * @return the elements on the right side of the assignment.
     */
    public List<IAssignable<T>> getAssignedElements() {
        return assignmentCompositeModel.getTableRowValuesRight();
    }

    /**
     * Sets the <code>value</code> of the column with the given <code>columnIndex</code>.
     *
     * @param value       the new value.
     * @param columnIndex the index of the column.
     */
    void setLeftFilterValueAt(String value, int columnIndex) {
        currentFilterValuesLeft.put(columnIndex, value);
    }

    /**
     * Returns the current value of the column with the given <code>columnIndex</code>.
     *
     * @param columnIndex the index of the column.
     * @return the current value.
     */
    String getLeftFilterValueAt(int columnIndex) {
        return currentFilterValuesLeft.get(columnIndex);
    }

    /**
     * Sets the <code>value</code> of the column with the given <code>columnIndex</code>.
     *
     * @param value       the new value.
     * @param columnIndex the index of the column.
     */
    void setRightFilterValueAt(String value, int columnIndex) {
        currentFilterValuesRight.put(columnIndex, value);
    }

    /**
     * Returns the current value of the column with the given <code>columnIndex</code>.
     *
     * @param columnIndex the index of the column.
     * @return the current value.
     */
    String getRightFilterValueAt(int columnIndex) {
        return currentFilterValuesRight.get(columnIndex);
    }

    /**
     * Called after a value of the left filters were changed.
     */
    void filterLeftChanged() {
        assignmentComposite.setTableRowFilterLeft(buildRowFilter(currentFilterValuesLeft.entrySet()));
    }

    /**
     * Gets the filter values and creates a {@link RowFilter}.
     */
    private RowFilter<AssignmentTableModel<T>, Object> buildRowFilter(Set<Entry<Integer, String>> entrySet) {
        Collection<RowFilter<AssignmentTableModel<T>, Object>> filters = new ArrayList<>();
        for (Entry<Integer, String> columnFilterValue : entrySet) {
            String value = columnFilterValue.getValue();
            if (value == null || value.isEmpty()) {
                continue;
            }

            Integer column = columnFilterValue.getKey();
            // case-insensitive regex
            filters.add(RowFilter.regexFilter("(?i)" + value, column));
        }

        return RowFilter.andFilter(filters);
    }

    /**
     * Called after a value of the right filters were changed.
     */
    void filterRightChanged() {
        assignmentComposite.setTableRowFilterRight(buildRowFilter(currentFilterValuesRight.entrySet()));
    }

    /**
     * @return the assignmentComposite
     */
    public AssignmentCompositeIF<T> getAssignmentComposite() {
        return assignmentComposite;
    }

    /**
     * @param assignmentComposite the assignmentComposite to set
     */
    public void setAssignmentComposite(AssignmentCompositeIF<T> assignmentComposite) {
        this.assignmentComposite = assignmentComposite;
    }
}
