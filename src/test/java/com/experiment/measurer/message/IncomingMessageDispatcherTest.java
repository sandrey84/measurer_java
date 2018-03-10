package com.experiment.measurer.message;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class IncomingMessageDispatcherTest {

	private IncomingMessageDispatcher dispatcher = new IncomingMessageDispatcher();

	@Test
	public void testCreateScanData() {
		Map<Integer, Integer> expected = new HashMap<>();
		expected.put(20,  55);
		expected.put(22,  44);

		assertEquals(expected, dispatcher.createScanData("22=44,20=55"));
	}
}