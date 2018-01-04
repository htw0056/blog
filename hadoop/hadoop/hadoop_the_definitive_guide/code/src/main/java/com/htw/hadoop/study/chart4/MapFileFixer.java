package com.htw.hadoop.study.chart4;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.SequenceFile;

public class MapFileFixer {
	public static void main(String[] args) throws Exception {
		String mapUri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(mapUri), conf);
		Path map = new Path(mapUri);
		Path mapData = new Path(map, MapFile.DATA_FILE_NAME);

		SequenceFile.Reader reader = new SequenceFile.Reader(fs, mapData, conf);
		Class keyClass = reader.getKeyClass();
		Class valueClass = reader.getValueClass();
		reader.close();

		long entries = MapFile.fix(fs, map, keyClass, valueClass, false, conf);
		System.out.printf("Create MapFile %s with %d entries\n", map, entries);

		
	}

}
