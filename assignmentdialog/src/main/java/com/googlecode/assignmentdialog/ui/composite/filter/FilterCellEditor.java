package com.googlecode.assignmentdialog.ui.composite.filter;

import static java.awt.event.KeyEvent.VK_ACCEPT;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_END;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_HOME;
import static java.awt.event.KeyEvent.VK_TAB;
import static java.awt.event.KeyEvent.VK_UP;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * {@link DefaultCellEditor} for the filter row which ignores several keys to work like a simple text field.
 * 
 * @author Kai Winter
 */
@SuppressWarnings("serial")
public class FilterCellEditor extends DefaultCellEditor {

	// Ignore events which traverse the focus. The editors should behave like normal text fields.
	private static final List<Integer> IGNORE_KEYS = Arrays.asList(VK_UP, VK_DOWN, VK_TAB, VK_ESCAPE, VK_ACCEPT,
			VK_ENTER, VK_ENTER);

	private static final List<Integer> IGNORE_KEYS_WITH_CTRL = Arrays.asList(VK_HOME, VK_END);

	/**
	 * Constructs a new {@link FilterCellEditor}.
	 * 
	 * @param textField
	 *            the {@link JTextField} to use as editor.
	 * @param filterTable
	 *            the table to which this {@link FilterCellEditor} applies.
	 */
	public FilterCellEditor(final JTextField textField, final JTable filterTable) {
		super(textField);

		setClickCountToStart(1);

		textField.setBorder(BorderFactory.createEmptyBorder());

		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				ignoreKeys(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				ignoreKeys(e);

				int editingColumn = filterTable.getEditingColumn();
				filterTable.getModel().setValueAt(textField.getText(), 0, editingColumn);
			}

			private void ignoreKeys(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (IGNORE_KEYS.contains(keyCode)) {
					e.consume();
				} else if (e.isControlDown() && IGNORE_KEYS_WITH_CTRL.contains(keyCode)) {
					e.consume();
				}
			}
		});
	}

}
