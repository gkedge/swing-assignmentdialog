package com.googlecode.assignmentdialog.core;

/**
 * Abstract implementation of {@link IAssignable} to do common things like setting and getting the actual object.
 * 
 * @author Kai Winter
 * 
 * @param <T>
 *            The concrete object.
 */
public abstract class AbstractAssignable<T> implements IAssignable<T> {

	private final T assignableObject;

	/**
	 * Creates a {@link AbstractAssignable} and assigns the object of this {@link IAssignable}.
	 * 
	 * @param assignableObject
	 *            the actual object of a table row.
	 */
	public AbstractAssignable(T assignableObject) {
		this.assignableObject = assignableObject;
	}

	@Override
	public T getAssignableObject() {
		return assignableObject;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assignableObject == null) ? 0 : assignableObject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked")
		AbstractAssignable<T> other = (AbstractAssignable<T>) obj;
		if (assignableObject == null) {
			if (other.assignableObject != null) {
				return false;
			}
		} else if (!assignableObject.equals(other.assignableObject)) {
			return false;
		}
		return true;
	}

}
