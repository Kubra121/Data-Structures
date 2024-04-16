import java.io.File;

public class arrayNode {
	private String file;
	private int count;
	
	public arrayNode(String file, int count) {
		this.file = file;
		this.count = count;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
