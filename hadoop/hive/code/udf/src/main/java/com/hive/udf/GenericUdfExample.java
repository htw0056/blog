package com.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.IntObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/*
 * 相对于UDF来说GenericUDF能处理更多的高级数据类型:list,map,struct等
 * 这是一篇很好的参考:http://www.baynote.com/2012/11/a-word-from-the-engineers/
 * 
 */
public class GenericUdfExample extends GenericUDF {

	public StringObjectInspector stringOI;
	public IntObjectInspector intOI;
	/*
	 * 初始化:用来检查输入变量数量、类型等信息，返回值为udf返回结果类型
	 */
	@Override
	public ObjectInspector initialize(ObjectInspector[] arguments)
			throws UDFArgumentException {
		// 检查数量
		if (arguments.length != 2) {
			throw new UDFArgumentLengthException("usage: 2 arguments: S, T");
		}
		ObjectInspector a = arguments[0];
		ObjectInspector b = arguments[1];

		// 检查输入类型
		if (!(a instanceof StringObjectInspector)
				|| !(b instanceof IntObjectInspector)) {
			throw new UDFArgumentException(
					"first argument must be a string, second argument must be a int");
		}

		// 将类型信息保存，供evaluate使用(不保存也没关系)
		this.stringOI = (StringObjectInspector) a;
		this.intOI = (IntObjectInspector) b;

		// udf返回Text类型结果,所以这边也返回相应的结果
		return PrimitiveObjectInspectorFactory.writableStringObjectInspector;
		// udf返回String类型结果,所以这边也返回相应的结果
		// return PrimitiveObjectInspectorFactory.javaStringObjectInspector;
	}

	@Override
	public Object evaluate(DeferredObject[] arguments) throws HiveException {
		// 这里其实有java类型和writable类型之分
		String s = this.stringOI.getPrimitiveJavaObject(arguments[0].get());
		// Text s= this.stringOI.getPrimitiveWritableObject(arguments[0].get());

		IntWritable i = (IntWritable) this.intOI
				.getPrimitiveWritableObject(arguments[1].get());

		if (s == null || i == null) {
			return null;
		}
		return new Text(s + i);
	}

	/*
	 * hive中在使用explain的时候调用该方法
	 */
	@Override
	public String getDisplayString(String[] arg0) {
		return "genericudf";
	}

}
