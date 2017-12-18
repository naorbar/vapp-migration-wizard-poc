package com.ca.service;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.ca.steps.base.BaseStep;
import com.ca.steps.dto.ExecutionResult;
import com.ca.steps.dto.ExecutionState;
import com.ca.steps.dto.StepExecutionRequest;
import com.ca.steps.dto.StepExecutionResponse;
import com.ca.steps.dto.StepMetadata;

@Component
public class MigrationService {

	private static final Logger log = LogManager.getLogger();

	// Map of requestId and the StepExecutionResponse:
	public static Map<String, StepExecutionResponse> responseMap;
	
	public String getVersion() throws Exception {
		try {
			// TODO - FIX THIS
			MavenXpp3Reader reader = new MavenXpp3Reader();
			org.apache.maven.model.Model model = reader.read(new FileReader("pom.xml"));
			//System.out.println(model.getId());
			//System.out.println(model.getGroupId());
			//System.out.println(model.getArtifactId());
			//System.out.println(model.getVersion());
			return model.getVersion();
		} catch (Exception e) {
			throw new Exception("Failed to get the project version: " + e.getMessage(), e);
		}
	}
	
	/**
	 * @author barna10
	 * @description Get all the implemented classes metadata under this inner package: com.ca.steps.impl or in this external folder: ./custom/com/ca/steps/impl
	 */ 
	public List<StepMetadata> getAllSupportedStepsMetatdata() throws Exception {
		try {    		
			List<StepMetadata> list = new ArrayList<StepMetadata>();
    		
			// Get all the classes under this inner package: com.ca.steps.impl
			// Or in this external folder: ./custom/com/ca/steps/impl
			List<String> scannedPackages = Arrays.asList("com/ca/steps/impl/*", "file:**/custom/com/ca/steps/impl/*.class");
			
			// Set the class loader with the given external folders:
			String currentDir = Paths.get(".").toAbsolutePath().normalize().toString();
			URL customDirUrl = new File(currentDir + "/custom").toURI().toURL();
			log.debug("Adding this url to the class loader: " + customDirUrl.toString());
			ClassLoader cl = URLClassLoader.newInstance(new URL[]{customDirUrl}, this.getClass().getClassLoader());
			
			PathMatchingResourcePatternResolver scanner = new PathMatchingResourcePatternResolver();
			for (String scannedPackage : scannedPackages) {
				Resource[] resources = scanner.getResources(scannedPackage);
				if (resources == null || resources.length == 0)
					log.warn("Warning: could not find any resources in this scanned package: " + scannedPackage);
			    for (Resource resource : resources) {
			    	Class<?> c = cl.loadClass("com.ca.steps.impl." + resource.getFilename().replaceFirst(".class", ""));
			    	if (c.newInstance() instanceof BaseStep)
						list.add(((BaseStep) c.newInstance()).getMetadata());
			    }
			}
			return list;
		} catch (Exception e) {
			throw new Exception("Failed to get the supported steps metadata: " + e.getMessage(), e);
		}
    }
	
	/**
	 * @author barna10
	 * @description Get a specific step metadata
	 */
	public StepMetadata getStepMetatdata(String className) throws Exception {
    	try {
    		Class<?> c = Class.forName(className);
    		if (c.newInstance() instanceof BaseStep)
				return ((BaseStep) c.newInstance()).getMetadata();
    		return null;
    	} catch (Exception e) {
			throw new Exception("Failed to get the supported steps metadata: " + e.getMessage(), e);
		}
	}
	
	/**
	 * @author barna10
	 * @description Run a list of steps
	 */
	public void runSteps(List<StepExecutionRequest> stepExecutionRequests) throws Exception {
		log.debug("Migration Machine - START");

		// Clear the responseMap:
		if (responseMap == null)
			responseMap = new HashMap<String, StepExecutionResponse>();
		if (!responseMap.isEmpty())
			log.warn("Warning: clearing the response list: last run's response list will not be saved...");
		responseMap = new HashMap<String, StepExecutionResponse>();
		for (StepExecutionRequest request : stepExecutionRequests)
			this.setResponse(request, ExecutionResult.NONE, ExecutionState.NOT_STARTED, "Step Not Started");
		
		// 1. Execute each request one by one:
		// 		1.1 Load the step instance
		// 		1.2 Initialize the step members with the given request fields
		// 		1.3 Validate the step if required
		// 		1.4 Execute the step if required
		// 		1.5 Rollback the step if required
		// 2. Return the responseList accordingly
		for (StepExecutionRequest request : stepExecutionRequests) {
			log.info("~~~~ Working on request: [" + request.getId() + "] - START ~~~~");
			this.setResponse(request, ExecutionResult.NONE, ExecutionState.IN_PROGRESS, "Step In Progress");
			BaseStep step = null;
			
			// Load the step instance:
			try {
				log.info("Loading step instance: " + request.getId());
				Class<?> c = Class.forName(request.getClassName());
				if (c.newInstance() instanceof BaseStep) {
					step  = (BaseStep) c.newInstance();
					this.setResponse(request, ExecutionResult.NONE, ExecutionState.IN_PROGRESS, "Loading succeeded");
				} else { 
					log.error("Failed to load step: " + request.getClassName());
					this.setResponse(request, ExecutionResult.FAILURE, ExecutionState.COMPLETED, "Loading failed: " + "This class is not an instance of BaseStep");
					continue;
				}
			} catch (Exception e) {
				log.error("Failed to load step: " + request.getClassName() + " - " + e.getMessage());
				this.setResponse(request, ExecutionResult.FAILURE, ExecutionState.COMPLETED, "Loading failed: " + e.getMessage());
				continue;
			}
			
			// Initialize the step with the given fields found in the request:
			try {
				log.info("Initializing step: " + request.getId());
				step.init(request.getMembers());
				this.setResponse(request, ExecutionResult.NONE, ExecutionState.IN_PROGRESS, "Initialization succeeded");
			} catch (Exception e) {
				log.error("Failed to initialize step: " + request.getId() + " - " + e.getMessage());
				this.setResponse(request, ExecutionResult.FAILURE, ExecutionState.COMPLETED, "Initialization failed: " + e.getMessage());
				continue;
			}
			
			switch (request.getRequestType()) {
			case VALIDATE:
				try {
					log.info("Validating step: " + request.getId());
					this.setResponse(request, ExecutionResult.NONE, ExecutionState.IN_PROGRESS, null);
					step.validate();
					this.setResponse(request, ExecutionResult.SUCCESS, ExecutionState.COMPLETED, "Validation succeeded");
				} catch (Exception e) {
					log.error("Failed to validate step: " + request.getId() + " - " + e.getMessage());
					this.setResponse(request, ExecutionResult.FAILURE, ExecutionState.COMPLETED, "Validation failed: " + e.getMessage());
				}
				break;
			case EXECUTE:
				try {
					log.info("Executing step: " + request.getId());
					this.setResponse(request, ExecutionResult.NONE, ExecutionState.IN_PROGRESS, null);
					step.execute();
					this.setResponse(request, ExecutionResult.SUCCESS, ExecutionState.COMPLETED, "Execution succeeded");
				} catch (Exception e) {
					log.error("Failed to execute step: " + request.getId() + " - " + e.getMessage());
					this.setResponse(request, ExecutionResult.FAILURE, ExecutionState.COMPLETED, "Execution failed: " + e.getMessage());
				}
				break;
			case ROLLBACK:
				try {
					log.info("Rolling back step: " + request.getId());
					this.setResponse(request, ExecutionResult.NONE, ExecutionState.IN_PROGRESS, null);
					step.rollback();
					this.setResponse(request, ExecutionResult.SUCCESS, ExecutionState.COMPLETED, "Rollback succeeded");
				} catch (Exception e) {
					log.error("Failed to rollbacks step: " + request.getId() + " - " + e.getMessage());
					this.setResponse(request, ExecutionResult.FAILURE, ExecutionState.COMPLETED, "Rollback failed: " + e.getMessage());
				}
				break;
			case ALL:
				try {
					log.info("Validating step: " + request.getId());
					this.setResponse(request, ExecutionResult.NONE, ExecutionState.IN_PROGRESS, null);
					step.validate();
					this.setResponse(request, ExecutionResult.NONE, ExecutionState.IN_PROGRESS, "Validation succeeded");
				} catch (Exception ex) {
					log.error("Failed to validate step: " + request.getId() + " - " + ex.getMessage());
					this.setResponse(request, ExecutionResult.FAILURE, ExecutionState.COMPLETED, "Validation failed: " + ex.getMessage());
					break;
				}

				try {
					log.info("Executing step: " + request.getId());
					step.execute();
					this.setResponse(request, ExecutionResult.SUCCESS, ExecutionState.COMPLETED, "Execution succeeded");
				} catch (Exception e) {
					log.error("Failed to execute step: " + request.getId() + " - " + e.getMessage());
					this.setResponse(request, ExecutionResult.FAILURE, ExecutionState.IN_PROGRESS, "Execution failed: " + e.getMessage());
					// Try to rollback step:
					try {
						log.info("Rolling back step: " + request.getId());
						step.rollback();
						this.setResponse(request, ExecutionResult.FAILURE, ExecutionState.COMPLETED, "Rollback succeeded");
					} catch (Exception ex) {
						log.error("Failed to rollback step: " + request.getId() + " - " + ex.getMessage());
						this.setResponse(request, ExecutionResult.FAILURE, ExecutionState.COMPLETED, "Rollback failed: " + ex.getMessage());
					}
				} finally {
					// Free resources if needed:
					log.info("Finalizing step: " + request.getId());
					// TODO TBD
					//s.free();
				}
				break;
			default:
				log.warn("Found an unsupported request type: " + request.getRequestType() + " for step: " + request.getId() + " - skipping...");
				this.setResponse(request, ExecutionResult.FAILURE, ExecutionState.COMPLETED, "Step failed: Found an unsupported request type");
				break;
			}
			log.info("~~~~ Working on request: [" + request.getId() + "] - END ~~~~");
		}
	}
	
	private void setResponse(StepExecutionRequest request, ExecutionResult result, ExecutionState state, String message) {
		if (responseMap == null)
			responseMap = new HashMap<String, StepExecutionResponse>();
		if (!responseMap.containsKey(request.getId()))
			responseMap.put(request.getId(), new StepExecutionResponse());
		
		// If the message is null, keep the last message:
		if (message == null)
			message = responseMap.get(request.getId()).getMessage();
		responseMap.put(request.getId(), new StepExecutionResponse(request.getId(), request.getClassName(), result, state, message));	
	}
}
