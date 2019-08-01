package simulator.entities;

import java.util.ArrayList;
import java.util.TreeMap;

import simulator.entities.switches.SDNSwitchv1;

public class Flow extends Entity {

	public TreeMap<Float, Float> ackSeqNumArrivalTimes; // <SeqNum, Time>
	public float arrivalTime; // in Simulator
	public float completionTime; // in Receiver Agent
	/** ========== Statistical Counters ========== **/
	public float dataSendingStartTime; // in Sender Agent

	/* Sequence Number */
	// in Sender Agent
	public TreeMap<Float, Float> dataSeqNumSendingTimes; // <SeqNum, Time>
	private int dstHostID;
	public float FINSendingTime;
	/** ========================================== **/

	/* Stores the Node IDs of the path */
	/* Initialized at the creation of the flow */
	public ArrayList<SDNSwitchv1> path = new ArrayList<SDNSwitchv1>();
	private int size;
	private int srcHostID;
	public float totalBufferTime; // in Buffer (when getting bufferTime)

	public int totalDroppedSegments; // in Buffer
	public int totalSentSegments; // in Sender Agent

	public float totalTransmissionTime;

	public Flow(int ID, int srcHostID, int dstHostID, int size, float arrivalTime) {
		super(ID);
		this.srcHostID = srcHostID;
		this.dstHostID = dstHostID;
		this.size = size;
		this.arrivalTime = arrivalTime;

		/** ========== Statistical Counters Initialization ========== **/
		dataSendingStartTime = 0;
		completionTime = 0;
		totalDroppedSegments = 0;
		totalSentSegments = 0;
		totalBufferTime = 0;
		FINSendingTime = 0;
		totalTransmissionTime = 0;

		dataSeqNumSendingTimes = new TreeMap<Float, Float>();
		ackSeqNumArrivalTimes = new TreeMap<Float, Float>();
		/** ========================================================= **/
	}

	public int getDstHostID() {
		return dstHostID;
	}

	/**********************************************************************/
	/********************** Getters and Setters ***************************/
	/**********************************************************************/

	public int getSize() {
		return size;
	}

	public int getSrcHostID() {
		return srcHostID;
	}

	public void updateCompletionTime(float completionTime) {
		this.completionTime = completionTime;
	}

	/*---------- Statistical counters methods ----------*/
	public void updateDataSendingStartTime(float startTime) {
		dataSendingStartTime = startTime;
	}

}