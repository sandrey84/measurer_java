package com.experiment.measurer.dataChannel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Service
public class ChannelManager {

	private static final int READ_SERIAL_DELAY_MS = 1000;
	private static final int WRITE_SERIAL_DELAY_MS = 100;

	@Autowired
	private IncomingDataJoiner incomingDataJoiner;

	private Queue<String> messagesToSend = new ConcurrentLinkedQueue<>();


	@Autowired
	private DataChannel dataChannel;

	@Scheduled(fixedDelay = WRITE_SERIAL_DELAY_MS)
	private void write() {
		if (messagesToSend.isEmpty()) {
			return;
		}

		dataChannel.write(messagesToSend.poll());
	}

	@Scheduled(fixedDelay = READ_SERIAL_DELAY_MS)
	private void read() throws InterruptedException {
		String data = dataChannel.read();

		if (data != null) {
			log.trace("read ----> " + data);
			incomingDataJoiner.processMessage(data);
		}
	}
}
