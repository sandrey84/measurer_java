package com.experiment.measurer.dataChannel;

import com.experiment.measurer.message.IncomingMessageDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncomingDataJoiner {

	private final static String END_OF_MESSAGE = "\n";

	@Autowired
	private IncomingMessageDispatcher messaageProcessor;

	private String readData = "";

	public void processMessage(String messge) {
		if (messge.endsWith(END_OF_MESSAGE)) {
			readData += messge.trim();
			messaageProcessor.processMessage(readData);
			readData = "";
		} else {
			readData += messge;
		}
	}

	String substringEndOfMessage(String message) {
		return message.trim();
	}
}
