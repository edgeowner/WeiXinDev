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


import com.github.asherli0103.utils.enums.BrowserType;
import com.github.asherli0103.utils.exception.UtilException;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class NetWorkUtil {

    private static final String INVALID_IP = "0.0.0.0";

    private static final String NATIVE_IP = "127.0.0.1";

    private static final String UNKNOWN_HOST = "";

    private static final int IP_LENGTH_ERROR = -3;

    private static final int IS_BETWEEN = 0;

    private static final int IS_NOT_BETWEEN = 1;

    private static final String HEADER_USER_AGENT = "USER-AGENT";

    private final static String IE11 = "rv:11.0";

    private final static String IE10 = "MSIE 10.0";

    private final static String IE9 = "MSIE 9.0";

    private final static String IE8 = "MSIE 8.0";

    private final static String IE7 = "MSIE 7.0";

    private final static String IE6 = "MSIE 6.0";

    private final static String MAXTHON = "Maxthon";

    private final static String QQ = "QQBrowser";

    private final static String GREEN = "GreenBrowser";

    private final static String SE360 = "360SE";

    private final static String FIREFOX = "Firefox";

    private final static String OPERA = "Opera";

    private final static String CAMINO = "Camino";

    private final static String CHROME = "Chrome";

    private final static String SAFARI = "Safari";

    private final static String OTHER = "其它";
    private final static String ZH = "zh";
    private final static String ZH_CN = "zh-cn";
    private final static String EN = "en";
    private final static String EN_US = "en";
    private static Map<String, String> langMap = new HashMap<>();

    static {
        langMap.put(ZH, ZH_CN);
        langMap.put(EN, EN_US);
    }

    /**
     * 判断客户端是否为IE
     *
     * @param request 请求流
     * @return true/false
     */
    public static boolean isIE(HttpServletRequest request) {
        int ie = request.getHeader(HEADER_USER_AGENT).toLowerCase().indexOf("msie");
        int ie11 = request.getHeader(HEADER_USER_AGENT).toLowerCase().indexOf("rv:11.0");
        return (ie > 0 || ie11 > 0);
    }

    /**
     * 获取IE版本
     *
     * @param request 请求流
     * @return 版本号
     */
    public static Double getIEVersion(HttpServletRequest request) {
        Double version = 0.0;
        if (checkBrowser(request, IE11)) {
            version = 11.0;
        } else if (checkBrowser(request, IE10)) {
            version = 10.0;
        } else if (checkBrowser(request, IE9)) {
            version = 9.0;
        } else if (checkBrowser(request, IE8)) {
            version = 8.0;
        } else if (checkBrowser(request, IE7)) {
            version = 7.0;
        } else if (checkBrowser(request, IE6)) {
            version = 6.0;
        }
        return version;
    }

    /**
     * 获取浏览器类型
     *
     * @param request 请求流
     * @return 浏览器类型
     */
    public static BrowserType getBrowserType(HttpServletRequest request) {
        BrowserType browserType = null;
        if (checkBrowser(request, IE11)) {
            browserType = BrowserType.IE11;
        } else if (checkBrowser(request, IE10)) {
            browserType = BrowserType.IE10;
        } else if (checkBrowser(request, IE9)) {
            browserType = BrowserType.IE9;
        } else if (checkBrowser(request, IE8)) {
            browserType = BrowserType.IE8;
        } else if (checkBrowser(request, IE7)) {
            browserType = BrowserType.IE7;
        } else if (checkBrowser(request, IE6)) {
            browserType = BrowserType.IE6;
        } else if (checkBrowser(request, FIREFOX)) {
            browserType = BrowserType.Firefox;
        } else if (checkBrowser(request, SAFARI)) {
            browserType = BrowserType.Safari;
        } else if (checkBrowser(request, CHROME)) {
            browserType = BrowserType.Chrome;
        } else if (checkBrowser(request, OPERA)) {
            browserType = BrowserType.Opera;
        } else if (checkBrowser(request, CAMINO)) {
            browserType = BrowserType.Camino;
        } else if (checkBrowser(request, SE360)) {
            browserType = BrowserType.SE360;
        } else if (checkBrowser(request, GREEN)) {
            browserType = BrowserType.GreenBrowser;
        } else if (checkBrowser(request, QQ)) {
            browserType = BrowserType.QQBrowser;
        } else if (checkBrowser(request, MAXTHON)) {
            browserType = BrowserType.Maxthon;
        } else {
            browserType = BrowserType.Other;
        }
        return browserType;
    }

    /**
     * 判断浏览器是否是指定类型
     *
     * @param request     请求流
     * @param browserType 浏览器类型
     * @return true/false
     */
    private static boolean checkBrowser(HttpServletRequest request, String browserType) {
        int type = -1;
        if (StringUtil.isNotBlank(browserType)) {
            type = request.getHeader(HEADER_USER_AGENT).toLowerCase().indexOf(browserType);
        }
        return type > 0;
    }

    /**
     * 获取浏览器语言
     *
     * @param request 请求流
     * @return 语言
     */
    public static String getBrowserLanguage(HttpServletRequest request) {
        String browserLang = request.getLocale().getLanguage();
        String browserLangCode = langMap.get(browserLang);
        if (browserLangCode == null) {
            browserLangCode = EN_US;
        }
        return browserLangCode;
    }

    /**
     * 获得path部分<br>
     * URI →  http://www.aaa.bbb/search?scope=ccc\&amp;q=ddd
     * PATH → /search
     *
     * @param uriStr URI路径
     * @return path
     * @throws UtilException URISyntaxException
     */
    public static String getPath(String uriStr) {
        URI uri;
        try {
            uri = new URI(uriStr);
        } catch (URISyntaxException e) {
            throw new UtilException(e);
        }
        return uri.getPath();
    }

    /**
     * 根据主机名得到IP地址字符串。
     *
     * @param hostName d
     * @return d
     */
    public static String getByName(String hostName) {
        try {
            InetAddress inet = InetAddress.getByName(hostName);
            return inet.getHostAddress();
        } catch (UnknownHostException e) {
            return INVALID_IP;
        }
    }

    /**
     * 根据IP地址得到主机名。
     *
     * @param ip d
     * @return d
     */
    public static String getHostName(String ip) {
        try {
            InetAddress inet = InetAddress.getByName(ip);
            return inet.getHostName();
        } catch (UnknownHostException e) {
            return UNKNOWN_HOST;
        }
    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request s
     * @return s
     * @throws IOException e
     */
    public static String getIpAddress(HttpServletRequest request) throws IOException {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String ip1 : ips) {
                if (!("unknown".equalsIgnoreCase(ip1))) {
                    ip = ip1;
                    break;
                }
            }
        }
        return ip;
    }

    // judge the specified ip is between two ip addresses.
    public static boolean isBetween(String ip, String begin, String end) throws UnknownHostException {

        if (!isValidIP(begin)) {
            throw new UnknownHostException("ip begin :" + begin);
        }
        if (!isValidIP(end)) {
            throw new UnknownHostException("ip end :" + end);
        }
        if (!isValidIP(ip)) {
            throw new UnknownHostException("ip :" + ip);
        }

        if (ip.compareToIgnoreCase(begin) == 0 || ip.compareToIgnoreCase(end) == 0) {
            return true;
        }

        // get the ip part array .byte type is enough.
        String ipArray[] = ip.split("\\.");
        String startArray[] = begin.split("\\.");
        String endArray[] = end.split("\\.");

        long ipLong = ((long) ((((Integer.parseInt(ipArray[0]) << 8) + Integer.parseInt(ipArray[1])) << 8) +
                Integer.parseInt(ipArray[2])) << 8) + Integer.parseInt(ipArray[3]);

        long startLong = ((long) ((((Integer.parseInt(startArray[0]) << 8) + Integer.parseInt(startArray[1])) << 8) +
                Integer.parseInt(startArray[2])) << 8) + Integer.parseInt(startArray[3]);

        long endLong = ((long) ((((Integer.parseInt(endArray[0]) << 8) + Integer.parseInt(endArray[1])) << 8) +
                Integer.parseInt(endArray[2])) << 8) + Integer.parseInt(endArray[3]);

        if (startLong < endLong) {
            return ipLong > startLong && ipLong < endLong;
        } else {
            return ipLong > endLong && ipLong < startLong;
        }
    }

    public static boolean isInTheSameSubnet(String strNetmask, String ip1, String ip2) throws UnknownHostException {
        if (!isValidNetmask(strNetmask)) {
            throw new UnknownHostException(strNetmask);
        }
        if (!isValidIP(ip1)) {
            throw new UnknownHostException(ip1);
        }
        if (!isValidIP(ip2)) {
            throw new UnknownHostException(ip2);
        }
        //return false;

        byte[] ba_netmask = InetAddress.getByName(strNetmask).getAddress();
        byte[] ba_ip1 = InetAddress.getByName(ip1).getAddress();
        byte[] ba_ip2 = InetAddress.getByName(ip2).getAddress();

        for (int i = 0; i < 4; ++i) {
            if ((ba_netmask[i] & ba_ip1[i]) != (ba_netmask[i] & ba_ip2[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * change ip-string-formatted address into byte array
     *
     * @param str ip adress
     * @return byte array ip adress
     * @throws IllegalArgumentException f
     */
    public static byte[] strAddressToByte(String str) throws IllegalArgumentException {
        if (str == null) {
            return null;
        }

        StringTokenizer token = new StringTokenizer(str, ".");
        if (token.countTokens() == 4) {
            byte[] newAdd = new byte[4];
            for (int i = 0; i < 4; i++) {
                String s = token.nextToken();
                try {
                    newAdd[i] = (byte) Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("The input string can't be store into byte array.");
                }
            }
            return newAdd;
        }
        return null;
    }

    public static String getNextIp(String ipBegin, String ipEnd, String thisIp) {
        byte[] byteIp;
        if (thisIp == null || thisIp.equalsIgnoreCase(ipEnd)) {
            return ipBegin;
        }

        try {
            byteIp = (InetAddress.getByName(thisIp)).getAddress();
        } catch (UnknownHostException ex) {
            return ipBegin;
        }
        for (int i = byteIp.length - 1; i >= 0; --i) {
            byteIp[i] += 1;
            if (byteIp[i] != 0) {
                break;
            }
        }

        try {
            String strIp = InetAddress.getByAddress(byteIp).getHostAddress();
            if (isBetween(strIp, ipBegin, ipEnd)) {
                return strIp;
            }
        } catch (UnknownHostException ex1) {
            return null;
        }
        return null;
    }

    /**
     * 通过IP获取地址(需要联网，调用淘宝的IP库)
     *
     * @param ip d
     * @return s
     * @throws IOException d
     */
    public static String getIpInfo(String ip) throws IOException {
        if (StringUtil.isBlank(ip)) {
            ip = NATIVE_IP;
        }
        String info = "";
        try {
            URL url = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
            HttpURLConnection htpcon = (HttpURLConnection) url.openConnection();
            htpcon.setRequestMethod("GET");
            htpcon.setDoOutput(true);
            htpcon.setDoInput(true);
            htpcon.setUseCaches(false);

            InputStream in = htpcon.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder temp = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                temp.append(line).append("\r\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            //临时解决
            info = temp.toString();
        } catch (MalformedURLException | ProtocolException e) {
            //TODO 异常需要解决
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 把long类型的Ip转为一般Ip类型：xx.xx.xx.xx
     *
     * @param ip d
     * @return d
     */
    public static String longToIpv4(Long ip) {
        String s1 = String.valueOf((ip & 4278190080L) / 16777216L);
        String s2 = String.valueOf((ip & 16711680L) / 65536L);
        String s3 = String.valueOf((ip & 65280L) / 256L);
        String s4 = String.valueOf(ip & 255L);
        return s1 + "." + s2 + "." + s3 + "." + s4;
    }

    /**
     * 把xx.xx.xx.xx类型的转为long类型的
     *
     * @param ip d
     * @return d
     */
    public static Long ipv4ToLong(String ip) {
        Long ipLong = 0L;
        String ipTemp = ip;
        ipLong = ipLong * 256 + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf(".")));
        ipTemp = ipTemp.substring(ipTemp.indexOf(".") + 1, ipTemp.length());
        ipLong = ipLong * 256 + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf(".")));
        ipTemp = ipTemp.substring(ipTemp.indexOf(".") + 1, ipTemp.length());
        ipLong = ipLong * 256 + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf(".")));
        ipTemp = ipTemp.substring(ipTemp.indexOf(".") + 1, ipTemp.length());
        ipLong = ipLong * 256 + Long.parseLong(ipTemp);
        return ipLong;
    }

    /**
     * 根据掩码位获取掩码
     *
     * @param maskBit d
     * @return d
     */
    public static String getMaskByMaskBit(String maskBit) {
        return StringUtil.isEmpty(maskBit) ? "error, maskBit is null !" : maskBitMap().get(maskBit);
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP 如 IP串 218.240.38.69/30
     *
     * @param ip      2
     * @param maskBit d
     * @return d
     */
    public static String getBeginIpStr(String ip, String maskBit) {
        return longToIpv4(getBeginIpLong(ip, maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP 如 IP串 218.240.38.69/30
     *
     * @param ip      2
     * @param maskBit d
     * @return d
     */
    public static Long getBeginIpLong(String ip, String maskBit) {
        return ipv4ToLong(ip) & ipv4ToLong(getMaskByMaskBit(maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的终止IP 如 IP串 218.240.38.69/30
     *
     * @param ip      2
     * @param maskBit d
     * @return d
     */
    public static String getEndIpStr(String ip, String maskBit) {
        return longToIpv4(getEndIpLong(ip, maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的终止IP 如 IP串 218.240.38.69/30
     *
     * @param ip      2
     * @param maskBit d
     * @return d
     */
    public static Long getEndIpLong(String ip, String maskBit) {
        return getBeginIpLong(ip, maskBit) + ~ipv4ToLong(getMaskByMaskBit(maskBit));
    }

    /**
     * 根据子网掩码转换为掩码位 如 255.255.255.252转换为掩码位 为 30
     *
     * @param netmarks 2
     * @return d
     */
    public static int getNetMask(String netmarks) {
        StringBuffer sbf;
        String str;
        int inetmask = 0, count;
        String[] ipList = netmarks.split("\\.");
        for (String anIpList : ipList) {
            sbf = toBin(Integer.parseInt(anIpList));
            str = sbf.reverse().toString();
            count = 0;
            for (int i = 0; i < str.length(); i++) {
                i = str.indexOf('1', i);
                if (i == -1) {
                    break;
                }
                count++;
            }
            inetmask += count;
        }
        return inetmask;
    }

    /**
     * 计算子网大小
     *
     * @param maskBit 2
     * @return d
     */
    public static int getPoolMax(int maskBit) {
        if (maskBit <= 0 || maskBit >= 32) {
            return 0;
        }
        return (int) Math.pow(2, 32 - maskBit) - 2;
    }

    public static void ping(String args, Runtime runtime, Process process) {
        try {
            // 执行PING命令
            process = runtime.exec("ping  -n 10 " + args);
            // 实例化输入流
            InputStream is = process.getInputStream();
            // 从字节中读取文本
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"));

            String line = "";
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            is.close();
            br.close();
        } catch (IOException e) {
            runtime.exit(1);
        }
    }

    /**
     * 检测本地端口可用性
     *
     * @param port 被检测的端口
     * @return 是否可用
     */
    public static boolean isUsableLocalPort(int port) {
        if (!isValidPort(port)) {
            // 给定的IP未在指定端口范围中
            return false;
        }
        try {
            new Socket(NATIVE_IP, port).close();
            // socket链接正常，说明这个端口正在使用
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 是否为有效的端口
     *
     * @param port 端口号
     * @return 是否有效
     */
    public static boolean isValidPort(int port) {
        //有效端口是0～65535
        return port >= 0 && port <= 0xFFFF;
    }

    public static boolean isValidNetmask(String inString) {
        if (inString == null) {
            return false;
        }
        int[] iValildPart = {0, 128, 192, 224, 240, 248, 252, 254, 255};

        try {
            String partArray[] = inString.split("\\.");

            if (partArray.length != 4) {
                return false;
            }
            int[] ipPart = new int[4];

            for (int i = 0; i < 4; ++i) {
                ipPart[i] = Integer.parseInt(partArray[i]);
            }
            // every part should be greater than 255 and less than 255
            for (int i = 0; i < 4; ++i) {
                if (ipPart[i] < 0 || ipPart[i] > 255) {
                    return false;
                }
            }
            // the priv part should be 255
            for (int i = 0; i < 3; ++i) {
                if (ipPart[i + 1] != 0 && ipPart[i] != 255) {
                    return false;
                }
            }
            for (int i = 0; i < 4; ++i) {
                boolean bValidCheck = false;
                for (int anIValildPart : iValildPart) {
                    if (ipPart[i] == anIValildPart) {
                        bValidCheck = true;
                        break;
                    }
                }
                if (!bValidCheck) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidIP(String str) {
        if (str == null)
            return false;
        str = str.trim();
        int pos1 = -1;
        int pos2;
        String str_temp;
        boolean have_err = false;
        int i = 0;

        //循环开始处
        while (pos1 < str.length()) {
            pos2 = pos1;
            pos1 = str.indexOf('.', pos2 + 1);
            if (pos1 == -1)
                pos1 = str.length();
            str_temp = str.substring(pos2 + 1, pos1);
            i++;
            if (i > 4) {
                have_err = true;
                break;
            }
            try {
                int a = Integer.parseInt(str_temp);
                if (a < 0 || a > 255) {
                    have_err = true;
                    break;
                }
            } catch (NumberFormatException nfe) {
                have_err = true;
                break;
            }
        }

        return !(have_err || i != 4);
    }

    /**
     * 判定是否为内网IP<br>
     * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类
     * 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
     *
     * @param ipAddress dd
     * @return dd
     **/
    public static boolean isInnerIP(String ipAddress) {
        boolean isInnerIp = false;
        long ipNum = ipv4ToLong(ipAddress);

        long aBegin = ipv4ToLong("10.0.0.0");
        long aEnd = ipv4ToLong("10.255.255.255");

        long bBegin = ipv4ToLong("172.16.0.0");
        long bEnd = ipv4ToLong("172.31.255.255");

        long cBegin = ipv4ToLong("192.168.0.0");
        long cEnd = ipv4ToLong("192.168.255.255");

        isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd) || ipAddress.equals(NATIVE_IP);
        return isInnerIp;
    }

    /**
     * 获得本机的IP地址列表
     *
     * @return IP地址列表
     */
    public static Set<String> localIpv4s() {
        Enumeration<NetworkInterface> networkInterfaces = null;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new UtilException(e.getMessage(), e);
        }

        if (networkInterfaces == null) {
            throw new UtilException("Get network interface error!");
        }

        final HashSet<String> ipSet = new HashSet<String>();

        while (networkInterfaces.hasMoreElements()) {
            final NetworkInterface networkInterface = networkInterfaces.nextElement();
            final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                final InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress != null && inetAddress instanceof Inet4Address) {
                    ipSet.add(inetAddress.getHostAddress());
                }
            }
        }
        return ipSet;
    }

    /**
     * 相对URL转换为绝对URL
     *
     * @param absoluteBasePath 基准路径，绝对
     * @param relativePath     相对路径
     * @return 绝对URL
     */
    public static String toAbsoluteUrl(String absoluteBasePath, String relativePath) {
        try {
            URL absoluteUrl = new URL(absoluteBasePath);
            return new URL(absoluteUrl, relativePath).toString();
        } catch (Exception e) {
            throw new UtilException(StringUtil.format("To absolute url [{}] base [{}] error!", relativePath, absoluteBasePath), e);
        }
    }

    /**
     * 隐藏掉IP地址的最后一部分为 * 代替
     *
     * @param ip IP地址
     * @return 隐藏部分后的IP
     */
    public static String hideIpPart(String ip) {
        return ip.substring(0, ip.lastIndexOf(".") + 1) + "*";
    }

    /**
     * 隐藏掉IP地址的最后一部分为 * 代替
     *
     * @param ip IP地址
     * @return 隐藏部分后的IP
     */
    public static String hideIpPart(long ip) {
        return hideIpPart(longToIpv4(ip));
    }

    /**
     * 构建InetSocketAddress<br>
     * 当host中包含端口时（用“：”隔开），使用host中的端口，否则使用默认端口<br>
     * 给定host为空时使用本地host（127.0.0.1）
     *
     * @param host        Host
     * @param defaultPort 默认端口
     * @return InetSocketAddress
     */
    public static InetSocketAddress buildInetSocketAddress(String host, int defaultPort) {
        if (StringUtil.isBlank(host)) {
            host = NATIVE_IP;
        }

        String destHost = null;
        int port = 0;
        int index = host.indexOf(":");
        if (index != -1) {
            // host:port形式
            destHost = host.substring(0, index);
            port = Integer.parseInt(host.substring(index + 1));
        } else {
            destHost = host;
            port = defaultPort;
        }

        return new InetSocketAddress(destHost, port);
    }

    /**
     * 获取客户端MAC地址(有一定缺陷);
     *
     * @param ipAddress dd
     * @return d
     * @throws IOException e
     */
    public static String getMacAddress(String ipAddress) throws IOException {
        String macAddress = null;
        try {
            String command = "nbtstat -a " + ipAddress;
            Process pp = Runtime.getRuntime().exec(command);
            InputStreamReader isr = new InputStreamReader(pp.getInputStream());
            LineNumberReader lnr = new LineNumberReader(isr);
            String strLine;
            do {
                strLine = lnr.readLine();
                if (strLine != null && strLine.indexOf("MAC Address") > 1) {
                    macAddress = strLine.substring(strLine.indexOf("MAC Address") + 14, strLine.length());
                    break;
                }
            } while (strLine != null);
        } catch (IOException e) {
            macAddress = null;
            //  logger.error("getMacAddress(String ipAddress=" + ipAddress + ")", e);
        }
        return macAddress;
    }

    /**
     * 把 名=值 参数表转换成字符串
     *
     * @param map map数据
     * @return 拼接字符串
     */
    public static String createQueryString(LinkedHashMap<String, String> map) {
        if (map != null && map.size() > 0) {
            String result = "";
            for (Map.Entry<String, String> entry : map.entrySet()) {
                result += Objects.equals("", result) ? "" : "&";
                result += String.format("%s=%s", entry.getKey(), entry.getValue());
            }
            return result;
        }
        return null;
    }

    /**
     * 解析字符串返回map键值对
     *
     * @param queryString 源参数字符串
     * @return map
     */
    @SuppressWarnings("unchecked")
    public static LinkedHashMap<String, Object> parseQueryString(String queryString) {
        if (!StringUtil.isEmpty(queryString) && queryString.indexOf('=') > 0) {
            LinkedHashMap<String, Object> result = new LinkedHashMap();

            String name = null;
            String value = null;
            Object tempValue = "";
            int len = queryString.length();
            for (int i = 0; i < len; i++) {
                char c = queryString.charAt(i);
                if (c == '=') {
                    value = "";
                } else if (c == '&') {
                    if (!StringUtil.isEmpty(name) && value != null) {
                        result.put(name, value);
                    }
                    name = null;
                    value = null;
                } else if (value != null) {
                    value += c;
                } else {
                    name = (name != null) ? (name + c) : "" + c;
                }
            }

            if (!StringUtil.isEmpty(name) && value != null) {
                result.put(name, value);
            }

            return result;
        }
        return null;
    }

    private static StringBuffer toBin(int x) {
        StringBuffer result = new StringBuffer();
        result.append(x % 2);
        x /= 2;
        while (x > 0) {
            result.append(x % 2);
            x /= 2;
        }
        return result;
    }

    /**
     * 存储着所有的掩码位及对应的掩码 key:掩码位 value:掩码（x.x.x.x）
     */
    private static Map<String, String> maskBitMap() {
        Map<String, String> maskBit = new HashMap<>();
        maskBit.put("1", "128.0.0.0");
        maskBit.put("2", "192.0.0.0");
        maskBit.put("3", "224.0.0.0");
        maskBit.put("4", "240.0.0.0");
        maskBit.put("5", "248.0.0.0");
        maskBit.put("6", "252.0.0.0");
        maskBit.put("7", "254.0.0.0");
        maskBit.put("8", "255.0.0.0");
        maskBit.put("9", "255.128.0.0");
        maskBit.put("10", "255.192.0.0");
        maskBit.put("11", "255.224.0.0");
        maskBit.put("12", "255.240.0.0");
        maskBit.put("13", "255.248.0.0");
        maskBit.put("14", "255.252.0.0");
        maskBit.put("15", "255.254.0.0");
        maskBit.put("16", "255.255.0.0");
        maskBit.put("17", "255.255.128.0");
        maskBit.put("18", "255.255.192.0");
        maskBit.put("19", "255.255.224.0");
        maskBit.put("20", "255.255.240.0");
        maskBit.put("21", "255.255.248.0");
        maskBit.put("22", "255.255.252.0");
        maskBit.put("23", "255.255.254.0");
        maskBit.put("24", "255.255.255.0");
        maskBit.put("25", "255.255.255.128");
        maskBit.put("26", "255.255.255.192");
        maskBit.put("27", "255.255.255.224");
        maskBit.put("28", "255.255.255.240");
        maskBit.put("29", "255.255.255.248");
        maskBit.put("30", "255.255.255.252");
        maskBit.put("31", "255.255.255.254");
        maskBit.put("32", "255.255.255.255");
        return maskBit;
    }

    /**
     * 指定IP的long是否在指定范围内
     *
     * @param userIp 用户IP
     * @param begin  开始IP
     * @param end    结束IP
     * @return 是否在范围内
     */
    private static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }
}
