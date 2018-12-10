/*
 * Copyright  (c) 2017. By AsherLi0103
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.github.asherli0103.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author AsherLi
 * @version 1.0.00
 */
@SuppressWarnings(value = "unused")
public class BytesUtil {

    /**
     * Number of bytes in a Java short.
     */
    private static final int NUM_BYTES_IN_SHORT = 2;
    /**
     * Number of bytes in a Java int.
     */
    private static final int NUM_BYTES_IN_INT = 4;
    /**
     * Number of bytes in a Java long.
     */
    private static final int NUM_BYTES_IN_LONG = 8;
    /**
     * Hex数组
     */
    private static final String[] hexArray = {
            "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D", "1E", "1F",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F",
            "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F",
            "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D", "5E", "5F",
            "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6A", "6B", "6C", "6D", "6E", "6F",
            "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E", "7F",
            "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8A", "8B", "8C", "8D", "8E", "8F",
            "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F",
            "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE", "AF",
            "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF",
            "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF",
            "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF",
            "E0", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF",
            "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF"
    };

    /**
     * 缓存空间
     */
    private static long maxValueCache[] = new long[64];

    static {
        for (int i = 1; i < 64; i++) {
            maxValueCache[i] = ((long) 1 << i) - 1;
        }
    }

    /**
     * 私有化构造方法,禁止实例化
     */
    private BytesUtil() {
    }

    /**
     * shot转换byte数组
     *
     * @param s short值
     * @return byte数组
     */
    public static byte[] shortToBytes(short s) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(NUM_BYTES_IN_SHORT);
        byteBuffer.putShort(s);
        return byteBuffer.array();
    }

    public static byte[] charToBytes(char data) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
        byteBuffer.putChar(data);
        byteBuffer.array();
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data);
        bytes[1] = (byte) (data >> 8);
        return bytes;
    }

    public static byte[] stringToBytes(String s) {
        if (s == null) {
            return null;
        }
        byte[] s1 = {};
        try {
            s1 = s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ignored) {
        }

        if (s1.length > 0xFFFF) {//65536
            throw new ArrayIndexOutOfBoundsException("字符串长度过长。");
        }

        byte[] b = new byte[s1.length + 2];

        b[0] = (byte) ((s1.length) >> 8);
        b[1] = (byte) ((s1.length));

        System.arraycopy(s1, 0, b, 2, s1.length);
        return b;
    }

    public static byte[] objectToBytes(Object obj) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(obj);
        out.flush();
        byte[] bytes = bout.toByteArray();
        bout.close();
        out.close();
        return bytes;
    }

    public static byte[] floatToBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        return intToBytes(intBits);
    }

    /**
     * Convert a Java int to a 4-byte array.
     *
     * @param i A Java int value.
     * @return A 4-byte array representing the int value.
     */
    public static byte[] intToBytes(int i) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(NUM_BYTES_IN_INT);
        byteBuffer.putInt(i);
        return byteBuffer.array();
    }

    public static byte[] doubleToBytes(double data) {
        long intBits = Double.doubleToLongBits(data);
        return longToBytes(intBits);
    }

    /**
     * Convert a Java long to a 4-byte array.
     *
     * @param l A Java long value.
     * @return A 4-byte array representing the int value.
     */
    public static byte[] longToBytes(long l) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(NUM_BYTES_IN_LONG);
        byteBuffer.putLong(l);
        return byteBuffer.array();
    }

    public static byte[] stringToBytes(String data, String encoding) {
        if (StringUtil.isEmpty(encoding)) {
            encoding = "UTF-8";
        }
        Charset charset = Charset.forName(encoding);
        return data.getBytes(charset);
    }

    /**
     * Convert a byte array to a Java short.
     *
     * @param bytes A byte array.
     * @return A Java short.
     */
    public static short bytesToShort(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return byteBuffer.getShort();
    }

    /**
     * 字节串生成16进制字符串（最笨但最快的办法）
     *
     * @param bytes 要转换的字节串
     * @return 转换后的字符串
     */
    public static String bytesToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(hexArray[0xFF & b]);
        }
        return sb.toString();
    }

    public static Object bytesToObject(byte[] bytes) throws IOException,
            ClassNotFoundException {
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream oi = new ObjectInputStream(bi);
        Object obj = oi.readObject();
        bi.close();
        oi.close();
        return obj;
    }

    public static float bytesToFloat(byte[] bytes) {
        return Float.intBitsToFloat(bytesToInt(bytes));
    }

    /**
     * Convert a byte array to a Java int.
     *
     * @param bytes A byte array.
     * @return A Java int.
     */
    public static int bytesToInt(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return byteBuffer.getInt();
    }

    public static double bytesToDouble(byte[] bytes) {
        long l = bytesToLong(bytes);
        return Double.longBitsToDouble(l);
    }

    /**
     * Convert a byte array to a Java long.
     *
     * @param bytes A byte array.
     * @return A Java long.
     */
    public static long bytesToLong(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return byteBuffer.getLong();
    }

    public static String bytesToString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }

    /**
     * Convert a short to a hex representation.
     *
     * @param s A Java short.
     * @return A hex representation of the short.
     */
    public static String shortToHex(short s) {
        // Java doesn't have a method for converting a short to hex, so use an int instead.
        int i = (int) s;
        return Integer.toHexString(i);
    }

    /**
     * Convert an int to a hex representation.
     *
     * @param i A Java int.
     * @return A hex representation of the int.
     */
    public static String intToHex(int i) {
        return Integer.toHexString(i);
    }

    /**
     * Convert a long to a hex representation.
     *
     * @param l A Java long.
     * @return A hex representation of the long.
     */
    public static String longToHex(long l) {
        return Long.toHexString(l);
    }


    /**
     * Convert a hex representation to a Java short.
     *
     * @param s A hex representation.
     * @return A Java short.
     */
    public static short hexToShort(String s) {
        return Short.parseShort(s, 16);
    }

    /**
     * Convert a hex representation to a Java int.
     *
     * @param s A hex representation.
     * @return A Java int.
     */
    public static int hexToInt(String s) {
        return Integer.parseInt(s, 16);
    }

    /**
     * Convert a hex representation to a Java long.
     *
     * @param s A hex representation.
     * @return A Java long.
     */
    public static long hexToLong(String s) {
        return Long.parseLong(s, 16);
    }

    /**
     * 把字节码转换成16进制
     */
    public static String byte2hex(byte bytes[]) {
        StringBuilder retString = new StringBuilder();
        for (byte aByte : bytes) {
            retString.append(Integer.toHexString(0x0100 + (aByte & 0x00FF))
                    .substring(1).toUpperCase());
        }
        return retString.toString();
    }

    /**
     * 把16进制转换成字节码
     */
    public static byte[] hex2byte(String hex) {
        byte[] bts = new byte[hex.length() / 2];
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2),
                    16);
        }
        return bts;
    }

    /**
     * Get the maximum value for the number of unsigned bits.
     *
     * @param i The number of unsigned bits.
     * @return The maximum value for the number of unsigned bits.
     */
    public static int getMaxIntValueForNumBits(int i) {
        if (i >= 32)
            throw new RuntimeException("Number of bits exceeds Java int.");
        else
            return (int) maxValueCache[i];
    }

    /**
     * Get the maximum value for the number of unsigned bits.
     *
     * @param i The number of unsigned bits.
     * @return The maximum value for the number of unsigned bits.
     */
    public static long getMaxLongValueForNumBits(int i) {
        if (i >= 64)
            throw new RuntimeException("Number of bits exceeds Java long.");
        else
            return maxValueCache[i];
    }

    /**
     * Get a byte array in a printable binary form.
     *
     * @param bytes The byte to be writen.
     * @return A String reprentation of the byte.
     */
    public static String writeBytes(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            // New line every 4 bytes
            if (i % 4 == 0) {
                stringBuffer.append("\n");
            }
            stringBuffer.append(writeBits(bytes[i])).append(" ");
        }
        return stringBuffer.toString();
    }

    /**
     * Get a byte in a printable binary form.
     *
     * @param b The byte to be writen.
     * @return A String reprentation of the byte.
     */
    public static String writeBits(byte b) {
        StringBuilder stringBuffer = new StringBuilder();
        int bit;
        for (int i = 7; i >= 0; i--) {
            bit = (b >>> i) & 0x01;
            stringBuffer.append(bit);
        }
        return stringBuffer.toString();
    }

    /**
     * Get a byte array in a printable binary form.
     *
     * @param bytes        The byte to be writen.
     * @param packetLength length
     * @return A String reprentation of the byte.
     */
    public static String writeBytes(byte[] bytes, int packetLength) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < packetLength; i++) {
            // New line every 4 bytes
            if (i % 4 == 0) {
                stringBuffer.append("\n");
            }
            stringBuffer.append(writeBits(bytes[i])).append(" ");
        }
        return stringBuffer.toString();
    }

    /**
     * change byte array into a unsigned byte array
     *
     * @param source source
     * @return byte[]
     */
    public static byte[] toUnsignedByteArray(byte[] source) {
        //ByteUtil.printByteArray(source);
        //to keep value in the 0-255
        int model = 256;
        if (source == null || source.length == 0) {
            return new byte[0];
        }
        byte[] dest = new byte[source.length];
        for (int i = 0; i < source.length; i++) {
            int tmp = ((source[i] + model) % model) & 0xff;
            dest[i] = (byte) tmp;
        }
        return dest;
    }

}
