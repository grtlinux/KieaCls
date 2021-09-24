import org.tain.socket.ClsClientSocket;
import org.tain.utils.ClsProp;

public class ClsClientMain {

	public static void main(String[] args) throws Exception {
		System.out.println(">>>>> Cls Server start <<<<<");
		
		ClsProp.getInstance().printAll();
		
		if (ClsProp.getInstance().get("client.file").equals("")) {
			new ClsClientSocket().executeRes();
		} else {
			new ClsClientSocket().executeReq();
		}
	}
}
