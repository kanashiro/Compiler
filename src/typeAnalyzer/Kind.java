package typeAnalyzer;

public enum Kind {
	NO_KIND_DEF_(-1), VAR_(0), PARAM_(1), FUNCTION_(2), FIELD_(3), ARRAY_TYPE_(4), STRUCT_TYPE_(5), ALIAS_TYPE_(6), SCALAR_TYPE_(7), UNIVERSAL_(8);
	
	public int value;
	
	private Kind(int value) {
		this.value = value;
	}
	
	public boolean isTypeKind(){
		return (this.value == 4 || this.value == 5 || this.value == 6 || this.value == 7);
	}
}
