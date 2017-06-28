package Archievers;

import Track.PLTFile;

public class DoubleLinesRemover {
	private PLTFile inputFile;
	private PLTFile outputFile;
	public DoubleLinesRemover(PLTFile inputFile) {
		this.inputFile = inputFile;
		outputFile = new PLTFile(inputFile);
	}
/*	private int addNextPoint(Track currentTrack, Track tempTrack, double minDeviation, int numberStart) throws Exception {
		for(int i = numberStart;i<currentTrack.size();i++) {
			if(currentTrack.getPoint(i).distTo(outputFile) > minDeviation) {
				currentTrack.getPoint(i).setBreakTrackLine(true);
				tempTrack = new Track(currentTrack.getPoint(i));
				return i+1;
			}
		}
		return currentTrack.size();
	}
	private int addSegments(Track currentTrack, Track tempTrack, double minDeviation, int numberStrart) throws Exception {
		for(int i = numberStrart-1;i<currentTrack.size()-1;i++) {
			if(currentTrack.getSegment(i).maxDistTo(outputFile) >minDeviation) {
				tempTrack.addPoint(currentTrack.getPoint(i+1));
			} else {
				outputFile.addTrack(tempTrack);
//				tempTrack = new Track();
				return i+2;
			}
		}
		outputFile.addTrack(tempTrack);
//		tempTrack = new Track();
		return currentTrack.size();
	}
	public void process(double minDeviation, double distanceAnotherTrack) throws Exception {
		Track tempTrack = new Track();
		int i = 0;
		for(Track track : inputFile.getListOfTracks()) {
			int currentPosition = addNextPoint(track, tempTrack, minDeviation, 0);
			while(currentPosition<track.size()) {
				tempTrack = new Track(track.getPoint(currentPosition-1));
				System.out.print("Current position is " + currentPosition + "\n");
				currentPosition = addSegments(track, tempTrack, minDeviation, currentPosition);
				currentPosition = addNextPoint(track, tempTrack, minDeviation, currentPosition);
			}
			i++;
			System.out.print("Track " + i + " processed\n");
		}
	}*/
	public PLTFile getInputFile() {
		return inputFile;
	}
	public void setInputFile(PLTFile inputFile) {
		this.inputFile = inputFile;
	}
	public PLTFile getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(PLTFile outputFile) {
		this.outputFile = outputFile;
	}

}
