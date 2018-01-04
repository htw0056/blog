package com.htw.hadoop.study.chart5;

import java.util.Arrays;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;

public class MaxTemperatureMapperTest {
	@Test
	public void processesValidRecord() {
		Text value = new Text(
				"0043011990999991950051518004+68750+023550FM-12+0382" +
				// Year ^^^^
						"99999V0203201N00261220001CN9999999N9-00111+99999999999");
		// Temperature ^^^^^
		new MapDriver<LongWritable, Text, Text, IntWritable>()
				.withMapper(new MaxTemperatureMapper())
				.withInputKey(new LongWritable(1)).withInputValue(value)
				.withOutput(new Text("1950"), new IntWritable(-11)).runTest();
	}
	@Test
	public void returnMaximumIntegerInValues() {
		new ReduceDriver<Text, IntWritable, Text, IntWritable>()
				.withReducer(new MaxTemperatureReducer())
				.withInputKey(new Text("1950"))
				.withInputValues( Arrays.asList(new IntWritable(10), new IntWritable(5)))
				.withOutput(new Text("1950"), new IntWritable(10)).runTest();;
	}
}
