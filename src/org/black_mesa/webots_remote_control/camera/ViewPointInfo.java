package org.black_mesa.webots_remote_control.camera;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains information about a ViewPoint (or camera). This includes its position, and previously saved positions.
 * 
 * @author Ilja Kroonen
 * 
 */
public class ViewPointInfo {
	private final State mState;
	private final List<State> mSavedStates = new ArrayList<State>();

	/**
	 * Instantiates the ViewPointInfo.
	 * 
	 * @param t
	 *            Position of the camera (3D vector).
	 * @param r
	 *            Orientation of the camera (axis-angle representation).
	 */
	public ViewPointInfo(final double[] t, final double[] r) {
		mState = new State(t, r);
	}

	/**
	 * Updates the structure.
	 * 
	 * @param t
	 *            New position of the camera (3D vector).
	 * @param r
	 *            New orientation of the camera (axis-angle representation).
	 */
	public final void update(final double[] t, final double[] r) {
		State tmp = new State(t, r);
		mState.set(tmp);
	}

	/**
	 * Getter for the position.
	 * 
	 * @return Current position of the camera (3D vector).
	 */
	public final double[] getT() {
		return mState.getT();
	}

	/**
	 * Getter for the orientation.
	 * 
	 * @return Current orientation of the camera (axis-angle representation).
	 */
	public final double[] getR() {
		return mState.getR();
	}

	/**
	 * Saves the current state, and returns its id for a later call to restoreState().
	 * 
	 * @return Id of the saved state.
	 */
	public final int saveState() {
		State newState = mState.clone();
		mSavedStates.add(newState);
		return mSavedStates.indexOf(newState);
	}

	/**
	 * Restores a state previously saved with saveState().
	 * 
	 * @param id
	 *            The identifier of the state previously returned by saveState().
	 */
	public final void restoreState(final int id) {
		mState.set(mSavedStates.get(id));
	}

	private class State {
		private final double[] mT;
		private final double[] mR;

		public State(final double[] t, final double[] r) {
			mT = t;
			mR = r;
		}

		public void set(final State state) {
			for (int i = 0; i < 3; i++) {
				mT[i] = state.getT()[i];
			}
			for (int i = 0; i < 4; i++) {
				mR[i] = state.getR()[i];
			}
		}

		private double[] getT() {
			return mT;
		}

		private double[] getR() {
			return mR;
		}

		@Override
		public State clone() {
			return new State(mT.clone(), mR.clone());
		}
	}
}