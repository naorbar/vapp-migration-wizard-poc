package com.ca.steps.base;

import com.ca.steps.dto.StepMetadata;


public interface Step {

	/**
	 * @author barna10
	 * @description Return the step's metadata such as description, members (mandatory/optional) etc
	 */
	public StepMetadata getMetadata() throws Exception;

	/**
	 * @author barna10
	 * @description Validate that the step is initialized as needed, throws an exception if the validation failed
	 */
	public void validate() throws Exception;

	/**
	 * @author barna10
	 * @description Execute the step main logic, throws an exception if the execution failed
	 */
	public void execute() throws Exception;

	/**
	 * @author barna10
	 * @description Rollback step, throws an exception if the rollback failed
	 */
	public void rollback() throws Exception;

}
