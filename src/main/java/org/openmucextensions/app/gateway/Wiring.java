package org.openmucextensions.app.gateway;

import org.openmuc.framework.data.Record;
import org.openmuc.framework.data.Value;
import org.openmuc.framework.dataaccess.Channel;
import org.openmuc.framework.dataaccess.DataAccessService;
import org.openmuc.framework.dataaccess.RecordListener;

public class Wiring implements RecordListener {

	private final String inputChannelId;
	private final String outputChannelId;
	
	private boolean isWired = false;
	
	private Channel inputChannel = null;
	private Channel outputChannel = null;
	
	public Wiring(final String inputChannelId, final String outputChannelId) {
		this.inputChannelId = inputChannelId;
		this.outputChannelId = outputChannelId;
	}

	@Override
	public void newRecord(Record record) {
		if(outputChannel != null && outputChannel.isConnected()){
			Value outLastValue = outputChannel.getLatestRecord().getValue();
			// only write value if it has changed. This inhibits a circular write for a bidirectional connection
			if( record.getValue() != null &&
					( outLastValue == null ||
						!outputChannel.getLatestRecord().getValue().asString().equals(record.getValue().asString()))) {
				outputChannel.write(record.getValue());
			}
		}
	}
	
	public void wire(DataAccessService service) {
		
		if(service.getAllIds().contains(inputChannelId)) {
			inputChannel = service.getChannel(inputChannelId);
			inputChannel.addListener(this);
			isWired = true;
		} else {
			inputChannel = null;
		}
		
		if(service.getAllIds().contains(outputChannelId)) {
			outputChannel = service.getChannel(outputChannelId);
		} else {
			outputChannel = null;
		}
	}
	
	public void unwire() {
		
		if(inputChannel != null) {
			inputChannel.removeListener(this);
			inputChannel = null;
		}
		
		outputChannel = null;
		isWired = false;
	}
	
	public String getInputChannelId() {
		return inputChannelId;
	}

	public String getOutputChannelId() {
		return outputChannelId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inputChannelId == null) ? 0 : inputChannelId.hashCode());
		result = prime * result + (isWired ? 1231 : 1237);
		result = prime * result + ((outputChannelId == null) ? 0 : outputChannelId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Wiring other = (Wiring) obj;
		if (inputChannelId == null) {
			if (other.inputChannelId != null)
				return false;
		} else if (!inputChannelId.equals(other.inputChannelId))
			return false;
		if (isWired != other.isWired)
			return false;
		if (outputChannelId == null) {
			if (other.outputChannelId != null)
				return false;
		} else if (!outputChannelId.equals(other.outputChannelId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Wiring [inputChannelId=" + inputChannelId + ", outputChannelId=" + outputChannelId + ", isWired="
				+ isWired + "]";
	}

}
