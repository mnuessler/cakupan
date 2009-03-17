package com.cakupan.xslt.data;

import java.util.*;

/**
 * Comperator for the TreeMap based on linenumber
 * @param <T> 
 * @author Patrick Oosterveld
 */
public class CoverageLineComperator<T> implements Comparator<T> {
        public int compare(Object obj1, Object obj2)
         {
              CoverageLine c1 = (CoverageLine)obj1;
              CoverageLine c2 = (CoverageLine)obj2;

              return(c1.getLineNumber() - c2.getLineNumber() );
         } 
}
