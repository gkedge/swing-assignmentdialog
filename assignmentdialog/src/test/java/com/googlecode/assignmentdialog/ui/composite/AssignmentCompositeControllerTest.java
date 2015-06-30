package com.googlecode.assignmentdialog.ui.composite;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.googlecode.assignmentdialog.MyTo;
import com.googlecode.assignmentdialog.MyToAssignable;
import com.googlecode.assignmentdialog.core.AssignmentCompositeModel;
import com.googlecode.assignmentdialog.core.IAssignable;

/**
 * Tests for the {@link AssignmentCompositeController}.
 */
public final class AssignmentCompositeControllerTest {

	private static final MyToAssignable LEFT_ASSIGNABLE_1 = new MyToAssignable(new MyTo("row 1-col 1", "row 1-col 2"));
	private static final MyToAssignable LEFT_ASSIGNABLE_2 = new MyToAssignable(new MyTo("row 2-col 1", "row 2-col 2"));

	private static final MyToAssignable RIGHT_ASSIGNABLE_1 = new MyToAssignable(new MyTo("row 1-col 1-r",
			"row 1-col 2-r"));
	private static final MyToAssignable RIGHT_ASSIGNABLE_2 = new MyToAssignable(new MyTo("row 2-col 1-r",
			"row 2-col 2-r"));

	private static final List<IAssignable<MyTo>> TABLE_ROW_VALUES_LEFT = (List) Arrays.asList(new MyToAssignable[] {
			LEFT_ASSIGNABLE_1, LEFT_ASSIGNABLE_2 });
	private static final List<IAssignable<MyTo>> TABLE_ROW_VALUES_RIGHT = (List) Arrays.asList(new MyToAssignable[] {
			RIGHT_ASSIGNABLE_1, RIGHT_ASSIGNABLE_2 });

	/**
	 * Tests assignment of all items from the right table to the left.
	 */
	@Test
	public void testMoveAllLeft() {
		AssignmentCompositeModel<MyTo> model = new AssignmentCompositeModel<MyTo>(Collections.<String> emptyList(),
				Collections.<IAssignable<MyTo>> emptyList(), TABLE_ROW_VALUES_RIGHT);

		AssignmentCompositeController<MyTo> controller = new AssignmentCompositeController<MyTo>();
		controller.setAssignmentComposite(new AssignmentComposite<MyTo>());
		controller.setAssignmentCompositeModel(model);

		List<IAssignable<MyTo>> tableRowValuesRight = model.getTableRowValuesRight();
		Assert.assertEquals(2, tableRowValuesRight.size());

		List<IAssignable<MyTo>> tableRowValuesLeft = model.getTableRowValuesLeft();
		Assert.assertEquals(0, tableRowValuesLeft.size());

		controller.moveAllLeft();

		tableRowValuesRight = model.getTableRowValuesRight();
		Assert.assertEquals(0, tableRowValuesRight.size());

		tableRowValuesLeft = model.getTableRowValuesLeft();
		Assert.assertEquals(2, tableRowValuesLeft.size());
	}

	/**
	 * Tests assignment of all items from the left table to the right.
	 */
	@Test
	public void testMoveAllRight() {
		AssignmentCompositeModel<MyTo> model = new AssignmentCompositeModel<MyTo>(Collections.<String> emptyList(),
				TABLE_ROW_VALUES_LEFT);

		AssignmentCompositeController<MyTo> controller = new AssignmentCompositeController<MyTo>();
		controller.setAssignmentComposite(new AssignmentComposite<MyTo>());
		controller.setAssignmentCompositeModel(model);

		List<IAssignable<MyTo>> tableRowValuesLeft = model.getTableRowValuesLeft();
		Assert.assertEquals(2, tableRowValuesLeft.size());

		List<IAssignable<MyTo>> tableRowValuesRight = model.getTableRowValuesRight();
		Assert.assertEquals(0, tableRowValuesRight.size());

		controller.moveAllRight();

		tableRowValuesLeft = model.getTableRowValuesLeft();
		Assert.assertEquals(0, tableRowValuesLeft.size());

		tableRowValuesRight = model.getTableRowValuesRight();
		Assert.assertEquals(2, tableRowValuesRight.size());
	}

	/**
	 * Tests the assignment of one selected item from the left to the right table.
	 */
	@Test
	public void testMoveOneRight() {
		AssignmentCompositeModel<MyTo> model = new AssignmentCompositeModel<MyTo>(Collections.<String> emptyList(),
				TABLE_ROW_VALUES_LEFT);

		AssignmentCompositeController<MyTo> controller = new AssignmentCompositeController<MyTo>();

		AssignmentComposite<MyTo> composite = new AssignmentComposite<MyTo>() {
			@Override
			public List<IAssignable<MyTo>> getSelectedValuesLeft() {
				return Collections.<IAssignable<MyTo>> singletonList(LEFT_ASSIGNABLE_1);
			}
		};
		controller.setAssignmentComposite(composite);
		controller.setAssignmentCompositeModel(model);

		List<IAssignable<MyTo>> tableRowValuesLeft = model.getTableRowValuesLeft();
		Assert.assertEquals(2, tableRowValuesLeft.size());

		List<IAssignable<MyTo>> tableRowValuesRight = model.getTableRowValuesRight();
		Assert.assertEquals(0, tableRowValuesRight.size());

		controller.moveRight();

		tableRowValuesLeft = model.getTableRowValuesLeft();
		Assert.assertEquals(1, tableRowValuesLeft.size());

		tableRowValuesRight = model.getTableRowValuesRight();
		Assert.assertEquals(1, tableRowValuesRight.size());
	}

	/**
	 * Tests the assignment of two selected item from the left to the right table.
	 */
	@Test
	public void testMoveTwoRight() {
		AssignmentCompositeModel<MyTo> model = new AssignmentCompositeModel<MyTo>(Collections.<String> emptyList(),
				TABLE_ROW_VALUES_LEFT);

		AssignmentCompositeController<MyTo> controller = new AssignmentCompositeController<MyTo>();

		AssignmentComposite<MyTo> composite = new AssignmentComposite<MyTo>() {
			@Override
			public List<IAssignable<MyTo>> getSelectedValuesLeft() {
				return TABLE_ROW_VALUES_LEFT;
			}
		};
		controller.setAssignmentComposite(composite);
		controller.setAssignmentCompositeModel(model);

		List<IAssignable<MyTo>> tableRowValuesLeft = model.getTableRowValuesLeft();
		Assert.assertEquals(2, tableRowValuesLeft.size());

		List<IAssignable<MyTo>> tableRowValuesRight = model.getTableRowValuesRight();
		Assert.assertEquals(0, tableRowValuesRight.size());

		controller.moveRight();

		tableRowValuesLeft = model.getTableRowValuesLeft();
		Assert.assertEquals(0, tableRowValuesLeft.size());

		tableRowValuesRight = model.getTableRowValuesRight();
		Assert.assertEquals(2, tableRowValuesRight.size());
	}

	/**
	 * Tests the assignment of one selected item from the right to the left table.
	 */
	@Test
	public void testMoveOneLeft() {
		AssignmentCompositeModel<MyTo> model = new AssignmentCompositeModel<MyTo>(Collections.<String> emptyList(),
				Collections.<IAssignable<MyTo>> emptyList(), TABLE_ROW_VALUES_RIGHT);

		AssignmentCompositeController<MyTo> controller = new AssignmentCompositeController<MyTo>();

		AssignmentComposite<MyTo> composite = new AssignmentComposite<MyTo>() {
			@Override
			public List<IAssignable<MyTo>> getSelectedValuesRight() {
				return Collections.<IAssignable<MyTo>> singletonList(RIGHT_ASSIGNABLE_1);
			}
		};
		controller.setAssignmentComposite(composite);
		controller.setAssignmentCompositeModel(model);

		List<IAssignable<MyTo>> tableRowValuesRight = model.getTableRowValuesRight();
		Assert.assertEquals(2, tableRowValuesRight.size());

		List<IAssignable<MyTo>> tableRowValuesLeft = model.getTableRowValuesLeft();
		Assert.assertEquals(0, tableRowValuesLeft.size());

		controller.moveLeft();

		tableRowValuesRight = model.getTableRowValuesRight();
		Assert.assertEquals(1, tableRowValuesRight.size());

		tableRowValuesLeft = model.getTableRowValuesLeft();
		Assert.assertEquals(1, tableRowValuesLeft.size());
	}

	/**
	 * Tests the assignment of two selected item from the right to the left table.
	 */
	@Test
	public void testMoveTwoLeft() {
		AssignmentCompositeModel<MyTo> model = new AssignmentCompositeModel<MyTo>(Collections.<String> emptyList(),
				Collections.<IAssignable<MyTo>> emptyList(), TABLE_ROW_VALUES_RIGHT);

		AssignmentCompositeController<MyTo> controller = new AssignmentCompositeController<MyTo>();

		AssignmentComposite<MyTo> composite = new AssignmentComposite<MyTo>() {
			@Override
			public List<IAssignable<MyTo>> getSelectedValuesRight() {
				return TABLE_ROW_VALUES_RIGHT;
			}
		};
		controller.setAssignmentComposite(composite);
		controller.setAssignmentCompositeModel(model);

		List<IAssignable<MyTo>> tableRowValuesRight = model.getTableRowValuesRight();
		Assert.assertEquals(2, tableRowValuesRight.size());

		List<IAssignable<MyTo>> tableRowValuesLeft = model.getTableRowValuesLeft();
		Assert.assertEquals(0, tableRowValuesLeft.size());

		controller.moveLeft();

		tableRowValuesRight = model.getTableRowValuesRight();
		Assert.assertEquals(0, tableRowValuesRight.size());

		tableRowValuesLeft = model.getTableRowValuesLeft();
		Assert.assertEquals(2, tableRowValuesLeft.size());
	}
}
