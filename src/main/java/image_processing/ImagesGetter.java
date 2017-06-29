package image_processing;

import java.util.LinkedList;


public class ImagesGetter {
	private LinkedList<String> lockedImages;
	String rootPath;

    private synchronized boolean isLocked(String filePath) {
        for(String image:lockedImages) {
            if(image.equals(filePath)) {
                return true;
            }
        }
        return false;

    }

	public synchronized boolean lockImage(String filePath) {
        while(isLocked(filePath)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lockedImages.add(filePath);
        return true;
	}
	public synchronized void unlockImage(String filePath) {
        for(String image:lockedImages) {
            if(image.equals(filePath)) {
                lockedImages.remove(image);
                notify();
                return;
            }
        }
        throw new RuntimeException("Attempt to unlock not locked image");
	}

	public ImagesGetter(String rootPath) {
		this.rootPath = rootPath;
		lockedImages = new LinkedList<String>();
	}

    public String getRootPath() {
        return rootPath;
    }
	
}
