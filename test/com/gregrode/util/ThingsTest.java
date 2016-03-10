/**
 * 
 */
package com.gregrode.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public final class ThingsTest
{

	@Test(expected = NullPointerException.class)
	public void verifyTest()
	{
		Things.verify("test", (str) -> str.length() == 5);
	}

	@Test
	public void toJSONTest()
	{
		String json = Things.toJSON("{'home' : 'house', 'vehicle' : 'car'}");
		Assert.assertNotNull(json);
	}

	@Test
	public void toMapTest()
	{

		final Map<String, String> map = Things.toMap(LinkedHashMap::new, Things.pair("firstName", "greg"), Things.pair("lastName", "Dennis"));
		Assert.assertTrue(map.containsKey("firstName"));
		Assert.assertEquals("greg", map.get("firstName"));

		final Map<String, String> map2 = Things.toMap("{'home' : 'house', 'vehicle' : 'car'}");
		Assert.assertTrue(map2.containsKey("home"));
		Assert.assertEquals("house", map2.get("home"));

		final Map<String, String> map3 = Things.toMap(HashMap::new, "{'1': 'apple', '4' : 'zebra', '7' : 'queens', '100' : 'baseball'}");
		Assert.assertEquals("apple", map3.get("1"));

		Map<Color, Integer> map4 = Things.toMap(Color.class, Color::hashCode);
		Assert.assertTrue(map4.containsKey(Color.RED));
	}

	enum Color
	{
		BLUE, RED, GREEN
	}
}
