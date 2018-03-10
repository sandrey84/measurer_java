package com.experiment.measurer.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogService {

	public void processInfo(String logString) {
		log.info(logString);
	}

	public void processError(String logString) {
		log.error(logString);
	}
}
