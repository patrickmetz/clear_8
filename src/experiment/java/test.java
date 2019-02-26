/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 26.02.19 11:25.
 * Copyright (c) 2019. All rights reserved.
 */

public class test {

            /*
        byte: -(2^7) bis (2^7)-1
              -128   bis 127

             -128
               |
         -32 : 1110 0000
                |
              +64+32
        */


    /*
        short: -(2^15) bis (2^7)-1
               -32768  bis 32767

             -0
              |
        224:  0000 0000 1110 0000
                         |
                      128+64+32
     */

    public static short castToShort(byte b) {
        return (short) b;
    }

    public static short convertToUnsigned(byte b) {
        return (short) (b & 0xFF);
    }

    public static void main(String[] args) {

        byte b = (byte) -32;

        System.out.println(b);
        System.out.println(castToShort(b));
        System.out.println(convertToUnsigned(b));
    }

}
