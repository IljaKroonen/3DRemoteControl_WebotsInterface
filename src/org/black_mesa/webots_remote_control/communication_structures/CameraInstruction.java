package org.black_mesa.webots_remote_control.communication_structures;

import java.io.Serializable;

import org.black_mesa.webots_remote_control.camera.ViewPointInfo;
import org.black_mesa.webots_remote_control.geometry.Geometry;

/**
 * Instruction class for a Webots camera.
 * 
 * @author Ilja Kroonen
 * 
 */
public class CameraInstruction implements Serializable {
	private static final long serialVersionUID = -1401919642170517372L;
	private final Type mType = null;
	private final double[] mArgs = null;

	private enum Type {
		MOVE, TURN, PITCH
	}

	/**
	 * Executes the instruction on a ViewPointInfo structure.
	 * 
	 * @param vpi
	 *            The structure to be updated.
	 */
	public final void execute(final ViewPointInfo vpi) {
		switch (mType) {
		case MOVE:
			System.out.println(mType + "(" + mArgs[0] + "," + mArgs[1] + "," + mArgs[2] + ")");
			move(vpi);
			break;
		case TURN:
			System.out.println(mType + "(" + mArgs[0] + ")");
			turn(vpi);
			break;
		case PITCH:
			System.out.println(mType + "(" + mArgs[0] + ")");
			pitch(vpi);
			break;
		}
	}

	private void move(final ViewPointInfo vpi) {
		mArgs[2] = -mArgs[2];
		double[] r = vpi.getR();
		double[][] mat = Geometry.axisAngleToMatrix(vpi.getR());
		double[] newT = Geometry.rotate3DVectorMatrix(mArgs, mat);
		double[] t = Geometry.add3DVector3DVector(vpi.getT(), newT);
		vpi.update(t, r);
	}

	private void turn(final ViewPointInfo vpi) {
		double[] rotation = { 0, 1, 0, -mArgs[0] };
		double[] t = vpi.getT();
		double[] r = Geometry.composeAxisAngleAxisAngle(rotation, vpi.getR());
		vpi.update(t, r);
	}

	private void pitch(final ViewPointInfo vpi) {
		double[] rotation = { 1, 0, 0, -mArgs[0] };
		rotation[0] = 1;
		rotation[1] = 0;
		rotation[2] = 0;
		rotation[3] = -mArgs[0];
		double[] r = Geometry.composeAxisAngleAxisAngle(vpi.getR(), rotation);
		double[] t = vpi.getT();
		vpi.update(t, r);
	}

	@Override
	public final String toString() {
		switch (mType) {
		case MOVE:
			return mType + "(" + mArgs[0] + "," + mArgs[1] + "," + mArgs[2] + ")";
		case TURN:
			return mType + "(" + mArgs[0] + ")";
		case PITCH:
			return mType + "(" + mArgs[0] + ")";
		}
		return null;
	}
}