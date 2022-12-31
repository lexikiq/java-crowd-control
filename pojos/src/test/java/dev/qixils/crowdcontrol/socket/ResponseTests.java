package dev.qixils.crowdcontrol.socket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ConstantConditions")
public class ResponseTests {
	@SuppressWarnings("deprecation") // old constructors still need to be tested! :)
	@Test
	public void constructorTest() {
		Request request = new Request.Builder().effect("test").viewer("sdk").id(1).build();

		// Constructor 1

		// null packet type throws IllegalArgumentException
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Response(
				(Socket) null,
				null,
				"Server is disconnecting"
		));
		// effect result packet throws IllegalArgumentException
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Response(
				(Socket) null,
				Response.PacketType.EFFECT_RESULT,
				"Effect applied successfully"
		));
		// null message throws IllegalArgumentException when PacketType#isMessageRequired() is true
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Response(
				(Socket) null,
				Response.PacketType.DISCONNECT,
				null
		));
		// null message doesn't throw when PacketType#isMessageRequired() is false
		Assertions.assertDoesNotThrow(() -> new Response(
				(Socket) null,
				Response.PacketType.LOGIN,
				null
		));
		// doesn't throw when all parameters are valid
		Assertions.assertDoesNotThrow(() -> new Response(
				(Socket) null,
				Response.PacketType.DISCONNECT,
				"Server is disconnecting"
		));

		// Constructor 2

		// negative ID throws IllegalArgumentException
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Response(
				-1,
				null,
				Response.ResultType.SUCCESS,
				"Effect applied successfully",
				null
		));
		// non-positive timeRemaining throws IllegalArgumentException
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Response(
				1,
				null,
				Response.ResultType.SUCCESS,
				"Effect applied successfully",
				Duration.ZERO
		));
		// null result type throws IllegalArgumentException
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Response(
				1,
				null,
				null,
				"Effect applied successfully",
				null
		));
		// doesn't throw when all parameters are valid
		Assertions.assertDoesNotThrow(() -> new Response(
				1,
				null,
				Response.ResultType.SUCCESS,
				"Effect applied successfully",
				Duration.ofSeconds(10)
		));
		Assertions.assertDoesNotThrow(() -> new Response(
				1,
				null,
				Response.ResultType.SUCCESS,
				"Effect applied successfully",
				null
		));

		// Constructor 3

		// null request throws IllegalArgumentException
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Response(
				(Request) null,
				Response.PacketType.DISCONNECT,
				"Server is disconnecting"
		));
		// null packet type throws IllegalArgumentException
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Response(
				request,
				null,
				"Server is disconnecting"
		));
		// effect type packet throws IllegalArgumentException
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Response(
				request,
				Response.PacketType.EFFECT_RESULT,
				"Effect applied successfully"
		));
		// null message throws IllegalArgumentException when PacketType#isMessageRequired() is true
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Response(
				request,
				Response.PacketType.DISCONNECT,
				null
		));
		// null message doesn't throw when PacketType#isMessageRequired() is false
		Assertions.assertDoesNotThrow(() -> new Response(
				request,
				Response.PacketType.LOGIN,
				null
		));
		// doesn't throw when all parameters are valid
		Assertions.assertDoesNotThrow(() -> new Response(
				request,
				Response.PacketType.DISCONNECT,
				"Server is disconnecting"
		));

		// Constructor 4

		// null request throws IllegalArgumentException
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Response(
				null,
				Response.ResultType.SUCCESS,
				"Effect applied successfully",
				1000
		));
		// null result type throws IllegalArgumentException
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Response(
				request,
				null,
				"Effect applied successfully",
				1000
		));
		// negative timeRemaining throws IllegalArgumentException
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Response(
				request,
				Response.ResultType.SUCCESS,
				"Effect applied successfully",
				-1
		));
		// doesn't throw when all parameters are valid
		Assertions.assertDoesNotThrow(() -> new Response(
				request,
				Response.ResultType.SUCCESS,
				"Effect applied successfully",
				1000
		));
		Assertions.assertDoesNotThrow(() -> new Response(
				request,
				Response.ResultType.SUCCESS,
				"Effect applied successfully",
				0
		));
		Assertions.assertDoesNotThrow(() -> new Response(
				request,
				Response.ResultType.SUCCESS,
				null,
				0
		));

		// TODO: am i missing the big mega constructor???
	}

	@SuppressWarnings("deprecation") // old constructors still need to be tested! :)
	@Test
	public void getterTest() {
		// Constructor 1
		Response response = new Response(
				(Socket) null,
				Response.PacketType.LOGIN,
				"Effect applied successfully"
		);
		Assertions.assertEquals(0, response.getId());
		Assertions.assertFalse(response.isOriginKnown());
		Assertions.assertEquals(Response.PacketType.LOGIN, response.getPacketType());
		Assertions.assertNull(response.getResultType());
		Assertions.assertEquals("Effect applied successfully", response.getMessage());
		Assertions.assertNull(response.getTimeRemaining());

		// Constructor 2
		response = new Response(
				1,
				null,
				Response.ResultType.SUCCESS,
				"Effect applied successfully",
				Duration.ofSeconds(1)
		);
		Assertions.assertEquals(1, response.getId());
		Assertions.assertFalse(response.isOriginKnown());
		Assertions.assertEquals(Response.PacketType.EFFECT_RESULT, response.getPacketType());
		Assertions.assertEquals(Response.ResultType.SUCCESS, response.getResultType());
		Assertions.assertEquals("Effect applied successfully", response.getMessage());
		Assertions.assertEquals(Duration.ofSeconds(1), response.getTimeRemaining());

		// Constructor 3
		response = new Response(
				new Request.Builder().type(Request.Type.KEEP_ALIVE).build(),
				Response.PacketType.LOGIN,
				"Effect applied successfully"
		);
		Assertions.assertEquals(0, response.getId());
		Assertions.assertFalse(response.isOriginKnown());
		Assertions.assertEquals(Response.PacketType.LOGIN, response.getPacketType());
		Assertions.assertNull(response.getResultType());
		Assertions.assertEquals("Effect applied successfully", response.getMessage());
		Assertions.assertNull(response.getTimeRemaining());

		// Constructor 4
		response = new Response(
				new Request.Builder().type(Request.Type.KEEP_ALIVE).build(),
				Response.ResultType.SUCCESS,
				"Effect applied successfully",
				1000
		);
		Assertions.assertEquals(0, response.getId());
		Assertions.assertFalse(response.isOriginKnown());
		Assertions.assertEquals(Response.PacketType.EFFECT_RESULT, response.getPacketType());
		Assertions.assertEquals(Response.ResultType.SUCCESS, response.getResultType());
		Assertions.assertEquals("Effect applied successfully", response.getMessage());
		Assertions.assertEquals(Duration.ofSeconds(1), response.getTimeRemaining());

		// Constructor 5
		response = new Response(
				new Request.Builder().type(Request.Type.KEEP_ALIVE).build(),
				Response.ResultType.SUCCESS,
				"Effect applied successfully",
				Duration.ofSeconds(1)
		);
		Assertions.assertEquals(0, response.getId());
		Assertions.assertFalse(response.isOriginKnown());
		Assertions.assertEquals(Response.PacketType.EFFECT_RESULT, response.getPacketType());
		Assertions.assertEquals(Response.ResultType.SUCCESS, response.getResultType());
		Assertions.assertEquals("Effect applied successfully", response.getMessage());
		Assertions.assertEquals(Duration.ofSeconds(1), response.getTimeRemaining());
	}

	@Test
	public void builderTest() {
		// constructor 1 test
		Response.Builder builder = new Response.Builder(1, null).clone();
		Assertions.assertEquals(1, builder.id());
		Assertions.assertNull(builder.originatingSocket());

		// constructor 2 test
		builder = new Response.Builder(new Request.Builder().id(2).type(Request.Type.KEEP_ALIVE).build());
		Assertions.assertEquals(2, builder.id());
		Assertions.assertNull(builder.originatingSocket());
		// other constructor 2 test
		builder = new Request.Builder().id(2).type(Request.Type.KEEP_ALIVE).build().buildResponse();
		Assertions.assertEquals(2, builder.id());
		Assertions.assertNull(builder.originatingSocket());

		// constructor 3 test
		builder = new Request.Builder().id(3).type(Request.Type.KEEP_ALIVE).build().buildResponse();
		Assertions.assertEquals(3, builder.id());
		Assertions.assertNull(builder.originatingSocket());

		// packet type test
		Assertions.assertNull(builder.packetType());
		builder = builder.packetType(Response.PacketType.EFFECT_RESULT);
		Assertions.assertEquals(Response.PacketType.EFFECT_RESULT, builder.packetType());

		// message test
		Assertions.assertNull(builder.message());
		builder = builder.message("Effect applied successfully");
		Assertions.assertEquals("Effect applied successfully", builder.message());

		// time remaining test
		Assertions.assertNull(builder.timeRemaining());
		builder = builder.timeRemaining(1000);
		Assertions.assertEquals(Duration.ofSeconds(1), builder.timeRemaining());
		builder = builder.timeRemaining(2, TimeUnit.SECONDS);
		Assertions.assertEquals(Duration.ofSeconds(2), builder.timeRemaining());
		builder = builder.timeRemaining(Instant.now().plusSeconds(3));
		Assertions.assertFalse(builder.timeRemaining().minusSeconds(2).isNegative());
		builder = builder.timeRemaining(Duration.ofSeconds(4));
		Assertions.assertEquals(Duration.ofSeconds(4), builder.timeRemaining());
		builder = builder.timeRemaining(5, ChronoUnit.SECONDS);
		Assertions.assertEquals(Duration.ofSeconds(5), builder.timeRemaining());

		// result type test
		Assertions.assertNull(builder.type());
		builder = builder.type(Response.ResultType.SUCCESS);
		Assertions.assertEquals(Response.ResultType.SUCCESS, builder.type());

		// build test (clone + toBuilder)
		Response response = builder.clone().build().toBuilder().build();
		Assertions.assertEquals(3, response.getId());
		Assertions.assertFalse(response.isOriginKnown());
		Assertions.assertEquals(Response.PacketType.EFFECT_RESULT, response.getPacketType());
		Assertions.assertEquals("Effect applied successfully", response.getMessage());
		Assertions.assertEquals(Duration.ofSeconds(5), response.getTimeRemaining());
		Assertions.assertEquals(Response.ResultType.SUCCESS, response.getResultType());

		// misc
		response = response.toBuilder().message(null).build();
		Assertions.assertNull(response.getMessage());

		response = response.toBuilder().packetType(null).build();
		Assertions.assertEquals(Response.PacketType.EFFECT_RESULT, response.getPacketType());
	}

	@Test
	public void serializationTest() {
		Response effectResponse = new Response(
				1,
				null,
				Response.ResultType.SUCCESS,
				"Effect applied successfully",
				Duration.ofSeconds(1)
		);
		String json = "{\"id\":1,\"type\":0,\"message\":\"Effect applied successfully\",\"timeRemaining\":1000,\"status\":0}";
		Assertions.assertEquals(Response.fromJSON(effectResponse.toJSON()), Response.fromJSON(json));

		Response loginResponse = new Response(
				(Socket) null,
				Response.PacketType.LOGIN_SUCCESS,
				"Login successful"
		);
		json = "{\"id\":0,\"type\":241,\"message\":\"Login successful\"}";
		Assertions.assertEquals(Response.fromJSON(loginResponse.toJSON()), Response.fromJSON(json));
	}

	@Test
	public void fromRequestBuilder() {
		Request request = new Request.Builder()
				.id(1)
				.effect("test")
				.viewer("qixils")
				.type(Request.Type.START)
				.build();

		// test available response
		Response response = request.buildResponse()
				.type(Response.ResultType.SUCCESS)
				.message("Effect applied successfully")
				.timeRemaining(1000)
				.build();
		Assertions.assertEquals(Response.PacketType.EFFECT_RESULT, response.getPacketType());
		Assertions.assertEquals(Response.ResultType.SUCCESS, response.getResultType());
		Assertions.assertEquals("Effect applied successfully", response.getMessage());
		Assertions.assertEquals(Duration.ofSeconds(1), response.getTimeRemaining());
		Assertions.assertEquals(1, response.getId());
		Assertions.assertFalse(response.isOriginKnown());

		// test unavailable response
		response = request.buildResponse()
				.type(Response.ResultType.UNAVAILABLE)
				.message("Effect not usable in this game")
				.build();
		Assertions.assertEquals(Response.PacketType.EFFECT_RESULT, response.getPacketType());
		Assertions.assertEquals(Response.ResultType.UNAVAILABLE, response.getResultType());
		Assertions.assertEquals("Effect not usable in this game [effect: test]", response.getMessage());
		Assertions.assertNull(response.getTimeRemaining());
		Assertions.assertEquals(1, response.getId());
		Assertions.assertFalse(response.isOriginKnown());
	}
}
