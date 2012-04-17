package com.yelllabs.soapy.helpers;


import org.apache.commons.collections.CollectionUtils
import org.apache.commons.collections.PredicateUtils
import com.yelllabs.soapy.exceptions.HasToBeIterableException

/**
* A bunch of various helper methods
*
* @author - Janusz Kowalczyk (38115ja)
* @create-date 2011-11-01
* @lastUpdate - 2011-11-28
*/
class Various{

  /**
  * @brief Removes first word from a string. Words are seperated with specific separator, ie.: " ", ":" etc
  *
  * @param string - a string from which first  word will be removed
  * @param wordSeparator - a word separator
  *
  * @return A string without first  word
  */
  public static String removeFirstWord( String string, String wordSeparator ) {
    def list = string.split( wordSeparator ).toList();
    list = list[ 1..list.size()-1 ]; // remove first  word from list
    return listToString( list, wordSeparator );
  }


  /**
  * @brief Removes first word from a string using space as a default word separator. 
  *
  * @param string - a string from which first word will be removed
  *
  * @return A string without first word
  */
  public static String removeFirstWord( String string ) {
    return removeFirstWord( string, " " );
  }


  /**
  * @brief Removes first N words from a string using specific word separator. 
  *
  * @param string - a string from which first  word will be removed
  * @param numberOfWords - a number of words to remove
  * @param wordSeparator - 
  *
  * @return A string without first  N words
  */
  public static String removeNFirstWords( String string, int numberOfWords, String wordSeparator  ) {
    if ( numberOfWords > 0 ) {
      for (int i=0; i < numberOfWords; i++ ) {
        string = removeFirstWord( string, wordSeparator );
      }
    }
    return string;
  }


  /**
  * @brief Removes first N words from a string using space as a default word separator. 
  *
  * @param string - a string from which first  word will be removed
  * @param numberOfWords - a number of words to remove
  *
  * @return A string without first N words
  */
  public static String removeNFirstWords( String string, int numberOfWords ) {
    return removeNFirstWords( string, numberOfWords, " " );
  }
 

  /**
  * @brief Removes last word from a string. Words are seperated with specific separator, ie.: " ", ":" etc
  *
  * @param string - a string from which last word will be removed
  * @param wordSeparator - a word separator
  *
  * @return A string without last word
  */
  public static String removeLastWord( String string, String wordSeparator ) {
    def list = string.split( wordSeparator ).toList();
    list.pop(); // remove last word from list
    return listToString( list, wordSeparator );
  }


  /**
  * @brief Removes last word from a string using space as a default word separator. 
  *
  * @param string - a string from which last word will be removed
  *
  * @return A string without last word
  */
  public static String removeLastWord( String string ) {
    return removeLastWord( string, " " );
  }



  /**
  * @brief Removes last N words from a string using specific word separator. 
  *
  * @param string - a string from which last word will be removed
  * @param numberOfWords - a number of words to remover
  * @param wordSeparator - 
  *
  * @return A string without last N words
  */
  public static String removeNLastWords( String string, int numberOfWords, String wordSeparator  ) {
    if ( numberOfWords > 0 ) {
      for (int i=0; i < numberOfWords; i++ ) {
        string = removeLastWord( string, wordSeparator  );
      }
    }
    return string;
  }

  /**
  * @brief Removes last N words from a string using space as a default word separator. 
  *
  * @param string - a string from which last word will be removed
  * @param numberOfWords - a number of words to remove
  *
  * @return A string without last N words
  */
  public static String removeNLastWords( String string, int numberOfWords ) {
    return removeNLastWords( string, numberOfWords, " " );
  }
 
  /**
  * @brief Removes randomly selected word from a string. Words are seperated with specific separator, ie.: " ", ":" etc
  *
  * @param string - a string from which random word will be removed
  * @param wordSeparator - a word separator
  *
  * @return A string without randomly selected word
  */
  public static String removeRandomWord( String string, String wordSeparator ) {
    def list = string.split( wordSeparator ).toList();
    int randInd = new Random().nextInt( list.size() ); // select random word index
    list[ randInd ] = "";
    return listToString( list, wordSeparator );
  }

  /**
  * @brief Removes N randomly selected words from a string. Words are seperated with specific separator, ie.: " ", ":" etc
  *
  * @param string - a string from which N random words will be removed
  * @param wordSeparator - a word separator
  * @param numberOfWords - a number of words to remove
  *
  * @return A string without randomly selected N words
  */
  public static String removeNRandomWords( String string, String wordSeparator, int numberOfWords ) {
    if ( numberOfWords > 0 ) {
      for (int i=0; i < numberOfWords; i++) {
        string = removeRandomWord( string, wordSeparator );
      }
    }
    return string; 
  }


  /**
  * @brief Converts a List into a String, each word is separeted with next using custom seperator
  *
  * @param list - List of Strings
  * @param wordSeparator - custom word separator, ie.: ":", " " etc
  *
  * @return String of List values separated with custom seperator
  */
  public static String listToString( List list, String wordSeparator ) {
    String tmp = ""
      if ( list.size() > 0 ) {
        list.each{ word -> 
                      // to avoid adding unnecessary spaces there's an additional check 
                      tmp += ( tmp == "" ) ? word : ( ( word != "" ) ? wordSeparator + word : "" );
                 }
      }
      else   {
        return null;
      }
    return tmp;
  }

  /**
  * @brief Converts a List into a String, each word is separeted with a space
  *
  * @param list - List of Strings
  *
  * @return String of List values separated with a space
  */
  public static String listToString( List list ) {
    return listToString( list, " " );
  }

  /**
  * @brief Converts a List into a String, each word is separeted with a space
  *
  * @param list - List of Strings
  * @param splitter - A Splitter ENUM to set custom word separator
  *
  * @return String of List values separated with a space
  */
  public static String listToString( List list, Splitter splitter ) {
    return listToString( list, splitter.value() );
  }


  public static String getRandVal( List list ) {
    return list.getAt( new Random().nextInt( list.size() ) ).toString();
  }

  public static Object collectionFind( Collection collection, Object searchValue ){
    return CollectionUtils.find( collection, new PredicateUtils().equalPredicate(searchValue));
  }

  public static boolean listContainsInt( List list, Integer searchValue ){
    boolean found = false;
    list.each{
        val->
          if ( new Integer(val.toInteger()).equals( searchValue ) ) found = true;
    } 
    return found;
  } 

    /**
  * @brief Removes all the duplicates from a List
  *
  * @param list
  *
  * @return a list with only unique values ( a Set is used to create unique list )
  */
  public static List removeDuplicates( List list )
  {
    def uniques = [] as Set, dups= [] as Set
        
    list.each{ uniques.add(it) || dups.add(it) }
    uniques.removeAll(dups)
    return uniques.toList();

  }

  public static List getUniques( List list )
  {
    def uniques = [] as Set;
        
    list.each{ uniques.add(it) }
    uniques.retainAll(uniques)
    return uniques.toList();

  }


  /**
  * @brief Checks whether the list contains only the same values
  *
  * @param list Any list
  *
  * @return true when all values are the same and false when it's not.
  */
  public static boolean hasSameValues( List list ) {
    return ( this.removeDuplicates( list ).size() == 0 ) ? true : false;
  }




  /**
  * @brief A simple function to clean a string from various characters
  *
  * @param stringToBeCleaned Any string that contains " ","'" etc
  *
  * @return A String without specific characters;
  */
  public static String cleanString( String stringToBeCleaned ) {

		stringToBeCleaned = stringToBeCleaned.replaceAll("/","");
		stringToBeCleaned = stringToBeCleaned.replaceAll("\\\\","");
		stringToBeCleaned = stringToBeCleaned.replaceAll("\\{","");
		stringToBeCleaned = stringToBeCleaned.replaceAll("\\}","");
		stringToBeCleaned = stringToBeCleaned.replaceAll(":","");
		stringToBeCleaned = stringToBeCleaned.replaceAll(";","");
		stringToBeCleaned = stringToBeCleaned.replaceAll("!","");
		stringToBeCleaned = stringToBeCleaned.replaceAll(" ","-");
		stringToBeCleaned = stringToBeCleaned.replaceAll("&","");
		stringToBeCleaned = stringToBeCleaned.replaceAll("'","");
		stringToBeCleaned = stringToBeCleaned.replaceAll("\"","");

		stringToBeCleaned = stringToBeCleaned.trim();

		return stringToBeCleaned;
	}
	
  public static List stringToList( String string, Splitter splitter ) {
    List result = [];
    string.split( splitter.value() ).each{
      val ->
        splitter.charsToReplace().each{ // get chars that has to be stripped out from the string using pre-defined list
          chr ->
            val = val.replaceAll( chr, "" );
        }
        result.add( val );
    }

    return result;
  }



  /**
  * @brief a helper method to count lines in a text file
  * http://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
  *
  * @param filePath a path to the file
  *
  * @return number of found lines ( new line delimiter is '\n'
  */
  public static int countLines( String filePath ) throws IOException {
    InputStream is = new BufferedInputStream( new FileInputStream( filePath ) );
    try {
      byte[] c = new byte[1024];
      int count = 0;
      int readChars = 0;
      while ((readChars = is.read(c)) != -1) {
        for (int i = 0; i < readChars; ++i) {
          if (c[i] == '\n')
            ++count;
        }
      }
      return count;
    } finally {
      is.close();
    }
  }

  
  public static String getRandomLine( String filePath ) throws IOException {
    InputStream is = new BufferedInputStream( new FileInputStream( filePath ) );
    try {
      byte[] c = new byte[1024];
      int count = 0;
      int readChars = 0;
      String line = "";
      int lineNum = 0;
      int cnt = 0;

      // count lines
      while ((readChars = is.read(c)) != -1) {
        for (int i = 0; i < readChars; ++i) {
          if (c[i] == '\n')
            ++count;
        }
      }
      // select random line
      lineNum = Randomizer.randRange( 1, count );

      // return that line
      FileInputStream fstream = new FileInputStream( filePath );
      DataInputStream ins = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(ins));
      while (  ((line = br.readLine()) != null) && (cnt != lineNum) ) {
        cnt++;
      }
      return line;
    } finally {
      is.close();
    }
  }



  /**
  * @brief A method that cleans the collection 
  *
  * @param coll
  *
  * @return 
  */
  public static <E extends java.lang.Iterable> E cleanCollection(E coll) throws HasToBeIterableException{
    if ( java.lang.Iterable.class.isInstance(coll) ) {
      def uniques = []; 
      //def tmp = coll;
                            
      while( coll.iterator().hasNext() ){
        def value = coll.iterator().next();
        uniques.add(value);
        coll.removeAll(value);
      }

      return uniques;    
 
    }else{
      throw new HasToBeIterableException();
    }
  }

}
