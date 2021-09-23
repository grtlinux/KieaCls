package org.tain.tasks;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class CheckFolderThread extends Thread {

	public static void main(String[] args) throws Exception {
		String dir = "/Users/kang-air/KANG/checkFolder/svr";
		
		new CheckFolderThread(dir).start();
		
		System.out.println(">>>>> CheckFolderThread.....(svr)");
	}
	
	/////////////////////////////////////////////////////////////
	
	private String folderName = null;
	
	public CheckFolderThread(String folderName) {
		this.folderName = folderName;
		System.out.println(">>>>> " + this.folderName);
	}
	
	public void run() {
		
		try {
			WatchService watchService = FileSystems.getDefault().newWatchService();
			Path path = Paths.get(this.folderName);
			path.register(watchService,
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY,
					StandardWatchEventKinds.ENTRY_DELETE);
			while (true) {
				WatchKey watchKey = watchService.take();
				List<WatchEvent<?>> list = watchKey.pollEvents();
				for (WatchEvent<?> watchEvent : list) {
					Kind<?> kind = watchEvent.kind();
					Path pth = (Path) watchEvent.context();
					if (pth.getFileName().toString().charAt(0) == '.') {
						System.out.println(">>>>> hidden: " + pth.getFileName());
					} else if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
						// 파일이 생성되었을 때 실행되는 코드
						System.out.println(">>>>> CREATE: " + pth.getFileName());
					} else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
						// 파일이 삭제되었을 때 실행되는 코드
						System.out.println(">>>>> DELETE: " + pth.getFileName());
					} else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
						// 파일이 수정되었을 때 실행되는 코드
						System.out.println(">>>>> MODIFY: " + pth.getFileName());
					} else if (kind.equals(StandardWatchEventKinds.OVERFLOW)) {
						// 운영체제에서 이벤트가 소실되었거나 버려질 경우에 발생되는코드
						System.out.println(">>>>> OVERFLOW ");
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
