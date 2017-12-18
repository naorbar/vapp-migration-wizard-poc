package com.ca.steps.impl;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import com.ca.steps.base.BaseStep;
import com.ca.steps.dto.StepMetadata;

public class CopyFileStepImpl extends BaseStep {

	@Override
	public StepMetadata getMetadata() throws Exception {
		StepMetadata metadata = super.getMetadata();
		metadata.setMandatoryFields(Arrays.asList("source", "target"));
		return metadata;
	}
	
	@Override
	public void validate() throws Exception {
		if (!this.properties.containsKey("source") || !this.properties.containsKey("target") || 
				this.properties.get("source") == null || this.properties.get("target") == null) {
			log.error("Failed to validate step: source/target can not be null");
			throw new Exception("Failed to validate step: source/target can not be null");
		} else if (!new File(this.properties.get("source")).exists() || !new File(this.properties.get("source")).isFile()) {
			log.error("Failed to validate step: source file " + this.properties.get("source") + " does not exist");
			throw new Exception("Failed to validate step: source file " + this.properties.get("source") + " does not exist");
		} else if (!new File(this.properties.get("source")).canRead()) {
				log.error("Failed to validate step: source file " + this.properties.get("source") + " is not readable");
				throw new Exception("Failed to validate step: source file " + this.properties.get("source") + " is not readable");
		}
	}

	@Override
	public void execute() throws Exception {
		Path sourcePath = null;
		Path targetPath = null;
		try {
			sourcePath = FileSystems.getDefault().getPath(this.properties.get("source"));
			targetPath = FileSystems.getDefault().getPath(this.properties.get("target"));
			Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new Exception("Failed to copy file: " + sourcePath + " to: " + targetPath + e.getMessage(), e);
		}
	}

	@Override
	public void rollback() throws Exception { 
		try {
			// Remove the target file if exists:
			Path targetPath = FileSystems.getDefault().getPath(this.properties.get("target"));
			Files.delete(targetPath);
		} catch (Exception e) {
			throw new Exception("Failed to delete the target file: " + e.getMessage(), e);
		}
	}
}
