package com.ca.steps.dto;

public class StepExecutionResponse {

	private String id;
	private String className;
	private ExecutionResult result;
	private ExecutionState state;
	private String message;

	public StepExecutionResponse() {
	}

	public StepExecutionResponse(String id, String className, ExecutionResult result,
			ExecutionState state, String message) {
		this.id = id;
		this.className = className;
		this.result = result;
		this.state = state;
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public ExecutionResult getResult() {
		return result;
	}

	public void setResult(ExecutionResult result) {
		this.result = result;
	}

	public ExecutionState getState() {
		return state;
	}

	public void setState(ExecutionState state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
