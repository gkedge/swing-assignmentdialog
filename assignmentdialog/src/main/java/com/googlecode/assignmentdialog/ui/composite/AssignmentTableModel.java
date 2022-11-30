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

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * {@link AbstractTableModel} which evaluates the {@link AssignmentCompositeModel} to render the columns and their
 * values properly.
 *
 * @param <T> the concrete class used in the assignment dialog.
 * @author Kai Winter
 */
@SuppressWarnings("serial")
public final class AssignmentTableModel<T> extends AbstractTableModel {

    // The names of the columns.
    private final List<String> tableColumNames;

    // The actual values which are shown in the table.
    private final List<IAssignable<T>> assignables;

    /**
     * Constructs a new {@link AbstractTableModel}.
     *
     * @param tableColumNames {@link List} of column names
     * @param assignables     {@link List} of {@link IAssignable}s which will be shown in the table.
     */
    public AssignmentTableModel(List<String> tableColumNames, List<IAssignable<T>> assignables) {
        this.tableColumNames = tableColumNames;
        this.assignables = assignables;
    }

    @Override
    public int getRowCount() {
        return assignables.size();
    }

    @Override
    public int getColumnCount() {
        return tableColumNames.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        IAssignable<T> assignable = assignables.get(rowIndex);
        return assignable.getValueAt(columnIndex);
    }

    @Override
    public String getColumnName(int columnNumber) {
        assert tableColumNames.size() > columnNumber;

        return tableColumNames.get(columnNumber);
    }

    /**
     * @return the assignables
     */
    public List<IAssignable<T>> getAssignables() {
        return assignables;
    }

    public void setAssignables(List<IAssignable<T>> assignables) {
        this.assignables.clear();
        this.assignables.addAll(assignables);
    }

}
