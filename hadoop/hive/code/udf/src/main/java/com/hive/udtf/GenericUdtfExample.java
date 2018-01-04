package com.hive.udtf;

import java.util.ArrayList;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.Text;

/*
 * GenericUDTF
 * 将array<string> 转为多行 string
 * 
 */
public class GenericUdtfExample extends GenericUDTF {

	private ListObjectInspector loi;
	private StringObjectInspector soi;
	@Override
	public void close() throws HiveException {
		// TODO Auto-generated method stub

	}

	@Override
	public StructObjectInspector initialize(ObjectInspector[] arguments)
			throws UDFArgumentException {
		if (arguments.length != 1) {
			throw new UDFArgumentException("accept one argument");
		}
		if (arguments[0].getCategory() != ObjectInspector.Category.LIST) {
			throw new UDFArgumentTypeException(0, "new list");
		}

		loi = (ListObjectInspector) arguments[0];
		ObjectInspector oi = loi.getListElementObjectInspector();

		if (oi.getCategory() != ObjectInspector.Category.PRIMITIVE) {
			throw new UDFArgumentTypeException(0,
					"need input array<primitive>");
		}
		if (((PrimitiveObjectInspector) oi)
				.getPrimitiveCategory() != PrimitiveObjectInspector.PrimitiveCategory.STRING) {
			throw new UDFArgumentTypeException(0, "need array<string>");
		}
		soi = (StringObjectInspector) oi;

		ArrayList<String> structFieldNames = new ArrayList<String>();
		ArrayList<ObjectInspector> structFieldObjectInspectors = new ArrayList<ObjectInspector>();

		structFieldNames.add("col1");
		structFieldObjectInspectors.add(
				PrimitiveObjectInspectorFactory.writableStringObjectInspector);
		return ObjectInspectorFactory.getStandardStructObjectInspector(
				structFieldNames, structFieldObjectInspectors);

	}

	@Override
	public void process(Object[] arguments) throws HiveException {
		if (arguments[0] == null) {
			return;
		}
		int aleng = loi.getListLength(arguments[0]);
		for (int i = 0; i < aleng; i++) {
			Text s = soi.getPrimitiveWritableObject( loi.getListElement(arguments[0], i));
			Object[] o = new Object[1];
			o[0] = s;
			forward(o);
		}

	}

}
