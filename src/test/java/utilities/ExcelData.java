
package utilities;

import java.util.ArrayList;
import java.util.List;

public class ExcelData {

	public static List<String> LCHFEliminate = new ArrayList<>();
	public static List<String> LCHFAdd = new ArrayList<>();

	public static List<String> LCHFAllergyMilk = new ArrayList<>();
	public static List<String> LCHFAllergyNut = new ArrayList<>();

	public static List<String> LCHAvoid = new ArrayList<>();

	public static List<String> LFVEliminate = new ArrayList<>();
	public static List<String> LFVAdd = new ArrayList<>();
	public static List<String> LFVAllergyMilk = new ArrayList<>();
	public static List<String> LFVAllergyNut = new ArrayList<>();

	public static List<String> LFVAddnotfullyvegan = new ArrayList<>();
	public static List<String> LFVAvoid = new ArrayList<>();
	static String filePath = ".\\src\\test\\resources\\Data.xlsx";
	static ExcelReader readexcel = new ExcelReader(filePath);

	public static void LoadLCHFData() {
		LCHFEliminate = readexcel.readColumnFromExcel(0, 0);
		LCHFAdd = readexcel.readColumnFromExcel(1, 0);
		LCHFAllergyMilk = readexcel.readColumnFromExcel(4, 0);
		LCHFAllergyNut = readexcel.readColumnFromExcel(5, 0);
		LCHAvoid = readexcel.readColumnFromExcel(2, 0);

	}

	public static void LoadLFVData() {
		LFVEliminate = readexcel.readColumnFromExcel(0, 1);
		LFVAdd = readexcel.readColumnFromExcel(1, 1);
		LFVAllergyMilk = readexcel.readColumnFromExcel(5, 1);
		LFVAllergyNut = readexcel.readColumnFromExcel(6, 1);
		LFVAddnotfullyvegan = readexcel.readColumnFromExcel(2, 1);
		LFVAvoid = readexcel.readColumnFromExcel(3, 1);

	}

}
