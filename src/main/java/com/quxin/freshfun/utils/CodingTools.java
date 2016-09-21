package com.quxin.freshfun.utils;

import java.io.UnsupportedEncodingException;

public class CodingTools {
	
	public String enCodeStr(String str) {  
        try {  
          return new String(str.getBytes("iso-8859-1"), "UTF-8");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
            return null;  
        }  
    } 

}
