package com.googlecode.assignmentdialog.core;

/**
 * Simple implementation of {@link IAssignable} which takes any {@link Object} and calls {@link #toString()} on it.
 * 
 * @author Kai Winter
 */
public final class SimpleAssignable extends AbstractAssignable<Object> {

	/**
	 * Constructs a new {@link SimpleAssignable}.
	 * 
	 * @param object
	 *            the object.
	 */
	public SimpleAssignable(Object object) {
		super(object);
	}

	@Override
	public String getValueAt(int columnIndex) {
		return getAssignableObject().toString();
	}

}
