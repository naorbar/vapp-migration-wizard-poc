package com.ca.steps.impl;

import com.ca.steps.base.BaseStep;

public class Test extends BaseStep {

	@Override
	public void execute() throws Exception {
		// do something:
		log.warn("HELLO FROM CUSTOM STEP - START");
		log.warn("WAITING...");
		Thread.sleep(10000);
		log.warn("HELLO FROM CUSTOM STEP - END");
		
	
	}

}
