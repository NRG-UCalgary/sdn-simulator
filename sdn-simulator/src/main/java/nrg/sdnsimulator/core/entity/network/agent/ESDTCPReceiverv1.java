package nrg.sdnsimulator.core.entity.network.agent;

import nrg.sdnsimulator.core.Network;
import nrg.sdnsimulator.core.entity.network.Agent;
import nrg.sdnsimulator.core.entity.traffic.Flow;
import nrg.sdnsimulator.core.entity.traffic.Segment;
import nrg.sdnsimulator.core.system.SimApp;
import nrg.sdnsimulator.core.utility.Debugger;
import nrg.sdnsimulator.core.utility.Keywords;

public class ESDTCPReceiverv1 extends Agent {

	// indicates the sequence number of the latest received ACK
	private int ACKNum;

	public ESDTCPReceiverv1(Flow flow) {
		super(flow);
		srcHostID = flow.getSrcHostID();
		dstHostID = flow.getDstHostID();
		ACKNum = 0;
	}

	public void recvSegment(Network net, Segment segment) {
		segmentsToSend.clear();
		/* Updating the ACKNum_ state variable */
		switch (segment.getType()) {
		case Keywords.Segments.Types.SYN:
			segmentsToSend.add(new Segment(flow.getID(), Keywords.Segments.Types.SYNACK, ACKNum,
					Keywords.Segments.Sizes.SYNSegSize, this.srcHostID, this.dstHostID));
			break;
		case Keywords.Segments.Types.DATA:
			updateACKNum(segment.getSeqNum());
			segmentsToSend.add(new Segment(flow.getID(), Keywords.Segments.Types.ACK, ACKNum,
					Keywords.Segments.Sizes.ACKSegSize, this.srcHostID, this.dstHostID));
			break;
		case Keywords.Segments.Types.FIN:
			segmentsToSend.add(
					new Segment(flow.getID(), Keywords.Segments.Types.FINACK, segment.getSeqNum(),
							Keywords.Segments.Sizes.FINSegSize, this.srcHostID, this.dstHostID));
			/** ===== Statistical Counters ===== **/
			net.getHosts().get(this.dstHostID).getTransportAgent().getFlow()
					.setCompletionTime(net.getCurrentTime());
			/** ================================ **/
			break;
		case Keywords.Segments.Types.CTRL:
			SimApp.error("SDTCPREceiver", "recvSegment", "Receiver got CTRL segment.");
			break;
		default:
			Debugger.debug("SDTCPReceiverv1.recv().case\"default\"::We should not get here.");
			break;
		}
	}

	// TODO Design bug which should be resolved later
	@Override
	public void sendFirst(Network net) {
	}

	/* Local methods */
	private void updateACKNum(int recvdSeqNum) {
		if (recvdSeqNum == ACKNum + 1) {
			ACKNum++;
		}
	}

	@Override
	public void timeout(Network net, int timerID) {

	}

	public int getACKNum() {
		return ACKNum;
	}

	public void setACKNum(int aCKNum) {
		ACKNum = aCKNum;
	}

}