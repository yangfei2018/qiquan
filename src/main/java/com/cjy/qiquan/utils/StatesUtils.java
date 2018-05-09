package com.cjy.qiquan.utils;


public abstract class StatesUtils {

	public static class States{
		public static final String empty_upload_file					= "0010";
		public static final String upload_file_format_error				= "0011";
		public static final String upload_file_error					= "0012";
		public static final String params_error							= "0005";
		public static final String success								= "0000";
		public static final String database_error						= "0020";
		public static final String server_error							= "0099";
		public static final String not_found							= "0299";
		public static final String forbidden_user					    = "0199";
		public static final String not_login							= "1099";
		public static final String not_guide							= "1009";
		public static final String not_validate							= "1019";
		public static final String not_master							= "1020";
		public static final String not_partner							= "1021";
	}	
}
