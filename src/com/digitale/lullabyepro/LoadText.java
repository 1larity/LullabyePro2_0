package com.digitale.lullabyepro;

import android.util.Log;

public class LoadText {

	private static String tag = "LoadText";

	public static String createLoadText(String input) {
		// clean string for comparison purposes (remove comma and numbers
		String testString = input.replaceAll("[0-9]+|(,)", "");
		if (Lullabye.debug)
			Log.d(tag, "teststring " + testString);
		// clean string for output (remove id number and comma)
		String output = input.replaceAll("(.*,)", "");
		if (Lullabye.debug)
			Log.d(tag, "Pre-output " + testString);
		// concatinate
		if (testString.equals("sheep")) {
			output = "Shearing " + testString;
		} else if (testString.equals("cuckoo")) {
			output = "Calling " + output;

		} else if (testString.equals("cow")) {
			output = "Tipping " + output;
		} else if (testString.equals("dog")) {
			output = "Walking " + output;
		} else if (testString.equals("duck")) {
			output = "Squeezing " + output;
		} else if (testString.equals("lakewaves")) {
			output = "Making " + output;
		} else if (testString.equals("wind")) {
			output = "Passing " + output;
		} else if (testString.contains("noise")) {
			output = "Making " + output;
		} else if (testString.contains("nightingale")) {
			output = "Singing " + output;
		} else if (testString.contains("thrush")) {
			output = "Thrashing " + output;
		} else if (testString.contains("loons")) {
			output = "Diving " + output;
		} else if (testString.contains("crow")) {
			output = "Counting " + output;
		} else if (testString.equals("roarfire")) {
			output = "Setting  " + output;
		} else if (testString.equals("softfire")) {
			output = "Lighting " + output;
		} else if (testString.equals("treewind")) {
			output = "Blowing " + output;
		} else if (testString.equals("riverbubbly")) {
			output = "Running " + output;
		} else if (testString.equals("softrain")) {
			output = "Pouring " + output;
		} else if (testString.equals("waves")) {
			output = "Making " + output;
		} else if (testString.equals("pigs")) {
			output = "Porking " + output;
		}
		output = output + ".";
		return output;
	}
}
