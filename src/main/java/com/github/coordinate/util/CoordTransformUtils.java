package com.github.coordinate.util;

import java.util.HashMap;
import java.util.Map;

public class CoordTransformUtils {
	private static final double X_OFF_SET = -74.194299;
	private static final double Y_OFF_SET = 166.269575;
	private static final double Z_OFF_SET = 110.228380;
	
	private static final double X_ROTATE_ANGLE = 1.33537898212433;
	private static final double Y_ROTATE_ANGLE = -2.26705648546184;
	private static final double Z_ROTATE_ANGLE = 0.683375929577256;
	
	private static final double M_SCALE = -1.6336009E-05;
	
	private static final double WGS84_A_AXIS = 6378137;
	private static final double WGS84_B_AXIS = 6356752.314;

	private static final double BJ54_A_AXIS = 6378245;
	private static final double BJ54_B_AXIS = 6356863;
    
    public static Map<String,Object> BLHtoXYZ(double B, double L, double H) {
    	//第一步转换，大地坐标系换换成空间直角坐标系
        double dblD2R = Math.PI / 180;
        double e1 = Math.sqrt(Math.pow(WGS84_A_AXIS, 2) - Math.pow(WGS84_B_AXIS, 2)) / WGS84_A_AXIS;

        double N = WGS84_A_AXIS / Math.sqrt(1.0 - Math.pow(e1, 2) * Math.pow(Math.sin(B * dblD2R), 2));
        double targetX = (N + H) * Math.cos(B * dblD2R) * Math.cos(L * dblD2R);
        double targetY = (N + H) * Math.cos(B * dblD2R) * Math.sin(L * dblD2R);
        double targetZ = (N * (1.0 - Math.pow(e1, 2)) + H) * Math.sin(B * dblD2R);
        
        System.out.println("wsg84空间直角坐标系X坐标为:" + targetX + "   Y坐标为:" + targetY + "   Z坐标为:" + targetZ);
        
        //第二步转换，空间直角坐标系里七参数转换
        double Ex = X_ROTATE_ANGLE / 3600 / 180 * Math.PI;
        double Ey = Y_ROTATE_ANGLE / 3600 / 180 * Math.PI;
        double Ez = Z_ROTATE_ANGLE / 3600 / 180 * Math.PI;
        
        double newX = X_OFF_SET + M_SCALE * targetX + targetY * Ez - targetZ * Ey + targetX;
        double newY = Y_OFF_SET + M_SCALE * targetY - targetX * Ez + targetZ * Ex + targetY;
        double newZ = Z_OFF_SET + M_SCALE * targetZ + targetX * Ey - targetY * Ex + targetZ;
        
        System.out.println("wsg84空间直角坐标系里七参数转换后X坐标为:" + newX + "   Y坐标为:" + newY + "   Z坐标为:" + newZ);
       
        //第三步转换，空间直接坐标系转换为大地坐标系
        return XYZtoBLH(newX, newY, newZ);
        
    }
    
  //第三步转换，空间直接坐标系转换为大地坐标系
    private static Map<String,Object> XYZtoBLH(double X, double Y, double Z) {
        double e1 = (Math.pow(BJ54_A_AXIS, 2) - Math.pow(BJ54_B_AXIS, 2)) / Math.pow(BJ54_A_AXIS, 2);
        double e2 = (Math.pow(BJ54_A_AXIS, 2) - Math.pow(BJ54_B_AXIS, 2)) / Math.pow(BJ54_B_AXIS, 2);

        double S = Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2));
        double cosL = X / S;
        double B = 0;
        double L = 0;

        L = Math.acos(cosL);
        L = Math.abs(L);

        double tanB = Z / S;
        B = Math.atan(tanB);
        double c = BJ54_A_AXIS * BJ54_A_AXIS / BJ54_B_AXIS;
        double preB0 = 0.0;
        double ll = 0.0;
        double N = 0.0;
        //迭代计算纬度
        do {
            preB0 = B;
            ll = Math.pow(Math.cos(B), 2) * e2;
            N = c / Math.sqrt(1 + ll);

            tanB = (Z + N * e1 * Math.sin(B)) / S;
            B = Math.atan(tanB);
        }
        while (Math.abs(preB0 - B) >= 0.0000000001);

        ll = Math.pow(Math.cos(B), 2) * e2;
        N = c / Math.sqrt(1 + ll);

        double targetZ = Z / Math.sin(B) - N * (1 - e1);
        double targetB = B * 180 / Math.PI;
        double targetL = L * 180 / Math.PI;
        
        System.out.println("北京54大地坐标系经度为:"+ targetL +"   纬度为:" + targetB +"   高度为:" + targetZ);
        
        return gaussBLtoXY(targetB, targetL);
    }
    
    
    //第四部转换，高斯变换，大地坐标系转平面坐标系，84转54
    private static Map<String,Object> gaussBLtoXY(double mX,double mY){
        double m_aAxis = BJ54_A_AXIS; //参考椭球长半轴
        double m_bAxis = BJ54_B_AXIS; //参考椭球短半轴
        double m_dbMidLongitude = 114;//中央子午线经度 济南117 威海123 巴州 87
        double m_xOffset = 500000;
        double m_yOffset = 0.0;
        try{
               //角度到弧度的系数
                double dblD2R = Math.PI / 180;
                //代表e的平方
                double e1 = (Math.pow(m_aAxis, 2) - Math.pow(m_bAxis, 2)) / Math.pow(m_aAxis, 2);
                //代表e'的平方
                double e2 = (Math.pow(m_aAxis, 2) - Math.pow(m_bAxis, 2)) / Math.pow(m_bAxis, 2);
                //a0
                double a0 = m_aAxis * (1 - e1) * (1.0 + (3.0 / 4.0) * e1 + (45.0 / 64.0) * Math.pow(e1, 2) + (175.0 / 256.0) * Math.pow(e1, 3) + (11025.0 / 16384.0) * Math.pow(e1, 4));
                //a2                
                double a2 = -0.5 * m_aAxis * (1 - e1) * (3.0 / 4 * e1 + 60.0 / 64 * Math.pow(e1, 2) + 525.0 / 512.0 * Math.pow(e1, 3) + 17640.0 / 16384.0 * Math.pow(e1, 4));
                //a4
                double a4 = 0.25 * m_aAxis * (1 - e1) * (15.0 / 64 * Math.pow(e1, 2) + 210.0 / 512.0 * Math.pow(e1, 3) + 8820.0 / 16384.0 * Math.pow(e1, 4));
                //a6
                double a6 = (-1.0 / 6.0) * m_aAxis * (1 - e1) * (35.0 / 512.0 * Math.pow(e1, 3) + 2520.0 / 16384.0 * Math.pow(e1, 4));
                //a8
                double a8 = 0.125 * m_aAxis * (1 - e1) * (315.0 / 16384.0 * Math.pow(e1, 4));
            ////纬度转换为弧度表示
                //B
                double B = mX * dblD2R;
                //l
                double l = (mY - m_dbMidLongitude) * dblD2R;
                ////X
                double X = a0 * B + a2 * Math.sin(2.0 * B) + a4 * Math.sin(4.0 * B) + a6 * Math.sin(6.0 * B) + a8 * Math.sin(8.0 * B);
                //
                double ll = Math.pow(Math.cos(B), 2) * e2;
                double c = m_aAxis * m_aAxis / m_bAxis;
                //N
                double N = c / Math.sqrt(1 + ll);
                //t
                double t = Math.tan(B);
                double p = Math.cos(B) * l;
                double dby = X + N * t * (1 + ((5.0 - t * t + (9.0 + 4.0 * ll) * ll) + ((61.0 + (t * t - 58.0) * t * t + (9.0 - 11.0 * t * t) * 30.0 * ll) + (1385.0 + (-31111.0 + (543 - t * t) * t * t) * t * t) * p * p / 56.0) * p * p / 30.0) * p * p / 12.0) * p * p / 2.0;
                double dbx;
                dbx = N * (1.0 + ((1.0 - t * t + ll) + ((5.0 + t * t * (t * t - 18.0 - 58.0 * ll) + 14 * ll) + (61.0 + (-479.0 + (179.0 - t * t) * t * t) * t * t) * p * p / 42.0) * p * p / 20.0) * p * p / 6.0) * p;
                double mTargetY = dbx + m_xOffset;
                double mTargetX = dby + m_yOffset;
                
                System.out.println("最终平面坐标系X坐标为:" + mTargetX + "   Y坐标为:" + mTargetY);
                
                return XY4change(mTargetX, mTargetY);
        }
         catch (Exception ex) {
        	 
         }
		return null;
    }
    
    //最后一步,四参数转化
    private static Map<String,Object> XY4change(double x,double y){
//    	double DX = -1650461.328396,DY = -513095.213704,T = 0.0509864374,K = 1.512892312411;
        double DX = -3000000,DY = 0,T = 0,K = 0;
        double Xt;
        double Yt;
        if(T == 0 && K == 0){
            Xt = x + DX;
            Yt = y + DY;
        }else{
            Xt = DX + K*x*Math.cos(T) - K*y*Math.sin(T);
            Yt = DY + K*x*Math.sin(T) + K*y*Math.cos(T);
        }
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	resultMap.put("X", Xt);
    	resultMap.put("Y", Yt);
    	
    	System.out.println("Xt:" + Xt + "Yt:" + Yt);
    	return  resultMap;
    }

    public static void main(String[] args) {
        BLHtoXYZ(30.37535759,114.0750103,0);
    }

}
