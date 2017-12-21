package guodu.net.warning.entity;

public class GD_WX_INFOMATION {
    private String id;
    private String create_time;
    private String content;
    private String desmobile;
    private String warning_type;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
    
	public String getDesmobile() {
		return desmobile;
	}
	public void setDesmobile(String desmobile) {
		this.desmobile = desmobile;
	}
	public String getWarning_type() {
		return warning_type;
	}
	public void setWarning_type(String warning_type) {
		this.warning_type = warning_type;
	}
    
}
