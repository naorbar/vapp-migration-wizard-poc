package com.ca.steps.impl;

import java.util.Arrays;

import com.ca.steps.base.BaseStep;
import com.ca.steps.dto.StepMetadata;

public class FixLdapStepImpl extends BaseStep {

	@Override
	public StepMetadata getMetadata() throws Exception {
		StepMetadata metadata = super.getMetadata();
		metadata.setMandatoryFields(Arrays.asList("host", "port", "userDn", "password"));
		metadata.setStepDependencies(Arrays.asList("CopyFileStepImpl"));
		return metadata;
	}
	
	@Override
	public void validate() throws Exception {
		if (this.properties.containsKey("host") &&  this.properties.get("host") != null &&
				this.properties.containsKey("port") &&  this.properties.get("port") != null &&
				this.properties.containsKey("userDn") &&  this.properties.get("userDn") != null &&
				this.properties.containsKey("password") &&  this.properties.get("password") != null) {
		} else {
			throw new Exception("Invalid arguments: host/port/userDn/password can't be null");
		}
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
	}
}
