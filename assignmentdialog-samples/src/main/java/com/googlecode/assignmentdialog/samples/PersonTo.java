package com.googlecode.assignmentdialog.samples;

/**
 * This POJO stands for ANY of your objects which should be used in the AssignmentComposite.
 */
public final class PersonTo {
	private final String firstname;
	private final String lastname;

	public PersonTo(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}
}
