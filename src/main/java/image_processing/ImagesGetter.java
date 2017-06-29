package image_processing;

import java.util.LinkedList;


public class ImagesGetter {
	private LinkedList<String> lockedImages;
	String rootPath;

	public synchronized boolean lockImage(String filePath) {
        for(String image:lockedImages) {
            if(image.equals(filePath)) {
                return false;
            }
        }
        lockedImages.add(filePath);
        return true;
	}
	public synchronized void unlockImage(String filePath) {
        for(String image:lockedImages) {
            if(image.equals(filePath)) {
                lockedImages.remove(image);
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
