import org.tain.socket.ClsClientSocket;
import org.tain.utils.ClsProp;

public class ClsClientMain {

	public static void main(String[] args) throws Exception {
		System.out.println(">>>>> Cls Server start <<<<<");
		
		ClsProp.getInstance().printAll();
		
		new ClsClientSocket().execute();
	}
}
