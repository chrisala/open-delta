package au.org.ala.delta.rtf;

import junit.framework.TestCase;

import org.apache.commons.lang.math.IntRange;
import org.junit.Test;

/**
 * Tests the RTFUtils class.
 */
public class RTFUtilsTest extends TestCase {

	/**
	 * Tests the stripFormatting method leaves plain text untouched.
	 */
	@Test
	public void testStripFormattingStringPlainText() {
	
		String text = "I am simple text";
		assertEquals(text, RTFUtils.stripFormatting(text));
		
	}
	
	
	@Test
	public void testRtfToHtml() {
		String text = "\\i{}Ornithospermum\\i0{} Dumoulin, \\i{}Tema\\i0{} Adans.";
		String result = RTFUtils.rtfToHtml(text);
		
		String expected = "<I>Ornithospermum</I> Dumoulin, <I>Tema</I> Adans.";
		assertEquals(expected, result);
		
		text = "First paragraph \\par{} Second paragraph";
	    result = RTFUtils.rtfToHtml(text);
		expected = "First paragraph <P> Second paragraph";
		assertEquals(expected, result);
		
		
		text = "\\sub{}Test\\nosupersub{}";
		result = RTFUtils.rtfToHtml(text);
		expected = "<SUB>Test</SUB>";
		assertEquals(expected, result);
	}
	
	@Test
	public void testMarkKeyword() {
		IntRange range = RTFUtils.markKeyword("\\i{}Ornithospermum\\i0{} Dumoulin, \\i{}Tema\\i0{} Adans.");
		assertEquals(0, range.getMinimumInteger());
		assertEquals(5, range.getMaximumInteger());
		
		range = RTFUtils.markKeyword("No keywords here");
		assertEquals(-1, range.getMinimumInteger());
		assertEquals(-1, range.getMaximumInteger());
		
		
		range = RTFUtils.markKeyword("Hi\\i there.");
		assertEquals(2, range.getMinimumInteger());
		assertEquals(6, range.getMaximumInteger());
		
		range = RTFUtils.markKeyword("Hi\\i ");
		assertEquals(2, range.getMinimumInteger());
		assertEquals(6, range.getMaximumInteger());
	}

}
