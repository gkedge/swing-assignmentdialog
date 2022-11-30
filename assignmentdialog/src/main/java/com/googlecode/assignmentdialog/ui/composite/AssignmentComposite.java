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
import com.googlecode.assignmentdialog.ui.composite.filter.FilterCellEditor;
import com.googlecode.assignmentdialog.ui.composite.filter.FilterTableModel;
import com.googlecode.assignmentdialog.ui.composite.filter.FilterVisible;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * The Composite which contains the assignment controls.
 *
 * <p>
 * To use this Component initialize an {@link AssignmentComposite} and one {@link AssignmentCompositeController} and
 * link them by {@link AssignmentComposite#setAssignmentCompositeController(AssignmentCompositeController)} and
 * {@link AssignmentCompositeController#setAssignmentComposite(AssignmentComposite)}. Then set the
 * {@link AssignmentCompositeModel} by
 * {@link AssignmentCompositeController#setAssignmentCompositeModel(AssignmentCompositeModel)}.
 * </p>
 *
 * @param <T> the concrete class used in the assignment dialog.
 * @author Kai Winter
 */
@SuppressWarnings("serial")
public class AssignmentComposite<T> extends JComponent implements AssignmentCompositeIF<T> {

    private static final DefaultTableCellRenderer BORDERLESS_CELL_RENDERER = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused,
                                                       int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            setBorder(BorderFactory.createLineBorder(getBackground(), 1));
            return this;
        }
    };
    private final JPanel contentPanel = new JPanel();
    protected JTable tableLeft;
    protected JTable tableRight;
    private AssignmentCompositeController<T> compositeController;
    private JButton buttonRight;
    private JButton buttonAllRight;
    private JButton buttonLeft;
    private JButton buttonAllLeft;
    private TitledBorder titledBorderLeft;
    private TitledBorder titledBoarderRight;
    private JButton buttonAllUp;
    private JButton buttonUp;
    private JButton buttonDown;
    private JButton buttonAllDown;
    private JPanel panelButtonsBottomLeft;
    private JPanel panelButtonsMiddle;
    private JPanel panelButtonsBottomRight;
    private LeftRightButtonsPosition leftRightButtonsPosition;
    private boolean enableReorder;
    private JPanel panelLeftFilter;
    private JTable tableLeftFilter;
    private JPanel panelLeft;
    private JPanel panelRightFilter;
    private JTable tableRightFilter;
    private FilterVisible visibleFilter;
    private JPanel panelRight;
    private boolean tableLeftSortable;
    private boolean tableRightSortable;
    private Integer sortColumnIndex = null;
    private SortOrder sortColumnOrder;

    /**
     * Create the dialog.
     */
    public AssignmentComposite() {
        setBounds(100, 100, 647, 376);
        setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(contentPanel, BorderLayout.CENTER);
        GridBagLayout gbl_contentPanel = new GridBagLayout();
        gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0};
        gbl_contentPanel.rowHeights = new int[]{0, 0};
        gbl_contentPanel.columnWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_contentPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        contentPanel.setLayout(gbl_contentPanel);
        {
            panelLeft = new JPanel();
            titledBorderLeft = new TitledBorder(null, "Available", TitledBorder.LEADING, TitledBorder.TOP, null, null);
            panelLeft.setBorder(titledBorderLeft);
            GridBagConstraints gbc_panelLeft = new GridBagConstraints();
            gbc_panelLeft.fill = GridBagConstraints.BOTH;
            gbc_panelLeft.gridx = 0;
            gbc_panelLeft.gridy = 0;
            contentPanel.add(panelLeft, gbc_panelLeft);
            GridBagLayout gbl_panelLeft = new GridBagLayout();
            gbl_panelLeft.rowHeights = new int[]{0, 0, 0, 0};
            gbl_panelLeft.columnWeights = new double[]{1.0};
            gbl_panelLeft.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
            panelLeft.setLayout(gbl_panelLeft);
            {
                panelLeftFilter = new JPanel();
                GridBagConstraints gbc_panelLeftFilter = new GridBagConstraints();
                gbc_panelLeftFilter.insets = new Insets(0, 0, 5, 0);
                gbc_panelLeftFilter.fill = GridBagConstraints.BOTH;
                gbc_panelLeftFilter.gridx = 0;
                gbc_panelLeftFilter.gridy = 0;
                panelLeft.add(panelLeftFilter, gbc_panelLeftFilter);
                GridBagLayout gbl_panelLeftFilter = new GridBagLayout();
                gbl_panelLeftFilter.columnWidths = new int[]{0, 0};
                gbl_panelLeftFilter.rowHeights = new int[]{0, 0};
                gbl_panelLeftFilter.columnWeights = new double[]{1.0, Double.MIN_VALUE};
                gbl_panelLeftFilter.rowWeights = new double[]{0.0, Double.MIN_VALUE};
                panelLeftFilter.setLayout(gbl_panelLeftFilter);
                {
                    tableLeftFilter = new JTable();
                    tableLeftFilter.setDefaultRenderer(String.class, BORDERLESS_CELL_RENDERER);
                    tableLeftFilter.setShowHorizontalLines(false);
                    tableLeftFilter.setRowSelectionAllowed(false);
                    GridBagConstraints gbc_tableLeftFilter = new GridBagConstraints();
                    gbc_tableLeftFilter.fill = GridBagConstraints.BOTH;
                    gbc_tableLeftFilter.gridx = 0;
                    gbc_tableLeftFilter.gridy = 0;
                    panelLeftFilter.add(tableLeftFilter, gbc_tableLeftFilter);
                }
            }
            {
                JScrollPane scrollPane = new JScrollPane();
                GridBagConstraints gbc_scrollPane = new GridBagConstraints();
                gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
                gbc_scrollPane.fill = GridBagConstraints.BOTH;
                gbc_scrollPane.gridx = 0;
                gbc_scrollPane.gridy = 1;
                panelLeft.add(scrollPane, gbc_scrollPane);
                {
                    tableLeft = getTableLeft();
                    tableLeft.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                                compositeController.moveRight();
                            }
                        }
                    });
                    tableLeft.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    scrollPane.setViewportView(tableLeft);
                }
            }
            {
                panelButtonsBottomLeft = new JPanel();
                panelButtonsBottomLeft.setVisible(false);
                GridBagConstraints gbc_panelButtonsBottomLeft = new GridBagConstraints();
                gbc_panelButtonsBottomLeft.fill = GridBagConstraints.BOTH;
                gbc_panelButtonsBottomLeft.gridx = 0;
                gbc_panelButtonsBottomLeft.gridy = 2;
                panelLeft.add(panelButtonsBottomLeft, gbc_panelButtonsBottomLeft);
            }
        }
        {
            panelButtonsMiddle = new JPanel();
            panelButtonsMiddle.setBorder(new EmptyBorder(15, 0, 10, 0));
            GridBagConstraints gbc_panelButtonsMiddle = new GridBagConstraints();
            gbc_panelButtonsMiddle.insets = new Insets(0, 0, 0, 5);
            gbc_panelButtonsMiddle.fill = GridBagConstraints.BOTH;
            gbc_panelButtonsMiddle.gridx = 1;
            gbc_panelButtonsMiddle.gridy = 0;
            contentPanel.add(panelButtonsMiddle, gbc_panelButtonsMiddle);
            GridBagLayout gbl_panelButtonsMiddle = new GridBagLayout();
            gbl_panelButtonsMiddle.columnWidths = new int[]{33, 0};
            gbl_panelButtonsMiddle.rowHeights = new int[]{25, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            gbl_panelButtonsMiddle.columnWeights = new double[]{0.0, Double.MIN_VALUE};
            gbl_panelButtonsMiddle.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0,
                    Double.MIN_VALUE};
            panelButtonsMiddle.setLayout(gbl_panelButtonsMiddle);
            {
                buttonAllLeft = new JButton("");
                buttonAllLeft.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        compositeController.moveAllLeft();
                    }
                });
                GridBagConstraints gbc_buttonAllLeft = new GridBagConstraints();
                gbc_buttonAllLeft.insets = new Insets(0, 0, 5, 0);
                gbc_buttonAllLeft.gridx = 0;
                gbc_buttonAllLeft.gridy = 0;
                panelButtonsMiddle.add(buttonAllLeft, gbc_buttonAllLeft);
            }
            {
                buttonLeft = new JButton("");
                buttonLeft.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        compositeController.moveLeft();
                    }
                });
                GridBagConstraints gbc_buttonLeft = new GridBagConstraints();
                gbc_buttonLeft.insets = new Insets(0, 0, 5, 0);
                gbc_buttonLeft.gridx = 0;
                gbc_buttonLeft.gridy = 1;
                panelButtonsMiddle.add(buttonLeft, gbc_buttonLeft);
            }
            {
                buttonRight = new JButton("");
                buttonRight.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        compositeController.moveRight();
                    }
                });
                GridBagConstraints gbc_buttonRight = new GridBagConstraints();
                gbc_buttonRight.insets = new Insets(0, 0, 5, 0);
                gbc_buttonRight.gridx = 0;
                gbc_buttonRight.gridy = 2;
                panelButtonsMiddle.add(buttonRight, gbc_buttonRight);
            }
            {
                buttonAllRight = new JButton("");
                buttonAllRight.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        compositeController.moveAllRight();
                    }
                });
                GridBagConstraints gbc_buttonAllRight = new GridBagConstraints();
                gbc_buttonAllRight.insets = new Insets(0, 0, 5, 0);
                gbc_buttonAllRight.gridx = 0;
                gbc_buttonAllRight.gridy = 3;
                panelButtonsMiddle.add(buttonAllRight, gbc_buttonAllRight);
            }
            {
                buttonAllUp = new JButton("");
                buttonAllUp.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        compositeController.moveToTop();
                    }
                });
                buttonAllUp.setIcon(getIcon(AssignmentCompositeIF.ALLWAY_UP));
                GridBagConstraints gbc_buttonAllUp = new GridBagConstraints();
                gbc_buttonAllUp.insets = new Insets(0, 0, 5, 0);
                gbc_buttonAllUp.gridx = 0;
                gbc_buttonAllUp.gridy = 5;
                panelButtonsMiddle.add(buttonAllUp, gbc_buttonAllUp);
            }
            {
                buttonUp = new JButton("");
                buttonUp.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        compositeController.moveUp();
                        updateEnablement();
                    }
                });
                buttonUp.setIcon(getIcon(AssignmentCompositeIF.ONE_UP));
                GridBagConstraints gbc_buttonUp = new GridBagConstraints();
                gbc_buttonUp.insets = new Insets(0, 0, 5, 0);
                gbc_buttonUp.gridx = 0;
                gbc_buttonUp.gridy = 6;
                panelButtonsMiddle.add(buttonUp, gbc_buttonUp);
            }
            {
                buttonDown = new JButton("");
                buttonDown.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        compositeController.moveDown();
                        updateEnablement();
                    }
                });
                buttonDown.setIcon(getIcon(AssignmentCompositeIF.ONE_DOWN));
                GridBagConstraints gbc_buttonDown = new GridBagConstraints();
                gbc_buttonDown.insets = new Insets(0, 0, 5, 0);
                gbc_buttonDown.gridx = 0;
                gbc_buttonDown.gridy = 7;
                panelButtonsMiddle.add(buttonDown, gbc_buttonDown);
            }
            {
                buttonAllDown = new JButton("");
                buttonAllDown.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        compositeController.moveToBottom();
                    }
                });
                buttonAllDown.setIcon(getIcon(AssignmentCompositeIF.ALLWAY_DOWN));
                GridBagConstraints gbc_buttonAllDown = new GridBagConstraints();
                gbc_buttonAllDown.gridx = 0;
                gbc_buttonAllDown.gridy = 8;
                panelButtonsMiddle.add(buttonAllDown, gbc_buttonAllDown);
            }
        }
        {
            panelRight = new JPanel();
            titledBoarderRight = new TitledBorder(null, "Selected", TitledBorder.LEADING, TitledBorder.TOP, null, null);
            panelRight.setBorder(titledBoarderRight);
            GridBagConstraints gbc_panelRight = new GridBagConstraints();
            gbc_panelRight.fill = GridBagConstraints.BOTH;
            gbc_panelRight.gridx = 2;
            gbc_panelRight.gridy = 0;
            contentPanel.add(panelRight, gbc_panelRight);
            GridBagLayout gbl_panelRight = new GridBagLayout();
            gbl_panelRight.rowHeights = new int[]{0, 0, 0, 0};
            gbl_panelRight.columnWeights = new double[]{1.0};
            gbl_panelRight.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
            panelRight.setLayout(gbl_panelRight);
            {
                panelRightFilter = new JPanel();
                GridBagConstraints gbc_panelRightFilter = new GridBagConstraints();
                gbc_panelRightFilter.insets = new Insets(0, 0, 5, 0);
                gbc_panelRightFilter.fill = GridBagConstraints.BOTH;
                gbc_panelRightFilter.gridx = 0;
                gbc_panelRightFilter.gridy = 0;
                panelRight.add(panelRightFilter, gbc_panelRightFilter);
                GridBagLayout gbl_panelRightFilter = new GridBagLayout();
                gbl_panelRightFilter.columnWidths = new int[]{0, 0};
                gbl_panelRightFilter.rowHeights = new int[]{0, 0};
                gbl_panelRightFilter.columnWeights = new double[]{1.0, Double.MIN_VALUE};
                gbl_panelRightFilter.rowWeights = new double[]{1.0, Double.MIN_VALUE};
                panelRightFilter.setLayout(gbl_panelRightFilter);
                {
                    tableRightFilter = new JTable();
                    tableRightFilter.setDefaultRenderer(String.class, BORDERLESS_CELL_RENDERER);
                    tableRightFilter.setShowHorizontalLines(false);
                    tableRightFilter.setRowSelectionAllowed(false);
                    GridBagConstraints gbc_tableRightFilter = new GridBagConstraints();
                    gbc_tableRightFilter.fill = GridBagConstraints.BOTH;
                    gbc_tableRightFilter.gridx = 0;
                    gbc_tableRightFilter.gridy = 0;
                    panelRightFilter.add(tableRightFilter, gbc_tableRightFilter);
                }
            }
            {
                JScrollPane scrollPane = new JScrollPane();
                GridBagConstraints gbc_scrollPane = new GridBagConstraints();
                gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
                gbc_scrollPane.fill = GridBagConstraints.BOTH;
                gbc_scrollPane.gridx = 0;
                gbc_scrollPane.gridy = 1;
                panelRight.add(scrollPane, gbc_scrollPane);
                {
                    tableRight = getTableRight();
                    tableRight.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                                compositeController.moveLeft();
                            }
                        }
                    });
                    tableRight.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    scrollPane.setViewportView(tableRight);
                }
            }
            {
                panelButtonsBottomRight = new JPanel();
                panelButtonsBottomRight.setVisible(false);
                GridBagConstraints gbc_panelButtonsBottomRight = new GridBagConstraints();
                gbc_panelButtonsBottomRight.fill = GridBagConstraints.BOTH;
                gbc_panelButtonsBottomRight.gridx = 0;
                gbc_panelButtonsBottomRight.gridy = 2;
                panelRight.add(panelButtonsBottomRight, gbc_panelButtonsBottomRight);
            }
        }

        tableLeft.getTableHeader().setReorderingAllowed(false);
        tableRight.getTableHeader().setReorderingAllowed(false);

        initListListener();

        leftRightButtonsPosition = LeftRightButtonsPosition.MIDDLE;
        setLeftRightButtonIconsVisible(true);
    }

    private static @Nullable ImageIcon getIcon(String resourceName) {
        URL resource = AssignmentComposite.class.getResource(resourceName);
        return resource != null ? new ImageIcon(resource) : null;
    }

    @Override
    public JTable getTableLeft() {
        return new JTable();
    }

    @Override
    public JTable getTableRight() {
        return new JTable();
    }

    @Override
    public void initListListener() {
        ListSelectionListener tableSelectionListener = e -> updateEnablement();
        tableLeft.getColumnModel().getSelectionModel().addListSelectionListener(tableSelectionListener);
        tableRight.getColumnModel().getSelectionModel().addListSelectionListener(tableSelectionListener);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void updateEnablement() {

        buttonRight.setEnabled(!getSelectedValuesLeft().isEmpty());
        buttonLeft.setEnabled(!getSelectedValuesRight().isEmpty());

        TableModel modelLeft = tableLeft.getModel();
        if (modelLeft instanceof AssignmentTableModel) {
            List<IAssignable<T>> assignablesLeft = ((AssignmentTableModel<T>) modelLeft).getAssignables();
            buttonAllRight.setEnabled(!assignablesLeft.isEmpty());
        } else {
            buttonAllRight.setEnabled(false);
        }

        TableModel modelRight = tableRight.getModel();
        if (modelRight instanceof AssignmentTableModel) {
            List<IAssignable<T>> assignablesRight = ((AssignmentTableModel<T>) modelRight).getAssignables();
            buttonAllLeft.setEnabled(!assignablesRight.isEmpty());
        } else {
            buttonAllLeft.setEnabled(false);
        }

        // reorder buttons
        boolean selectedRight = getSelectedValuesRight().isEmpty();
        buttonAllUp.setEnabled(!selectedRight);
        buttonUp.setEnabled(!selectedRight);
        buttonDown.setEnabled(!selectedRight);
        buttonAllDown.setEnabled(!selectedRight);
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
        tableLeft.setModel(assignmentTableModel);
        updateEnablement();

        updateTableSortingLeft(assignmentTableModel);

        RowSorter<? extends TableModel> rowSorter = tableLeft.getRowSorter();
        if (sortColumnIndex != null && rowSorter != null) {
            rowSorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(sortColumnIndex, sortColumnOrder)));
        }
    }

    @Override
    public void updateTableSortingLeft(TableModel assignmentTableModel) {
        if (FilterVisible.LEFT.equals(visibleFilter) || FilterVisible.BOTH.equals(visibleFilter)) {
            initLeftFilter(assignmentTableModel);

            if (tableLeftSortable) {
                tableLeft.setAutoCreateRowSorter(true);
            } else {
                TableRowSorter<TableModel> tableRowSorter = new TableRowSorter<TableModel>(tableLeft.getModel()) {
                    @Override
                    public void toggleSortOrder(int column) {
                        // don't sort!
                    }
                };
                tableLeft.setRowSorter(tableRowSorter);
            }
        } else if (tableLeftSortable) {
            tableLeft.setAutoCreateRowSorter(true);
        }
    }

    /**
     * Sets the {@link AbstractTableModel} of the right table.
     *
     * @param assignmentTableModel the model.
     */
    @Override
    public void setTableModelRight(AssignmentTableModel<T> assignmentTableModel) {
        tableRight.setModel(assignmentTableModel);
        updateEnablement();

        updateTableSortingRight(assignmentTableModel);

        RowSorter<? extends TableModel> rowSorter = tableRight.getRowSorter();
        if (sortColumnIndex != null && rowSorter != null) {
            rowSorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(sortColumnIndex, sortColumnOrder)));
        }
    }

    @Override
    public void updateTableSortingRight(TableModel assignmentTableModel) {
        if (FilterVisible.RIGHT.equals(visibleFilter) || FilterVisible.BOTH.equals(visibleFilter)) {
            initRightFilter(assignmentTableModel);

            if (tableRightSortable) {
                tableRight.setAutoCreateRowSorter(true);
            } else {
                TableRowSorter<TableModel> tableRowSorter = new TableRowSorter<TableModel>(tableRight.getModel()) {
                    @Override
                    public void toggleSortOrder(int column) {
                        // don't sort!
                    }
                };

                tableRight.setRowSorter(tableRowSorter);
            }
        } else if (tableRightSortable) {
            tableRight.setAutoCreateRowSorter(true);
        }
    }

    @Override
    public void initLeftFilter(final TableModel assignmentTableModel) {
        TableModel leftFilterModel = new FilterTableModel() {

            @Override
            public int getColumnCount() {
                return assignmentTableModel.getColumnCount();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return compositeController.getLeftFilterValueAt(columnIndex);
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                compositeController.setLeftFilterValueAt((String) aValue, columnIndex);
                compositeController.filterLeftChanged();
            }
        };
        createFilter(tableLeft, tableLeftFilter, leftFilterModel);
    }

    @Override
    public void initRightFilter(final TableModel assignmentTableModel) {
        FilterTableModel rightFilterModel = new FilterTableModel() {

            @Override
            public int getColumnCount() {
                return assignmentTableModel.getColumnCount();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return compositeController.getRightFilterValueAt(columnIndex);
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                compositeController.setRightFilterValueAt((String) aValue, columnIndex);
                compositeController.filterRightChanged();
            }
        };

        createFilter(tableRight, tableRightFilter, rightFilterModel);
    }

    @Override
    public void createFilter(JTable dataTable, final JTable filterTable, TableModel filterTableModel) {
        filterTable.setModel(filterTableModel);
        filterTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        filterTable.setRowHeight(filterTable.getRowHeight() + 3);
        filterTable.setCellSelectionEnabled(false);

        // Editor which reacts on a single click and has no border
        FilterCellEditor filterCellEditor = new FilterCellEditor(new JTextField(), filterTable);
        filterTable.setDefaultEditor(String.class, filterCellEditor);

        // Change column width of the filter table when the data tables column widths changes
        Enumeration<TableColumn> columns = dataTable.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = columns.nextElement();
            column.addPropertyChangeListener(evt -> {
                if ("width".equals(evt.getPropertyName())) {
                    TableColumn tableColumn = ((TableColumn) evt.getSource());
                    int modelIndex = tableColumn.getModelIndex();
                    int newWidth = (Integer) evt.getNewValue();

                    TableColumnModel filterColumnModel = filterTable.getColumnModel();
                    TableColumn filterColumn = filterColumnModel.getColumn(modelIndex);
                    filterColumn.setPreferredWidth(newWidth);
                }
            });
        }
    }

    /**
     * Advises both tables to refresh their contents.
     */
    @Override
    public void refreshTables() {
        ((AbstractTableModel) tableLeft.getModel()).fireTableDataChanged();
        ((AbstractTableModel) tableRight.getModel()).fireTableDataChanged();
        updateEnablement();
    }

    /**
     * @return the selected {@link IAssignable}s in the left table (not the unassigned).
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<IAssignable<T>> getSelectedValuesLeft() {
        int[] selectedRows = tableLeft.getSelectedRows();
        TableModel tableModel = tableLeft.getModel();
        if (!(tableModel instanceof AssignmentTableModel)) {
            return List.of();
        }
        return getSelectedItemsFromModel(selectedRows, (AssignmentTableModel<T>) tableModel);
    }

    /**
     * @return the selected {@link IAssignable}s in the right table (not the assigned).
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<IAssignable<T>> getSelectedValuesRight() {
        int[] selectedRows = tableRight.getSelectedRows();
        TableModel tableModel = tableRight.getModel();
        if (!(tableModel instanceof AssignmentTableModel)) {
            return List.of();
        }
        return getSelectedItemsFromModel(selectedRows, (AssignmentTableModel<T>) tableModel);
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
        @SuppressWarnings("unchecked")
        List<IAssignable<T>> assignables = ((AssignmentTableModel<T>) tableRight.getModel()).getAssignables();

        for (IAssignable<T> value : selectedValues) {
            int indexOf = assignables.indexOf(value);
            tableRight.changeSelection(indexOf, -1, true, false);
        }
    }

    @Override
    public void selectValuesLeft(List<IAssignable<T>> selectedValues) {
        @SuppressWarnings("unchecked")
        List<IAssignable<T>> assignables = ((AssignmentTableModel<T>) tableLeft.getModel()).getAssignables();

        for (IAssignable<T> value : selectedValues) {
            int indexOf = assignables.indexOf(value);
            tableLeft.changeSelection(indexOf, -1, true, false);
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
     * @param enableReorder if set to true the buttons for reordering the elements in the right table are shown.
     */
    @Override
    public void setReorderEnabled(boolean enableReorder) {
        this.enableReorder = enableReorder;
        panelButtonsMiddle
                .setVisible(enableReorder || LeftRightButtonsPosition.MIDDLE.equals(leftRightButtonsPosition));
        buttonAllUp.setVisible(enableReorder);
        buttonUp.setVisible(enableReorder);
        buttonDown.setVisible(enableReorder);
        buttonAllDown.setVisible(enableReorder);
    }

    /**
     * Sets the position of the move left and move right buttons. This method is intended to be used on initialization
     * time. Later switching of the button position results in undefined state.
     *
     * @param position the position of the left/right buttons.
     */
    @Override
    public void setLeftRightButtonsPosition(LeftRightButtonsPosition position) {
        this.leftRightButtonsPosition = position;
        if (LeftRightButtonsPosition.BOTTOM.equals(position)) {
            panelButtonsMiddle.setVisible(this.enableReorder);
            panelButtonsBottomLeft.setVisible(true);
            panelButtonsBottomRight.setVisible(true);

            buttonToBottomLeft(buttonAllRight);
            buttonToBottomLeft(buttonRight);

            buttonToBottomRight(buttonLeft);
            buttonToBottomRight(buttonAllLeft);
        } else {
            panelButtonsBottomLeft.setVisible(false);
            panelButtonsBottomRight.setVisible(false);
        }
    }

    @Override
    public void buttonToBottomLeft(JButton button) {
        panelButtonsMiddle.remove(button);
        panelButtonsBottomLeft.add(button);
    }

    @Override
    public void buttonToBottomRight(JButton button) {
        panelButtonsMiddle.remove(button);
        panelButtonsBottomRight.add(button);
    }

    /**
     * Sets which of the left/right buttons should be visible.
     *
     * @param visibleButtons a {@link LeftRightButtonsVisible} to define which buttons should be visible.
     */
    @Override
    public void setLeftRightButtonsVisible(LeftRightButtonsVisible visibleButtons) {
        if (LeftRightButtonsVisible.ONE_LEFT_ONE_RIGHT_ONLY.equals(visibleButtons)) {
            buttonAllLeft.setVisible(false);
            buttonAllRight.setVisible(false);
        }
    }

    /**
     * @param text the text of the 'one left' button.
     */
    @Override
    public void setButtonTextToLeft(String text) {
        buttonLeft.setText(text);
    }

    /**
     * @param text the text of the 'one right' button.
     */
    @Override
    public void setButtonTextToRight(String text) {
        buttonRight.setText(text);
    }

    /**
     * @param text the text of the 'all to left' button.
     */
    @Override
    public void setButtonTextAllToLeft(String text) {
        buttonAllLeft.setText(text);
    }

    /**
     * @param text the text of the 'all to right' button.
     */
    @Override
    public void setButtonTextAllToRight(String text) {
        buttonAllRight.setText(text);
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
        if (leftRightButtonIconsVisible) {
            buttonAllLeft.setIcon(getIcon(AssignmentCompositeIF.ALL_RIGHT));
            buttonLeft.setIcon(getIcon(AssignmentCompositeIF.ONE_RIGHT));
            buttonRight.setIcon(getIcon(AssignmentCompositeIF.ONE_LEFT));
            buttonAllRight.setIcon(getIcon(AssignmentCompositeIF.ALL_LEFT));
        } else {
            buttonLeft.setIcon(null);
            buttonRight.setIcon(null);
            buttonAllLeft.setIcon(null);
            buttonAllRight.setIcon(null);
        }
    }

    /**
     * Configures over which table a filter should be shown.
     *
     * @param visibleFilter a {@link FilterVisible}.
     */
    @Override
    public void setVisibleFilter(FilterVisible visibleFilter) {
        this.visibleFilter = visibleFilter;
        if (!(FilterVisible.LEFT.equals(visibleFilter) || FilterVisible.BOTH.equals(visibleFilter))) {
            panelLeft.remove(panelLeftFilter);
        }
        if (!(FilterVisible.RIGHT.equals(visibleFilter) || FilterVisible.BOTH.equals(visibleFilter))) {
            panelRight.remove(panelRightFilter);
        }

        // Refresh tables as setVisibleFilters could be called after the model is already set
        updateTableSortingLeft(tableLeft.getModel());
        updateTableSortingRight(tableRight.getModel());
    }

    /**
     * Sets the {@link RowFilter} for the left filter.
     *
     * @param rowFilter the {@link RowFilter}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void setTableRowFilterLeft(RowFilter<AssignmentTableModel<T>, Object> rowFilter) {
        TableRowSorter<AssignmentTableModel<T>> rowSorter = (TableRowSorter<AssignmentTableModel<T>>) tableLeft
                .getRowSorter();
        rowSorter.setRowFilter(rowFilter);
    }

    /**
     * Sets the {@link RowFilter} for the right filter.
     *
     * @param rowFilter the {@link RowFilter}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void setTableRowFilterRight(RowFilter<AssignmentTableModel<T>, Object> rowFilter) {
        TableRowSorter<AssignmentTableModel<T>> rowSorter = (TableRowSorter<AssignmentTableModel<T>>) tableRight
                .getRowSorter();
        rowSorter.setRowFilter(rowFilter);
    }

    /**
     * @param tableLeftSortable the tableLeftSortable to set
     */
    @Override
    public void setTableSortableLeft(boolean tableLeftSortable) {
        this.tableLeftSortable = tableLeftSortable;
        updateTableSortingLeft(tableLeft.getModel());
    }

    /**
     * @param tableRightSortable the tableRightSortable to set
     */
    @Override
    public void setTableSortableRight(boolean tableRightSortable) {
        this.tableRightSortable = tableRightSortable;
        updateTableSortingRight(tableRight.getModel());
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

        RowSorter<? extends TableModel> rowSorterLeft = tableLeft.getRowSorter();
        if (rowSorterLeft != null) {
            rowSorterLeft.setSortKeys(Collections.singletonList(new RowSorter.SortKey(columnIndex, sortOrder)));
        }

        RowSorter<? extends TableModel> rowSorterRight = tableRight.getRowSorter();
        if (rowSorterRight != null) {
            rowSorterRight.setSortKeys(Collections.singletonList(new RowSorter.SortKey(columnIndex, sortOrder)));
        }
    }

    /**
     * @return only the visible rows (in regards to the filter) of the left table.
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<IAssignable<T>> getVisibleTableRowValuesLeft() {
        return getVisibleTableRowValues((AssignmentTableModel<T>) tableLeft.getModel(),
                ((TableRowSorter<?>) tableLeft.getRowSorter()));
    }

    /**
     * @return only the visible rows (in regards to the filter) of the right table.
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<IAssignable<T>> getVisibleTableRowValuesRight() {
        return getVisibleTableRowValues((AssignmentTableModel<T>) tableRight.getModel(),
                ((TableRowSorter<?>) tableRight.getRowSorter()));
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
