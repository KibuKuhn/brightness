package kibu.kuhn.brightness;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.inject.Inject;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kibu.kuhn.brightness.ui.IGui;
import kibu.kuhn.brightness.utils.InjectorSupport;

public class Brightness
{

    public static final String LOG_DIR = "logDir";
    public static final String LOG_FILE = "logFile";

    static {
        initLogging();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Brightness.class);

    public static void main(String[] args) throws IOException {
        if (!lockInstance("Brightness.lock")) {
            LOGGER.info("Brightness already running. Exiting.");
            System.exit(0);
        }

        if (System.getProperty("delay") != null) {
            try {
                var delay = System.getProperty("delay");
                var currentThread = Thread.currentThread();
                synchronized (currentThread) {
                    currentThread.wait(Integer.valueOf(delay));
                }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
                throw new IllegalStateException(ex);
            }
        }

        SwingUtilities.invokeLater(Brightness::new);
    }

    @Inject
    private IGui gui;

    Brightness() {
        InjectorSupport.get().injectMembers(this);
        if (!gui.checkSupport()) {
            System.exit(0);
        }

        gui.init();
    }

    private static void initLogging() {
        var logDir = System.getProperty(LOG_DIR);
        if (logDir == null) {
            logDir = System.getProperty("user.home");
            System.setProperty(LOG_DIR, logDir);
        }
        System.setProperty(LOG_FILE, ".brightness.log");
        var logLevel = System.getProperty("logLevel");
        if (logLevel == null) {
            System.setProperty("logLevel", "INFO");
        }
    }

    private static boolean lockInstance(String lockFile) {
        try {
            var file = new File(lockFile);
            var randomAccessFile = new RandomAccessFile(file, "rw");
            var fileLock = randomAccessFile.getChannel().tryLock();
            if (fileLock != null) {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        fileLock.release();
                        randomAccessFile.close();
                        file.delete();
                    } catch (Exception e) {
                        LOGGER.error("Unable to remove lock file: " + lockFile, e);
                    }
                }));
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Unable to create and/or lock file: " + lockFile, e);
        }
        return false;
    }
}
