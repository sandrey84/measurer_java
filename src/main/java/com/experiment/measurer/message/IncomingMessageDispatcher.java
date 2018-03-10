package com.experiment.measurer.message;

import com.experiment.measurer.log.LogService;
import com.experiment.measurer.scan.ScanService;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IncomingMessageDispatcher {

	@Autowired
	private LogService logService;

	@Autowired
	private ScanService scanService;

	private static final String END_OF_MESSAGE_MARKER = "\n";
	private static final String LOG_INFO_MESSAGE_PREFIX = "INFO_LOG_";
	private static final String ERROR_INFO_MESSAGE_PREFIX = "ERROR_LOG_";
	private static final String SCAN_RESULT_MESSAGE_PREFIX = "SCAN_RESULT_";
	private static final String SINGLE_SCAN_RESULT_MESSAGE_PREFIX = "SINGLE_SCAN_RESULT_";

	public void processMessage(String message) {
		log.debug("got: {}", message);
		//in case when few messages received in one string
		Splitter.on(END_OF_MESSAGE_MARKER).split(message).forEach(this::dispatchMessage);
	}

		private void dispatchMessage(String message) {
		if (message.startsWith(LOG_INFO_MESSAGE_PREFIX)) {
			log.debug("process: {}", LOG_INFO_MESSAGE_PREFIX);
			logService.processInfo(removePrefix(message, LOG_INFO_MESSAGE_PREFIX));
			return;
		}

		if (message.startsWith(ERROR_INFO_MESSAGE_PREFIX)) {
			log.debug("process: {}", ERROR_INFO_MESSAGE_PREFIX);
			logService.processError(removePrefix(message, ERROR_INFO_MESSAGE_PREFIX));
			return;
		}

		if (message.startsWith(SCAN_RESULT_MESSAGE_PREFIX)) {
			log.debug("process: {}", SCAN_RESULT_MESSAGE_PREFIX);
			scanService.onScanDataAvailable(createScanData(removePrefix(message, SCAN_RESULT_MESSAGE_PREFIX)));
			return;
		}

		if (message.startsWith(SINGLE_SCAN_RESULT_MESSAGE_PREFIX)) {
			log.debug("process: {}", SINGLE_SCAN_RESULT_MESSAGE_PREFIX);
			scanService.onSingleScanDataAvailable(Integer.valueOf(removePrefix(message, SINGLE_SCAN_RESULT_MESSAGE_PREFIX)));
			return;
		}

		log.warn("unknown message: {}", message);
	}

	Map<Integer,Integer> createScanData(String rawData) {
		return Splitter.on(",").withKeyValueSeparator("=").split(rawData)
				.entrySet().stream()
				.collect(Collectors.toMap(
						e -> Integer.valueOf(e.getKey()),
						e -> Integer.valueOf(e.getValue())));
	}

	private String removePrefix(String message, String prefix) {
		return message.replaceFirst(prefix, "");
	}
}
