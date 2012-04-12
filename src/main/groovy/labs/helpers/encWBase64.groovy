package labs.helpers;

import org.apache.commons.codec.binary.Base64
import com.eviware.soapui.support.types.StringToStringsMap 

class encWBase64 
{
	// new Base64 coder
	static Base64 encoder = new Base64()

	def static String encString(String textToEncode)
	{
		return new String(encoder.encode(textToEncode.getBytes()))
	}

	def static String encHTTPCredentials(String username, String password)
	{
		String credentials = username+":"+password
		return new String(encoder.encode(credentials.getBytes()))
	}
}
