package com.googlecode.assignmentdialog.util;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.assignmentdialog.core.IAssignable;

public final class AssignableConverter {

	private AssignableConverter() {
		// utility class, should not be instantiated
	}

	/**
	 * Un-Wraps a {@link List}&lt;{@link IAssignable}&lt;T&gt;&gt; to a {@link List}&lt;T&gt;.
	 * 
	 * @param assignables
	 *            {@link List} of {@link IAssignable}s.
	 * @return {@link List} of &lt;T&gt;.
	 */
	public static <T> List<T> convertAssignables2List(List<IAssignable<T>> assignables) {

		List<T> tList = new ArrayList<T>();

		for (IAssignable<T> assignable : assignables) {
			T assignableObject = assignable.getAssignableObject();
			tList.add(assignableObject);
		}

		return tList;
	}

	/**
	 * Wraps a {@link List}&lt;T&gt; into a {@link List}&lt;{@link IAssignable}&lt;T&gt;&gt; of the given
	 * <code>assignableClazz</code>.
	 * 
	 * @param tList
	 *            the list of objects to wrap.
	 * @param assignableClazz
	 *            the wrapping class which implements {@link IAssignable}.
	 * @return {@link List} of {@link IAssignable}&lt;T&gt;.
	 */
	public static <T> List<IAssignable<T>> convertList2Assignables(List<T> tList,
			Class<? extends IAssignable<T>> assignableClazz) {

		List<IAssignable<T>> assignableList = new ArrayList<IAssignable<T>>();

		for (T t : tList) {
			try {
				Constructor<? extends IAssignable<T>> declaredConstructor = assignableClazz.getDeclaredConstructor(t
						.getClass());
				IAssignable<T> assignable = declaredConstructor.newInstance(t);
				assignableList.add(assignable);

			} catch (Exception e) {
				// Reflection sucks.
				throw new IllegalArgumentException(e);
			}

		}

		return assignableList;
	}
}
