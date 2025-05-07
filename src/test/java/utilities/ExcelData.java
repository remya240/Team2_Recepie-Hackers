package utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelData {

	public static List<String> LCHEliminate= new ArrayList<String>();
	public static List<String> LCHAdd = new ArrayList<String>();
	public static List<String> LCHAvoid = new ArrayList<String>();
	public static List<String> LCHFoodProcessing = new ArrayList<String>();
	
	public static List<String> LFVEliminate = new ArrayList<String>();
	public static List<String> LFVAdd = new ArrayList<String>();
	public static List<String> LFVAddnotfullyvegan = new ArrayList<String>();
	public static List<String> LFVAvoid = new ArrayList<String>();
	public static List<String> LFVOptinalRecipe = new ArrayList<String>();
	
	public static List<String> AllergiesToFilter = new ArrayList<String>();
	public static List<String> MilkAllergy = new ArrayList<String>();
	public static List<String> NutAllergy = new ArrayList<String>();
	
	//static ExcelReader excelReader;
	static String filePath=".\\src\\test\\resources\\Data.xlsx";
	static ExcelReader readexcel = new ExcelReader(filePath);
	
	public static void LoadLCHData()
	{
		LCHEliminate = readexcel.readColumnFromExcel(0,0);
		LCHAdd = readexcel.readColumnFromExcel(1,0);
		LCHAvoid = readexcel.readColumnFromExcel(2,0);
		LCHFoodProcessing = readexcel.readColumnFromExcel(3,0);
	
	}
	
	public static void LoadLFVData()
	{
		LFVEliminate = readexcel.readColumnFromExcel(0,1);
		LFVAdd = readexcel.readColumnFromExcel(1,1);
		LFVAddnotfullyvegan =  readexcel.readColumnFromExcel(2,1);
		LFVAvoid = readexcel.readColumnFromExcel(3,1);
		LFVOptinalRecipe = readexcel.readColumnFromExcel(4,1);
	}
	
    public static void LoadAllergyData() {
    	MilkAllergy = readexcel.readColumnFromExcel(0,2);
		NutAllergy = readexcel.readColumnFromExcel(0,3);
    	
    }
    
}
