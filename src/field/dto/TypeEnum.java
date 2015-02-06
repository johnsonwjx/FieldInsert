package field.dto;

public enum TypeEnum {
	C {
		@Override
		public String getJavaType() {
			return "String";
		}
	},
	N {
		@Override
		public String getJavaType() {
			return "BigDecimal";
		}
	};
	public abstract String getJavaType();
}
