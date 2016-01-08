package org.openmucextensions.app.gateway;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Taken from http://stackoverflow.com/questions/16251273/can-i-watch-for-single-file-change-with-watchservice-not-the-whole-directory
 * and modified with callback.
 */
public class FileWatcher extends Thread {
    
	private static final Logger logger = LoggerFactory.getLogger(Gateway.class);
	
	private final File file;
    private final FileChangedCallback callback;
	
	private AtomicBoolean stop = new AtomicBoolean(false);

    public FileWatcher(final File file, final FileChangedCallback callback) {
        this.file = file;
        this.callback = callback;
    }

    public boolean isStopped() { return stop.get(); }
    public void stopThread() { stop.set(true); }

    @Override
    public void run() {
        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            Path path = file.toPath().getParent();
            path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
            while (!isStopped()) {
                WatchKey key;
                try { key = watcher.poll(1000, TimeUnit.MILLISECONDS); }
                catch (InterruptedException e) { return; }
                if (key == null) { Thread.yield(); continue; }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        Thread.yield();
                        continue;
                    } else if (kind == java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY
                            && filename.toString().equals(file.getName())) {
                        if(callback != null) callback.fileChanged(file);
                    }
                    boolean valid = key.reset();
                    if (!valid) { break; }
                }
                Thread.yield();
            }
        } catch (IOException e) {
			logger.error("I/O error while trying to setup config file watch service: {}", e.getMessage());
		}
    }
    
    public static interface FileChangedCallback {
    	public void fileChanged(File file);
    }
}
