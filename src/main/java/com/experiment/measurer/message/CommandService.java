package com.experiment.measurer.message;

import com.experiment.measurer.command.Command;
import com.experiment.measurer.dataChannel.DataChannel;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

@Slf4j
@Service
public class CommandService {

	private final static String END_OF_MESSAGE_MARKER = "\n";

	@Autowired
	private DataChannel dataChannel;

	public void send(Command command) {
		send(command, null);
	}

	public void send(Command command, Map<String, String> params) {
		String toSend = command.name();

		if (params != null) {
			toSend += "?";
			toSend += convertParams(params);
		}

		toSend += END_OF_MESSAGE_MARKER;

		log.debug("prepared for sending: {}", toSend);

		dataChannel.write(toSend);
	}

	private String convertParams(Map<String, String> params) {
		return Joiner.on("&").withKeyValueSeparator("=").join(params);
	}

	//temporary
	@Deprecated
	@Scheduled(fixedDelay = 1000)
	public void requestCommandSending() {
		log.debug("asking command");
		System.out.println("Enter command");
		String command = new Scanner(System.in).next() + "\n";
		dataChannel.write(command);
		log.debug("send to channel");
	}
}
