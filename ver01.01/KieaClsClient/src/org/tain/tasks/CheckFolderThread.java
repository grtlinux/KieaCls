package org.tain.tasks;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CheckFolderThread extends Thread {

	public static void main(String[] args) throws Exception {
		String dir = "/Users/kang-air/KANG/checkFolder/cli";
		String file = "resCls.dat";
		
		new CheckFolderThread(dir, file).start();
		
		System.out.println(">>>>> CheckFolderThread.....(cli)");
	}
	
	/////////////////////////////////////////////////////////////
	
	private String folderName = null;
	private String fileName = null;
	private File file = null;
	private RandomAccessFile raf = null;
	
	public CheckFolderThread(String folderName, String fileName) throws Exception {
		this.folderName = folderName;
		this.fileName = fileName;
		System.out.println(">>>>> dir: " + this.folderName);
		System.out.println(">>>>> file: " + this.fileName);
		
		this.file = new File(this.folderName + File.separator + this.fileName);
		file.delete();
		file.createNewFile();
		this.raf = new RandomAccessFile(this.file, "rw");
		this.raf.seek(0);
		this.raf.close();
	}
	
	public void run() {
		try {
			WatchService watchService = FileSystems.getDefault().newWatchService();
			Path path = Paths.get(this.folderName);
			path.register(watchService,
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY,
					StandardWatchEventKinds.ENTRY_DELETE);
			String line = null;
			long pointer = 0;
			while (true) {
				WatchKey watchKey = watchService.take();
				//WatchKey watchKey = watchService.poll(500, TimeUnit.MILLISECONDS);
				List<WatchEvent<?>> list = watchKey.pollEvents();
				for (WatchEvent<?> watchEvent : list) {
					Path pth = (Path)watchEvent.context();
					if (pth.getFileName().toString().equals(this.fileName)) {
						System.out.println(">>>>> " + pth.getFileName() + ", " 
								+ this.file.length() + ", "
								+ new Date(this.file.lastModified()) + ", ");
						this.raf = new RandomAccessFile(this.file, "rwd");
						this.raf.seek(pointer);
						line = this.raf.readLine();
						if (line != null) {
							System.out.println(">>>>> [" + line + "] " + this.raf.getFilePointer());
						}
						pointer = this.raf.getFilePointer();
						this.raf.close();
					}
				}
				
				if (!watchKey.reset())
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
