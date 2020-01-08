package com.map.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 地理位置工具类
 */
public class CoordinatePointUtil {

    /**
     * PI
     */
    public static double pi = 3.1415926535897932384626;
    /**
     *
     */
    public static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    /**
     * 地球半径
     */
    public static double earth_radius = 6378245.0;
    /**
     *
     */
    public static double ee = 0.00669342162296594323;
    //计算两点之间距离


    /**
     * 通过坐标获取纬度
     *
     * @param x 经度
     * @param y 纬度
     * @return
     */
    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 通过坐标获取经度
     *
     * @param x 经度
     * @param y 纬度
     * @return
     */
    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 判断是否在中国
     *
     * @param lat 纬度
     * @param lon 经度
     * @return
     */
    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    /**
     * GPS -> 高德
     *
     * @param lat
     * @param lon
     * @return
     */
    public static double[] gps84_To_Gcj02(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat, lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((earth_radius * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (earth_radius / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = retain6(lat + dLat);
        double mgLon = retain6(lon + dLon);
        return new double[]{mgLat, mgLon};
    }

    /**
     * 高德/国测/谷歌 -> GPS
     *
     * @param lat 纬度
     * @param lon 经度
     * @return
     */
    public static double[] gcj02_To_Gps84(double lat, double lon) {
        double[] wgLoc = new double[2];
        double dLon = lon, dLat = lat;
        double dX, dY;
        double[] currGcLoc = new double[2];
        int maxCount = 100;
        int count = 0;
        while (true) {
            currGcLoc = gps84_To_Gcj02(dLat, dLon);
            dX = lon - currGcLoc[1];
            dY = lat - currGcLoc[0];
            if (Math.abs(dY) < 1e-5 && Math.abs(dX) < 1e-5) {
                // 1e-6 ~ centimeter level accuracy// Result of experiment://Most of the time 2 iterations would be enough for an 1e-8 accuracy (milimeter level).
                wgLoc[1] = new BigDecimal(dLon).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                wgLoc[0] = new BigDecimal(dLat).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                break;
            }
            dLon += dX;
            dLat += dY;
            if (count > maxCount) {//超过100次计算未满足条件，返回0
                wgLoc[0] = 0;
                wgLoc[1] = 0;
                break;
            }
            count++;
        }
        return wgLoc;
    }

    /**
     * 高德 -> 百度
     *
     * @param lat 纬度
     * @param lon 经度
     * @return [纬度, 经度]
     */
    public static double[] gcj02_To_Bd09(double lat, double lon) {
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        double[] gps = {tempLat, tempLon};
        return gps;
    }

    /**
     * 百度 -> 高德
     *
     * @param lat 纬度
     * @param lon 经度
     * @return [纬度, 经度]
     */
    public static double[] bd09_To_Gcj02(double lat, double lon) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta);
        double tempLat = z * Math.sin(theta);
        double[] gps = {tempLat, tempLon};
        return gps;
    }

    /**
     * GPS -> 百度
     *
     * @param lat 纬度
     * @param lon 经度
     * @return [纬度, 经度]
     */
    public static double[] gps84_To_bd09(double lat, double lon) {
        double[] gcj02 = gps84_To_Gcj02(lat, lon);
        double[] bd09 = gcj02_To_Bd09(gcj02[0], gcj02[1]);
        return bd09;
    }

    /**
     * 百度 -> GPS
     *
     * @param lat 纬度
     * @param lon 经度
     * @return [纬度, 经度]
     */
    public static double[] bd09_To_gps84(double lat, double lon) {
        double[] gcj02 = bd09_To_Gcj02(lat, lon);
        double[] gps84 = gcj02_To_Gps84(gcj02[0], gcj02[1]);
        //保留小数点后六位
        gps84[0] = retain6(gps84[0]);
        gps84[1] = retain6(gps84[1]);
        return gps84;
    }

    /**
     * 保留小数点后六位
     *
     * @param num
     * @return
     */
    private static double retain6(double num) {
        BigDecimal bd = BigDecimal.valueOf(num);
        BigDecimal nbd = bd.setScale(6, RoundingMode.HALF_UP);
        return nbd.doubleValue();
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     *
     * @param lng1
     * @param lat1
     * @param lng2
     * @param lat2
     * @return
     */
    public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * earth_radius;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static void main(String[] args) {
        //39.215816,117.15422
        double[] doubles = gps84_To_Gcj02(39.215816, 117.15422);
        System.out.println(doubles[0]);
        System.out.println(doubles[1]);
        double[] gcj02_to_gps84 = gcj02_To_Gps84(doubles[0], doubles[1]);
        System.out.println(gcj02_to_gps84[0]);
        System.out.println(gcj02_to_gps84[1]);

    }
}

