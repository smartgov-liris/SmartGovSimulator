package smartgov.models.lez.copert.fields;

public enum RandomField implements CopertField {
	RANDOM("Random");
	
	private String matcher;
	
	private RandomField(String matcher) {
		this.matcher = matcher;
	}
	
	public String matcher() {
		return matcher;
	}
}
