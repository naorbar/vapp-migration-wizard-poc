package com.ca.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ca.service.MigrationService;
import com.ca.steps.dto.StepExecutionRequest;
import com.ca.steps.dto.StepExecutionResponse;
import com.ca.steps.dto.StepMetadata;

@Controller
public class MigrationController {

	@Autowired
	private MigrationService migrationService;

	@RequestMapping("/version")
	public @ResponseBody String getVersion() throws Exception {
		return this.migrationService.getVersion();
	}
	
	/**
	 * @author barna10
	 * @description Get all the implemented classes metadata under com.ca.migration_machine.impl
	 * @Sample http://localhost:8080/steps/metadata
	 */
	@RequestMapping(value="/steps/metadata", method=RequestMethod.GET)
	public @ResponseBody List<StepMetadata> getAllStepsMetadata() throws Exception {
		return this.migrationService.getAllSupportedStepsMetatdata();
	}
	
	/**
	 * @author barna10
	 * @description Get a specific step metadata
	 * @Sample http://localhost:8080/steps/com.ca.steps.impl.CopyFileStepImpl/metadata
	 */
	@RequestMapping(value="/steps/{className}/metadata", method=RequestMethod.GET)
	public @ResponseBody StepMetadata getStepMetadata(@PathVariable("className") String className) throws Exception {
		return this.migrationService.getStepMetatdata(className);
	}
	
	/**
	 * @author barna10
	 * @description Run a list of steps
	 * @Sample http://localhost:8080/steps/run
	 */
	@RequestMapping(value="/steps/run", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK) // Use this since the method returns void
	public void runSteps(@RequestBody List<StepExecutionRequest> requestsList) throws Exception {
		this.migrationService.runSteps(requestsList);
	}
	
	/**
	 * @author barna10
	 * @description Return the status of the current/last run
	 * @Sample http://localhost:8080/steps/status
	 */
	@RequestMapping(value="/steps/status", method=RequestMethod.GET)
	public @ResponseBody Map<String, StepExecutionResponse> getLastRunStatus() throws Exception {
		if (MigrationService.responseMap == null)
			return new HashMap<String, StepExecutionResponse>();
		return MigrationService.responseMap;
	}

}
