package org.tain.tasks;

import java.io.File;
import java.io.RandomAccessFile;

public class ResReader {

	public static void main(String[] args) throws Exception {
		String resDatFile = "/Users/kang-air/KANG/cls_config/res.dat";
		
		ResReader resReader = new ResReader(resDatFile);
		
		while (true) {
			String res = resReader.readLine();
			System.out.println(">>>>> res: [" + res + "]");
		}
	}
	
	//////////////////////////////////////////////////////////////
	
	private String resFile = null;
	private File file = null;
	private RandomAccessFile raf = null;
	private long position = -1;
	private long oldLastModified = -1;
	
	public ResReader(String resDatFile) throws Exception {
		this.resFile = resDatFile;
		this.file = new File(this.resFile);
		
		this.position = this.file.length();
		this.oldLastModified = this.file.lastModified();
	}
	
	public String readLine() throws Exception {
		String res = null;
		System.out.println(">>>>> position: " + this.position + ", oldLastModat: " + this.oldLastModified);
		while (true) {
			long lastModified = this.file.lastModified();
			if (lastModified != this.oldLastModified) {
				this.raf = new RandomAccessFile(this.resFile, "r");
				this.raf.seek(this.position);
				res = this.raf.readLine();
				this.position = this.raf.getFilePointer();
				this.oldLastModified = lastModified;
				this.raf.close();
				break;
			}
			
			try { Thread.sleep(1000); } catch (InterruptedException e) {}
		}
		
		return res;
	}
}
