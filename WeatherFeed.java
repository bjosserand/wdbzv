package com.blj.misc;

//import com.google.gson.Gson;
//import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WeatherFeed {

	private String title = "";
	private int titleLineCols = 1;
	private int typesLineCols = 8;
	private int headingLine = 2;
	private int dataCols = 20;
	private Map<Integer, FieldMeta> metaMap;

	private int smallestTempDiff = 1000;
	private String daySmallestTempDiff;
	private float totalPrecip = 0;

	public static void main(String[] args) {
		WeatherFeed obj = new WeatherFeed();

		if(args.length < 1) {
			System.out.println("1st argument of file to process is required");
			System.exit(1);
		}
		obj.doReads(args[0]);

//		FileMeta fileMeta = obj.getFileMetadata();
	}

//	public FileMeta getFileMetadata() {
//		FileMeta fileMeta = null;
//		try (JsonReader jsonReader = new JsonReader(new FileReader("/Users/blj/BZWD/fileDef.json")) )
//		{
//			Gson gson = new Gson();
//			fileMeta = gson.fromJson(jsonReader, FileMeta.class);
//
//		}
//		catch(IOException ioe) {
//			ioe.printStackTrace();
//		}
//		return fileMeta;
//	}

	public BufferedReader getReader(String fileName) {

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			return br;
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		return null;
	}

	public void doReads(String fileName) {
		String[] priorResult = null;
		int numCols = 20;
		BufferedReader br = this.getReader(fileName);
		String[] readResult;

		String[] types = new String[typesLineCols];

		int dataLineCnt = 0;
		int lineCnt = 0;
		while( (readResult = this.readLine(br)) != null) {
			if( (lineCnt == 0) && (readResult.length == this.titleLineCols) ) {
				this.title = readResult[0];
			}
			else if ( (lineCnt == 1) && (readResult.length - 1 == this.typesLineCols)) {
				types = readResult;
			}

			else if( (lineCnt == 2) && (readResult.length - 1 == this.dataCols)) {
				this.metaMap = this.setupFieldMD(types, readResult);
			}
			else {
				if(!this.validateLine(readResult)) {
					System.out.println("Invalid input!!!!");
					break;
				}
				this.processSingleLine(readResult);
//				if(priorResult != null) {
//					this.processTwoLines(readResult, priorResult);
//				}
				dataLineCnt++;
			}

			priorResult = readResult;
			lineCnt++;
		}

		System.out.println("smallest temp diff was: " + this.smallestTempDiff + ", on day: " + this.daySmallestTempDiff);
		System.out.printf("precip total: %.2f: ", this.totalPrecip);

	}

//	private void processTwoLines(String[] readResult, String[] priorLine) {
//
//	}

	private void processSingleLine(String[] readResult) {

		int tempDiff = this.getTempDiff(readResult[1], readResult[3], this.metaMap.get(1), this.metaMap.get(3));
		if(tempDiff < this.smallestTempDiff) {
			this.smallestTempDiff = tempDiff;
			this.daySmallestTempDiff = readResult[0];
		}
		System.out.println("Temp diff for day: " + readResult[0] + ", is:  " + tempDiff);

		this.totalPrecip += getPrecip(readResult[19]);
	}

	private float getPrecip(String precipToday) {
		float precip = 0;
		if(precipToday.equalsIgnoreCase("T"))
			return precip;

		precip = Float.parseFloat(precipToday);
		return precip;
	}

	private int getTempDiff(String high, String low, FieldMeta highMeta, FieldMeta lowMeta) {
		int result = 0;
		int hi = Integer.parseInt(high);
		int lo = Integer.parseInt(low);

		if(highMeta.getType().equalsIgnoreCase("TEMPC")) {
			hi = this.getFarenheitValue(hi);
		}

		if(lowMeta.getType().equalsIgnoreCase("TEMPC")) {
			lo = this.getFarenheitValue(lo);
		}

		if(lo > hi)			// a low temp should not be higher than a high temp
			return 0;

		if(hi > 0) {
			if(lo > 0) {		// both positive
				result = hi - lo;
			}
			else {				// low is negative
				result = hi + Math.abs(lo);
			}
		}
		else {					// both negative
			result = Math.abs(lo) - Math.abs(hi);
		}

		return result;
	}

	private int getFarenheitValue(int celciusVaue) {
		Double tmpVal  = (celciusVaue * 9/5.0) +32;

		return tmpVal.intValue();
	}


	private boolean validateLine(String[] readResult) {
		return true; 	// temp
	}

	private Map<Integer, FieldMeta> setupFieldMD(String[] types, String[] headings) {
		Map<Integer, FieldMeta> retVal = new HashMap<>();
		for(int i=0, j=0; i<types.length; i++) {
			if(i == 0) {
				FieldMeta fieldMeta = new FieldMeta("MONTH", j, "TEXT", "TEXT","0", "3000", "IGNORE");

				retVal.put(new Integer(j++), fieldMeta);
			}
			else if(i == 7) {
				FieldMeta fieldMeta = new FieldMeta("PRECIP", j, "FLOAT", "NUM","0", "1000", "IGNORE");

				retVal.put(new Integer(j++), fieldMeta);
			}
			else if(i == 8) {
				FieldMeta fieldMeta = new FieldMeta("EVENTS", j, "TEXT", "TEXT", "0", "1000", "IGNORE");
				fieldMeta.setName("EVENTS");
				fieldMeta.setLowRange("0");
				fieldMeta.setHighRange("1000");
				fieldMeta.setType("TEXT");

				retVal.put(new Integer(j++), fieldMeta);
			}
			else
			{
				String dataType = this.getDataType(types[i]);

				TypeRange typeRange = this.getTypeRange(dataType);

				FieldMeta fieldMeta = new FieldMeta("HIGH", j, typeRange.getType(), typeRange.getDataType(), typeRange.getLowRange(), typeRange.getHighRange(), "IGNORE");
				retVal.put(new Integer(j++), fieldMeta);

				fieldMeta = new FieldMeta("AVG", j, typeRange.getType(), typeRange.getDataType(), typeRange.getLowRange(), typeRange.getHighRange(), "IGNORE");
				retVal.put(new Integer(j++), fieldMeta);

				fieldMeta = new FieldMeta("LOW", j, typeRange.getType(), typeRange.getDataType(), typeRange.getLowRange(), typeRange.getHighRange(), "IGNORE");
				retVal.put(new Integer(j++), fieldMeta);
			}
		}

		return retVal;
	}

	private TypeRange getTypeRange(String type) {
		String actualType = "UNKNOWN";
		String lowRange = "0";
		String highRange = "1000";
		String dataType = "TEXT";
		String tempFaren = "°F";
		String tempCel = "°C";

		if(type.equals(tempFaren)) {
			actualType = "TEMPF";
			lowRange = "-100";
			highRange = "160";
			dataType = "INT";
		}
		if(type.equals(tempCel)) {
			actualType = "TEMPC";
			lowRange = "-70";
			highRange = "120";
			dataType = "INT";
		}
		if(type.equals("in")) {
			actualType = "INCHES";
			lowRange = "0";
			highRange = "100";
			dataType = "FLOAT";
		}
		if(type.equals("mi")) {
			actualType = "MILES";
			lowRange = "0";
			highRange = "10000";
			dataType = "INT";
		}
		if(type.equals("mph")) {
			actualType = "MPH";
			lowRange = "0";
			highRange = "1000";
			dataType = "INT";
		}

		TypeRange typeRange = new TypeRange(actualType, highRange, lowRange, dataType);

		return typeRange;
	}

	private String getDataType(String input) {
		int leftPos = input.indexOf('(');
		int rightPos = input.indexOf(')', leftPos);

		if( (rightPos == 0) && (leftPos == 0) )
			return "UNDETERMINED";
		if(rightPos < leftPos)
			return "UNDETERMINED";

		String dataType = input.substring(++leftPos, rightPos);
		return dataType;
	}

	public String[] readLine(BufferedReader br) {
		String line;
		String[] lines = null;

		try {
			line = br.readLine();

			if(line != null) {
				int holderCnt = 0;
				lines = line.split("\t");

//				for(int i=0; i<lines.length; i++) {
//					System.out.println("i: " + i + ", contains: " + lines[i]);
//				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

}
