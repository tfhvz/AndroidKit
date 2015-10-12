package com.snicesoft.basekit.util;

import java.text.DecimalFormat;

public class MathUtils {
	public static String getPercent(double x, double total) {
		String result = "";// 接受百分比的值
		double tempresult = x / total;
		DecimalFormat df1 = new DecimalFormat("0%"); // ##.00%
		result = df1.format(tempresult);
		return result;
	}
}
