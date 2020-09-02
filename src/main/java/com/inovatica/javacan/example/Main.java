package com.inovatica.javacan.example;

import static com.inovatica.javacan.example.CanTestHelper.CAN_INTERFACE;
import static com.inovatica.javacan.example.CanTestHelper.CAN_INTERFACE2;
import static tel.schich.javacan.CanFrame.FD_NO_FLAGS;
import static tel.schich.javacan.CanSocketOptions.LOOPBACK;
import static tel.schich.javacan.CanSocketOptions.RECV_OWN_MSGS;

import tel.schich.javacan.CanChannels;
import tel.schich.javacan.CanFrame;
import tel.schich.javacan.RawCanChannel;

public class Main {

	public static void main(String[] args) throws Exception {
		testLoopback();
//		testOwnMessage();
	}

	static void testOwnMessage() throws Exception {
		try (final RawCanChannel socket = CanChannels.newRawChannel()) {
			socket.bind(CAN_INTERFACE);

			socket.configureBlocking(false);
			socket.setOption(RECV_OWN_MSGS, true);

			final CanFrame input = CanFrame.create(0x7EC, FD_NO_FLAGS, new byte[] { 0x20, 0x33 });
			socket.write(input);
			final CanFrame output = socket.read();

			System.out.println("input: " + input.toString());
			System.out.println("output: " + output.toString());
		}
	}

	static void testLoopback() throws Exception {
		try (final RawCanChannel a = CanChannels.newRawChannel()) {
			a.bind(CAN_INTERFACE);

			try (final RawCanChannel b = CanChannels.newRawChannel()) {
				b.bind(CAN_INTERFACE2);
				b.configureBlocking(false);

				final CanFrame input = CanFrame.create(0x7EB, FD_NO_FLAGS, new byte[] { 0x20, 0x33 });
				a.write(input);
				final CanFrame output = b.read();

				System.out.println("input: " + input.toString());
				System.out.println("output: " + output.toString());

				a.setOption(LOOPBACK, false);
			}
		}
	}

}
