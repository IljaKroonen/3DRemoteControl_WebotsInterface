package org.black_mesa.webots_remote_control.communication_structures;

import java.util.LinkedList;
import java.util.Queue;

import org.black_mesa.webots_remote_control.camera.ViewPointInfo;

/**
 * Stores a queue of actions sent by a client. This controller must execute these actions on a camera.
 * 
 * @author Ilja Kroonen
 * 
 */
public class CameraInstructionQueue extends CommunicationStructure {
	private static final long serialVersionUID = 228351533118850327L;
	private Queue<CameraInstruction> mQueue = new LinkedList<>();

	/**
	 * Instantiates the queue.
	 * 
	 * @param id
	 *            Id of the corresponding camera.
	 */
	public CameraInstructionQueue(final int id) {
		super(id);
	}

	private CameraInstructionQueue(final int id, final CameraInstructionQueue queue) {
		super(id);
		if (queue == null) {
			mQueue = new LinkedList<CameraInstruction>();
		} else {
			mQueue = new LinkedList<CameraInstruction>(queue.mQueue);
		}
	}

	/**
	 * Executes all the instructions in this queue.
	 * 
	 * @param vpi
	 *            ViewPointInfo to be updated.
	 */
	public final void execute(final ViewPointInfo vpi) {
		while (!mQueue.isEmpty()) {
			mQueue.poll().execute(vpi);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final CommunicationStructure board(final CommunicationStructure previous) {
		CameraInstructionQueue castedPrevious = (CameraInstructionQueue) previous;
		CameraInstructionQueue newQueue = new CameraInstructionQueue(getId(), castedPrevious);

		while (!mQueue.isEmpty()) {
			newQueue.mQueue.add(mQueue.poll());
		}

		return newQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean checkIntegrity() {
		return mQueue != null;
	}

	@Override
	public final String toString() {
		Queue<CameraInstruction> queueCopy = new LinkedList<CameraInstruction>(mQueue);
		String ret = "Queue of size " + mQueue.size();
		while (!queueCopy.isEmpty()) {
			ret += "\n" + queueCopy.poll().toString();
		}
		return ret;
	}
}