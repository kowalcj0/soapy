package com.yelllabs.soapy.helpers;


class UTF8Helper {




  /**
  * @brief Remove all non UTF-8 Compliant characters from a String
  *
  * @author Naveed Hussain
  *
  * @param inString
  *
  * @return 
  */
  protected static String removeNonUtf8CompliantCharacters(final String inString) {

    if (inString == null) {
      return null;
    }

    StringBuilder newString = new StringBuilder();

    for (int i = 0; i < inString.length(); i++) {
      char ch = inString.charAt(i);

      if (ch == ';' || ch == '\t' || ch == '\n' || ch == '\r' || ch == '\'' || ch == '\"') {
        try {
          newString.append(URLEncoder.encode("" + ch, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
        }
      } else {
        newString.append(ch);
      }
    }

    return newString.toString();
  }



}
