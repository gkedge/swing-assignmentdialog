package com.googlecode.assignmentdialog.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.assignmentdialog.MyTo;
import com.googlecode.assignmentdialog.MyToAssignable;
import com.googlecode.assignmentdialog.core.IAssignable;

/**
 * Tests for the {@link AssignableConverter}.
 */
public final class AssignableConverterTest {

	/**
	 * Wraps and unwraps a List, checks if the un-wraped list is equal to the original list.
	 */
	@Test
	public void testObject2Assignable() {
		List<MyTo> originalList = new ArrayList<MyTo>();
		originalList.add(new MyTo("Firstname 1", "Lastname 5"));
		originalList.add(new MyTo("Firstname 2", "Lastname 4"));
		originalList.add(new MyTo("Firstname 3", "Lastname 3"));
		originalList.add(new MyTo("Firstname 4", "Lastname 2"));
		originalList.add(new MyTo("Firstname 5", "Lastname 1"));

		List<IAssignable<MyTo>> assignableList = AssignableConverter.convertList2Assignables(originalList,
				MyToAssignable.class);

		List<MyTo> unwrappedList = AssignableConverter.convertAssignables2List(assignableList);

		Assert.assertEquals(originalList, unwrappedList);
	}
}
