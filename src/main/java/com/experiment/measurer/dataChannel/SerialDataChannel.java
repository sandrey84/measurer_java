package com.experiment.measurer.dataChannel;

import com.fazecast.jSerialComm.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class SerialDataChannel implements DataChannel {

	private SerialPort serialPort;

	@PostConstruct
	private void initSerialPort() {
		serialPort = SerialPort.getCommPort("/dev/ttyACM1"); // device name TODO: must be changed
		serialPort.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written

		if (serialPort.openPort()) {
			log.info("Port is open :)");
		} else {
			log.error("Failed to open port");
		}
	}

	@Override
	public void write(String data) {
		byte[] bytes = data.getBytes();
		serialPort.writeBytes(bytes, bytes.length);
		log.trace("written: {}", data);
	}

	@Override
	public  String read()  {
		log.trace("attemptToRead");
		int bytesAvailable = serialPort.bytesAvailable();

		if (bytesAvailable == 0) {
			return null;
		}

		byte[] messageAsBytes = new byte[bytesAvailable];
		serialPort.readBytes(messageAsBytes, bytesAvailable);
		String readData = new String(messageAsBytes);
		log.trace("read: {}", readData);
		return readData;
	}
}