package com.experiment.measurer.dataChannel;


public interface DataChannel {

	String read();
	void write(String data);
}
