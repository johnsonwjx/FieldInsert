package field;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import field.dto.IngoreTable;
import field.dto.TableDto;

public class Global {
	public static FieldMain MAIN = null;
	public static List<IngoreTable> ingoreTables = null;

	public static void clear() {
		MAIN = null;
		ingoreTables = null;
		innitSort.clear();
		strutSort.clear();
		removes.clear();
	}

	public static final int BOTTOM_HEIGHT = 20;

	public static final String TEMP_FILE = "src" + File.separator + "field" + File.separator + "temp" + File.separator + "temp.xml";

	public static final String TABLE_TAG = "table";
	public static final String TABLE_NAME_TAG = "table_name";
	public static final String TABLE_DESC_TAG = "table_desc";

	public static final String FIELD_TAG = "field";
	public static final String FIELD_NAME_TAG = "field_name";
	public static final String FIELD_DESC_TAG = "field_desc";
	public static final String FIELD_TYPE_TAG = "field_type";
	public static final String FIELD_DEC_TAG = "field_dec";

	public static final String FIELD_NULL_TAG = "field_null";
	public static final String FIELD_LENGTH_TAG = "field_length";
	public static final String FIELD_DEFAULT_TAG = "field_default";
	public static final String PARA_TAG = "para";

	public static final String INDEX_TAG = "indexs";
	public static final String INDEX_NAME_TAG = "name";
	public static final String INDEX_FIELDS_TAG = "fields";
	public static final String INDEX_TYPE_TAG = "type";

	public static String Encoding_initData = "UTF-8";
	public static String Encoding_Strut = "UTF-8";
	public static String Encoding_java = "UTF-8";

	public static final String STRUT_TABLE_TAG = "table";
	public static final String STRUT_TABLENAME_TAG = "name";
	public static final String STRUT_TABLECNAME_TAG = "cname";
	public static final String STRUT_FATHERTABLENAME_TAG = "fathertablename";
	public static final String STRUT_MODE_TAG = "mode";
	public static final String STRUT_AUTHMODE_TAG = "authmode";
	public static final String STRUT_KEYFIELDNAME_TAG = "keyfieldname";
	public static final String STRUT_GROUPKEYFIELD_TAG = "groupkeyfield";

	public static final String STRUT_FIELD_TAG = "field";
	public static final String STRUT_FIELDNAME_TAG = "name";
	public static final String STRUT_FIELDCNAME_TAG = "cname";
	public static final String STRUT_FIELDFORIGNKEY_TAG = "forignkey";
	public static final String STRUT_FIELDTYPE_TAG = "type";
	public static final String STRUT_FIELDFIELDTYPE_TAG = "fieldType";

	public static List<String> innitSort = new ArrayList<String>();
	public static List<String> strutSort = new ArrayList<String>();
	public static List<TableDto> removes = new LinkedList<TableDto>();

	public static String cover_node_lowercase(String node) {
		return "translate(name(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='" + node.toLowerCase() + "'";
	}

	public static String cover_attr_lowercase(String attr,String value) {
		return "translate(@" + attr + ",'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='" + value.toLowerCase() + "'";
	}
}
