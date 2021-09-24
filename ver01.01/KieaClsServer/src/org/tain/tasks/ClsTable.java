package org.tain.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class ClsTable {

	public static void main(String[] args) throws Exception {
		final String datFile = "/Users/kang-air/KANG/cls_config/clsTable.dat";
		
		ClsTable clsTable = new ClsTable(datFile);
		clsTable.loadTable();
		clsTable.printTable();
		
		String res = clsTable.findRes("REQ002           ");
		System.out.println(">>>>> res: " + res);
		clsTable.appendRes(res);
	}
	
	///////////////////////////////////////////////////////
	
	private String datFile = null;
	private List<ReqRes> lstCls = null;
	
	public ClsTable(String datFile) throws Exception {
		this.datFile = datFile;
	}
	
	public void loadTable() throws Exception {
		this.lstCls = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(this.datFile));
		int lineNo = 0;
		String line = null;
		ReqRes dat = null;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.length() == 0
					|| line.charAt(0) == '#')
				continue;
			
			if (lineNo % 2 == 0) {
				dat = new ReqRes();
				dat.req = line;
			} else if (lineNo % 2 == 1) {
				dat.res = line;
				this.lstCls.add(dat);
			}
			
			lineNo ++;
		}
		
		br.close();
	}
	
	public void printTable() {
		int size = this.lstCls.size();
		ReqRes dat = null;
		for (int i=0; i < size; i++) {
			dat = this.lstCls.get(i);
			System.out.printf("(%d) %s\n", i, dat.req);
			System.out.printf("(%d) %s\n", i, dat.res);
			System.out.println();
		}
	}
	
	public String findRes(String req) {
		int size = this.lstCls.size();
		ReqRes dat = null;
		req = String.format("\"%s\"", req);
		for (int i=0; i < size; i++) {
			dat = this.lstCls.get(i);
			if (req.equals(dat.req)) {
				return dat.res;
			}
		}
		return null;
	}
	
	public void appendRes(String res) throws Exception {
		int len = res.length();
		res = res.substring(1, len-1) + "\n";
		
		final String resFile = "/Users/kang-air/KANG/cls_config/res.dat";
		File file = new File(resFile);
		RandomAccessFile raf = new RandomAccessFile(resFile, "rw");
		raf.seek(file.length());
		raf.writeChars(res);
		raf.close();
	}
}

class ReqRes {
	protected String req;
	protected String res;
}
