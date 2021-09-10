import org.tain.socket.ClsServerSocket;
import org.tain.utils.ClsProp;

public class ClsServerMain {

	public static void main(String[] args) throws Exception {
		System.out.println(">>>>> Cls Server start <<<<<");
		
		ClsProp.getInstance().printAll();
		
		new ClsServerSocket().execute();
	}
}
