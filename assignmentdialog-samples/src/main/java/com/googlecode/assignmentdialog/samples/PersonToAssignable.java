package com.googlecode.assignmentdialog.samples;

import com.googlecode.assignmentdialog.core.AbstractAssignable;
import com.googlecode.assignmentdialog.core.IAssignable;

/**
 * An implementation of {@link IAssignable} wraps a object so it can be used in the AssignmentComposite.
 */
public final class PersonToAssignable extends AbstractAssignable<PersonTo> {

	public PersonToAssignable(PersonTo personTo) {
		super(personTo);
	}

	@Override
	public String getValueAt(int columnIndex) {
		PersonTo personTo = getAssignableObject();
		if (columnIndex == 0) {
			return personTo.getFirstname();
		} else if (columnIndex == 1) {
			return personTo.getLastname();
		}
		return null;
	}
}
