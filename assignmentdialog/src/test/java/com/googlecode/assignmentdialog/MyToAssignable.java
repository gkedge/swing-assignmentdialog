package com.googlecode.assignmentdialog;

import com.googlecode.assignmentdialog.core.AbstractAssignable;
import com.googlecode.assignmentdialog.core.IAssignable;

/**
 * {@link IAssignable} wrapper for {@link MyTo}.
 */
public final class MyToAssignable extends AbstractAssignable<MyTo> {

	/**
	 * Constructs a new {@link MyToAssignable}.
	 * 
	 * @param object
	 *            the actual object.
	 */
	public MyToAssignable(MyTo object) {
		super(object);
	}

	@Override
	public String getValueAt(int columnIndex) {
		MyTo valueObject = getAssignableObject();
		if (valueObject == null) {
			return null;
		}

		if (columnIndex == 0) {
			return valueObject.getColumn1();
		} else if (columnIndex == 1) {
			return valueObject.getColumn2();
		}

		return null;
	}

}
