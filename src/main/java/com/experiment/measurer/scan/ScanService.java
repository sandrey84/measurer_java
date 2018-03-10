package com.experiment.measurer.scan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ScanService {

	public void onScanDataAvailable(Map<Integer, Integer> scanData) {
		log.info("new scan data -> " + scanData);
	}

	public void onSingleScanDataAvailable(int distanceInCm) {
		log.info("single scan -> " + distanceInCm + " cm");
	}
}
