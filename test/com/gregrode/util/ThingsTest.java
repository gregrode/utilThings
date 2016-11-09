/**
 *
 */
package com.gregrode.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public final class ThingsTest
{
	enum Color
	{
		BLUE,
		RED,
		GREEN
	}

	@Test
	public void isEmptyTest()
	{
		Assert.assertTrue(Things.isEmpty((String[]) null));
		Assert.assertTrue(Things.isEmpty(new String[0]));
		Assert.assertFalse(Things.isEmpty(new String[] { "test" }));

		Assert.assertTrue(Things.isEmpty((List<?>) null));
		Assert.assertTrue(Things.isEmpty(Arrays.asList(new String[0])));
		Assert.assertFalse(Things.isEmpty(Arrays.asList(new String[] { "test" })));

		final Map<String, String> map = new HashMap<>();

		Assert.assertTrue(Things.isEmpty((Map<?, ?>) null));
		Assert.assertTrue(Things.isEmpty(map));
		map.put("greg", "is great");
		Assert.assertFalse(Things.isEmpty(map));
	}

	@Test(expected = NullPointerException.class)
	public void verifyTest()
	{
		Assert.assertNotNull(Things.verify("test"));
		Assert.assertNotNull(Things.verify("test", s -> !s.isEmpty()));
		Assert.assertNotNull(Things.verify("test", "null", s -> !s.isEmpty()));
		Assert.assertTrue(Things.verify(true));
		Assert.assertNotNull(Things.verify(Things.toMap(LinkedHashMap::new, Things.toEntry("firstName", "greg"))));
		Things.verify("test", (str) -> str.length() == 5);
		Things.verify(null, "This is null");
	}

	@Test
	public void pluckTest()
	{
		final List<Map.Entry<String, String>> entries = Arrays.asList(Things.toEntry("firstname", "greg"),
			Things.toEntry("lastName", "Dennis"));
		final Collection<String> keys = Things.pluck(entries, Map.Entry::getKey);
		Assert.assertNotNull(keys);
	}

	@Test
	public void toJSONTest()
	{
		final String json = Things.toJSON("{'home' : 'house', 'vehicle' : 'car'}");
		Assert.assertNotNull(json);
	}

	@Test
	public void toMapTest()
	{

		final Map<String, String> map = Things.toMap(LinkedHashMap::new, Things.toEntry("firstName", "greg"),
			Things.toEntry("lastName", "Dennis"));
		Assert.assertTrue(map.containsKey("firstName"));
		Assert.assertEquals("greg", map.get("firstName"));

		final Map<String, String> map2 = Things.toMap("{'home' : 'house', 'vehicle' : 'car'}");
		Assert.assertTrue(map2.containsKey("home"));
		Assert.assertEquals("house", map2.get("home"));

		final Map<Integer, String> map3 = Things.toMap(HashMap::new,
			"{'1': 'apple', '4' : 'zebra', '7' : 'queens', '100' : 'baseball'}");
		Assert.assertEquals("apple", map3.get("1"));

		final Map<Color, Integer> map4 = Things.toMap(Color.class, Color::hashCode);
		Assert.assertTrue(map4.containsKey(Color.RED));
	}

	@Test
	public void buildTest()
	{
		final List<String> list = new ArrayList<>();
		list.add("Greg");
		list.add("Dennis");

	}

	@Test(expected = NullPointerException.class)
	public void nonNullTest()
	{
		Assert.assertNotNull((Things.nonNull(null, "")));
		Assert.assertNotNull((Things.nonNull("", null)));
		Assert.assertNotNull((Things.nonNull("", () -> null)));
		Assert.assertNotNull((Things.nonNull(null, () -> "")));
		Assert.assertNull((Things.nonNull(null, null)));

	}
}
