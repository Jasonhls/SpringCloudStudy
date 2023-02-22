package com.cloud.clientserver.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @Author: 何立森
 * @Date: 2023/02/21/18:26
 * @Description:
 */
public class IpUtils {

    public static String getIp() throws Exception{
        if(isWindowsOS()) {
            return InetAddress.getLocalHost().getHostAddress();
        }else {
            return getLinuxLocalIp();
        }
    }

    public static String getLinuxLocalIp() {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if(!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                         enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if(!inetAddress.isLoopbackAddress()) {
                            String ipAddress = inetAddress.getHostAddress().toString();
                            if(!ipAddress.contains("::") && !ipAddress.contains("0:0:")
                                && !ipAddress.contains("fe80")) {
                                ip = ipAddress;
                            }
                        }
                    }
                }
            }
        }catch (Exception e) {
            ip = "127.0.0.1";
            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 判断操作系统是否是Windows
     * @return
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        // 注：这里的system，系统指的是 JRE (runtime)system，不是指 OS
        String osName = System.getProperty("os.name");
        if(osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }
}
