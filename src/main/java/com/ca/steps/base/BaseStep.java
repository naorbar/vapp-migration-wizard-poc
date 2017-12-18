package com.ca.steps.base;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ca.steps.dto.StepMetadata;

public abstract class BaseStep implements Step {

	protected static final Logger log = LogManager.getLogger();

	protected Map<String, String> properties;
	
	/**
	 * @author barna10
	 * @return Return the class members and methods as json string
	 */
	public StepMetadata getMetadata() throws Exception {
		try {
			StepMetadata metadata = new StepMetadata();
			Class<?> clazz = this.getClass().getClassLoader().loadClass(this.getClass().getName());
			
			// list of methods:
			List<String> methodsList = new ArrayList<String>();
			for (Method m : clazz.getDeclaredMethods())
				methodsList.add(m.getName());
		
			metadata.setClassName(this.getClass().getName());
			metadata.setSupportedMethods(methodsList);
			return metadata;
			
		} catch (Exception e) {
			throw new Exception("Failed to get step metadata: " + e.getMessage(), e);
		}
	}

	public void validate() throws Exception {}
	
	/**
	 * @author barna10
	 * @description This is abstract since the inherited classes must implement it
	 */
	public abstract void execute() throws Exception;
	
	public void rollback() throws Exception {}

	/**
	 * @author barna10
	 * @description Set the class members (given as properties map)
	 */
	public void init(Map<String, String> properties) throws Exception {
		if (properties != null)
			this.properties = properties;
		else 
			this.properties = new HashMap<String, String>();
	}

}
