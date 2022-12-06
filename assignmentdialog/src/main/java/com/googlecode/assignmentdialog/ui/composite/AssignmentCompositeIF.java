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

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;

public interface AssignmentCompositeIF<T> {
    // icons for left right buttons
    String ALL_LEFT = "2rightarrow.png";
    String ONE_LEFT = "1rightarrow.png";
    String ONE_RIGHT = "1leftarrow.png";
    String ALL_RIGHT = "2leftarrow.png";
    // icons to up down buttons
    String ALLWAY_DOWN = "2downarrow.png";
    String ONE_DOWN = "1downarrow.png";
    String ONE_UP = "1uparrow.png";
    String ALLWAY_UP = "2uparrow.png";

    default JTable getTableLeft() {
        return null;
    }

    default JTable getTableRight() {
        return null;
    }

    void initListListener();

    void updateEnablement();

    void setAssignmentCompositeController(AssignmentCompositeController<T> assignmentCompositeController);

    void setTableModelLeft(AssignmentTableModel<T> assignmentTableModel);

    default void updateTableSortingLeft(TableModel assignmentTableModel) {

    }

    void setTableModelRight(AssignmentTableModel<T> assignmentTableModel);

    default void updateTableSortingRight(TableModel assignmentTableModel) {

    }

    default void initLeftFilter(TableModel assignmentTableModel) {

    }

    default void initRightFilter(TableModel assignmentTableModel) {

    }

    default void createFilter(JTable dataTable, JTable filterTable, TableModel filterTableModel) {

    }

    void refreshTables();

    void refreshTables(AbstractAssignable<T> reselectData);

    List<IAssignable<T>> getSelectedValuesLeft();

    List<IAssignable<T>> getSelectedValuesRight();

    List<IAssignable<T>> getSelectedItemsFromModel(int[] selectedRows, AssignmentTableModel<T> tableModel);

    void selectValuesRight(List<IAssignable<T>> selectedValues);

    void selectValuesLeft(List<IAssignable<T>> selectedValues);

    default void setTableTitleLeft(String tableLeftTitle) {

    }

    default void setTableTitleRight(String tableRightTitle) {

    }

    default void setReorderEnabled(boolean enableReorder) {

    }

    default void setLeftRightButtonsPosition(LeftRightButtonsPosition position) {

    }

    default void buttonToBottomLeft(JButton button) {

    }

    default void buttonToBottomRight(JButton button) {

    }

    default void setLeftRightButtonsVisible(LeftRightButtonsVisible visibleButtons) {

    }

    void setButtonTextToLeft(String text);

    void setButtonTextToRight(String text);

    void setButtonTextAllToLeft(String text);

    void setButtonTextAllToRight(String text);

    void setLeftRightButtonIconsVisible(boolean leftRightButtonIconsVisible);

    void setVisibleFilter(FilterVisible visibleFilter);

    void setTableRowFilterLeft(RowFilter<AssignmentTableModel<T>, Object> rowFilter);

    void setTableRowFilterRight(RowFilter<AssignmentTableModel<T>, Object> rowFilter);

    void setTableSortableLeft(boolean tableLeftSortable);

    void setTableSortableRight(boolean tableRightSortable);

    void setSorting(Integer columnIndex, SortOrder sortOrder);

    List<IAssignable<T>> getVisibleTableRowValuesLeft();

    List<IAssignable<T>> getVisibleTableRowValuesRight();

    List<IAssignable<T>> getVisibleTableRowValues(AssignmentTableModel<T> tableModel, TableRowSorter<?> rowSorter);
}
