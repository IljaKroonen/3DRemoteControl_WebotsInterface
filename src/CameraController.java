import org.black_mesa.webots_remote_control.camera.ViewPointInfo;
import org.black_mesa.webots_remote_control.communication_structures.CameraInstruction;
import org.black_mesa.webots_remote_control.communication_structures.CameraInstructionQueue;
import org.black_mesa.webots_remote_control.server.Server;

import com.cyberbotics.webots.controller.Supervisor;
import com.cyberbotics.webots.controller.Camera;

/**
 * Controller class for a camera.
 * 
 * @author Ilja Kroonen
 * 
 */
public class CameraController extends Supervisor {
	private static final int DEFAULT_PORT = 42511;
	private static final int TIMESTEP = 32;

	private Camera mCamera;
	private Server mServer;

	/**
	 * Instantiates the controller.
	 * 
	 * @param port
	 *            The port the server will listen on.
	 */
	public CameraController(final int port) {
		super();
		CameraInstructionQueue initialState = new CameraInstructionQueue(0);
		mServer = new Server(port, initialState);
		mCamera = this.getCamera("camera");
		mCamera.enable(TIMESTEP);
	}

	/**
	 * Run method for this controller. This method loops around calls to the step method, so that the content of the
	 * loop is executed each TIMESTEP ms.
	 */
	public final void run() {
		double[] t = getFromDef("CAMERA").getField("translation").getSFVec3f();
		double[] r = getFromDef("CAMERA").getField("rotation").getSFRotation();
		ViewPointInfo vpi = new ViewPointInfo(t, r);
		while (step(TIMESTEP) != -1) {
			CameraInstructionQueue currentState = mServer.getCamera();
			// Application de la translation et de la rotation
			t = getFromDef("CAMERA").getField("translation").getSFVec3f();
			r = getFromDef("CAMERA").getField("rotation").getSFRotation();
			vpi.update(t, r);
			currentState.execute(vpi);
			getFromDef("CAMERA").getField("translation").setSFVec3f(vpi.getT());
			getFromDef("CAMERA").getField("rotation").setSFRotation(vpi.getR());
		}
	}

	/**
	 * Main method. Instantiates the controller and starts it. First argument is the port number.
	 * 
	 * @param args
	 *            Arguments passed to the controller.
	 */
	public static void main(final String[] args) {
		int port;
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			port = DEFAULT_PORT;
		}
		CameraController controller = new CameraController(port);
		controller.run();
		@SuppressWarnings("unused")
		CameraInstruction webotsPleaseCompileThisClass;
	}
}