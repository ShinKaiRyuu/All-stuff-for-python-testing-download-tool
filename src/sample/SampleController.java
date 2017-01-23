package sample;

import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.toIntExact;


public class SampleController {
    public CheckBox python_checkbox;
    public CheckBox git_checkbox;
    public ProgressBar python_pb;
    public ProgressBar git_pb;
    public CheckBox firefox_checkbox;
    public CheckBox chrome_checkbox;
    public ProgressBar firefox_browser_pb;
    public ProgressBar firefox_driver_pb;
    public ProgressBar chrome_browser_pb;
    public ProgressBar chrome_driver_pb;
    public Label active_downloads_label;

    private int DOWNLOADS = 0;
    private Map python_download_url = new HashMap<String, String>() {
        {
            put("Windows", "https://www.python.org/ftp/python/3.5.3/python-3.5.3.exe");
            put("Mac", "https://www.python.org/ftp/python/3.5.3/python-3.5.3-macosx10.6.pkg");
            put("LINUX", "https://www.python.org/ftp/python/3.5.3/Python-3.5.3.tgz");
        }
    };
    private Map git_download_url = new HashMap<String, String>() {
        {
            put("Windows", "https://github.com/git-for-windows/git/releases/download/v2.11.0.windows.3/Git-2.11.0.3-32-bit.exe");
            put("Windows64", "https://github.com/git-for-windows/git/releases/download/v2.11.0.windows.3/Git-2.11.0.3-64-bit.exe");
            put("Mac", "https://sourceforge.net/projects/git-osx-installer/files/git-2.10.1-intel-universal-mavericks.dmg/download?use_mirror=netix&r=&use_mirror=netix");
            put("Linux", "https://codeload.github.com/git/git/zip/v2.11.0");
        }
    };
    private Map firefox_browser_download_url = new HashMap<String, String>() {
        {
            put("Windows", "https://ftp.mozilla.org/pub/firefox/releases/50.1.0/win32/ru/Firefox%20Setup%2050.1.0.exe");
            put("Mac", "https://ftp.mozilla.org/pub/firefox/releases/50.1.0/mac/ru/Firefox%2050.1.0.dmg");
            put("Linux", "https://ftp.mozilla.org/pub/firefox/releases/50.1.0/linux-x86_64/ru/firefox-50.1.0.tar.bz2");
        }
    };
    private Map firefox_driver_download_url = new HashMap<String, String>() {
        {
            put("Windows", "https://github.com/mozilla/geckodriver/releases/download/v0.13.0/geckodriver-v0.13.0-win32.zip");
            put("Mac", "https://github.com/mozilla/geckodriver/releases/download/v0.13.0/geckodriver-v0.13.0-macos.tar.gz");
            put("Linux", "https://github.com/mozilla/geckodriver/releases/download/v0.13.0/geckodriver-v0.13.0-linux32.tar.gz");
        }
    };
    private Map chrome_browser_download_url = new HashMap<String, String>() {
        {
            put("Windows", "https://dl.google.com/tag/s/appguid%3D%7B8A69D345-D564-463C-AFF1-A69D9E530F96%7D%26iid%3D%7B23939508-4452-649D-5896-A18215642189%7D%26lang%3Dru%26browser%3D4%26usagestats%3D1%26appname%3DGoogle%2520Chrome%26needsadmin%3Dprefers%26ap%3Dx64-stable-statsdef_1%26installdataindex%3Ddefaultbrowser/update2/installers/ChromeStandaloneSetup64.exe");
            put("Mac", "https://dl.google.com/chrome/mac/stable/GGRO/googlechrome.dmg");
            put("Linux", "https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb");
        }
    };
    private Map chrome_driver_download_url = new HashMap<String, String>() {
        {
            put("Windows", "https://chromedriver.storage.googleapis.com/2.27/chromedriver_win32.zip");
            put("Mac", "https://chromedriver.storage.googleapis.com/2.27/chromedriver_mac64.zip");
            put("Linux", "https://chromedriver.storage.googleapis.com/2.27/chromedriver_linux64.zip");
        }
    };


    public void StartExecution(MouseEvent mouseEvent) throws IOException {
        {

            //PYTHON
            String python_download_string = null;
            String output_python = null;
            //BROWSERS
            //CHROME
            String chrome_browser_download_string = null;
            String chrome_driver_download_string = null;
            String output_chrome_browser = null;
            String output_chrome_driver = null;
            //FIREFOX
            String firefox_browser_download_string = null;
            String firefox_driver_download_string = null;
            String output_firefox_browser = null;
            String output_firefox_driver = null;
            //GIT
            String git_download_string = null;
            String output_git = null;
            //IDE

            String OSNAME = System.getProperty("os.name");
            File tmp_python = null;
            File tmp_firefox_browser = null;
            File tmp_firefox_driver = null;
            File tmp_chrome_browser = null;
            File tmp_chrome_driver = null;
            File tmp_git = null;
            boolean is64bit;

            if (OSNAME.contains("Windows")) {
                is64bit = (System.getenv("ProgramFiles(x86)") != null);

                python_download_string = (String) python_download_url.get("Windows");
                tmp_python = File.createTempFile("python", ".exe");
                output_python = "/downloaded/python-3.5.3.exe";

                firefox_browser_download_string = (String) firefox_browser_download_url.get("Windows");
                tmp_firefox_browser = File.createTempFile("firefox", ".exe");
                output_firefox_browser = "/downloaded/firefox.exe";

                firefox_driver_download_string = (String) firefox_driver_download_url.get("Windows");
                tmp_firefox_driver = File.createTempFile("geckodriver", ".zip");
                output_firefox_driver = "/downloaded/geckodriver.zip";

                chrome_browser_download_string = (String) chrome_browser_download_url.get("Windows");
                tmp_chrome_browser = File.createTempFile("firefox", ".exe");
                output_chrome_browser = "/downloaded/chrome.exe";

                chrome_driver_download_string = (String) chrome_driver_download_url.get("Windows");
                tmp_chrome_driver = File.createTempFile("chromedriver", ".zip");
                output_chrome_driver = "/downloaded/chromedriver.zip";

                if (is64bit) {
                    git_download_string = (String) git_download_url.get("Windows64");
                    tmp_git = File.createTempFile("git", ".exe");
                    output_git = "/downloaded/Git-2.11.0.3-64-bit.exe";
                } else {
                    git_download_string = (String) git_download_url.get("Windows");
                    tmp_git = File.createTempFile("git", ".exe");
                    output_git = "/downloaded/Git-2.11.0.3-32-bit.exe";
                }
            }
            else if (OSNAME.contains("Mac"))
            {
                python_download_string = (String) python_download_url.get("Mac");
                tmp_python = File.createTempFile("python", ".pkg");
                output_python = "/downloaded/python-3.5.3.pkg";

                firefox_browser_download_string = (String) firefox_browser_download_url.get("Mac");
                tmp_firefox_browser = File.createTempFile("firefox", ".dmg");
                output_firefox_browser = "/downloaded/firefox.dmg";

                firefox_driver_download_string = (String) firefox_driver_download_url.get("Mac");
                tmp_firefox_driver = File.createTempFile("geckodriver", ".zip");
                output_firefox_driver = "/downloaded/geckodriver.zip";

                chrome_browser_download_string = (String) chrome_browser_download_url.get("Mac");
                tmp_chrome_browser = File.createTempFile("chrome", ".dmg");
                output_chrome_browser = "/downloaded/chrome.dmg";

                chrome_driver_download_string = (String) chrome_driver_download_url.get("Mac");
                tmp_chrome_driver = File.createTempFile("chromedriver", ".zip");
                output_chrome_driver = "/downloaded/chromedriver.zip";

                git_download_string = (String) git_download_url.get("Mac");
                tmp_git = File.createTempFile("git", ".dmg");
                output_git = "/downloaded/Git-2.11.0.3-64-bit.dmg";
            }
            if (python_checkbox.isSelected()) {
                final Worker python_download_worker = new Worker(python_download_string, tmp_python, output_python);
                python_pb.setVisible(true);
                python_download_worker.addPropertyChangeListener(pcEvt -> {
                    if ("progress".equals(pcEvt.getPropertyName())) {
                        double progress = (Integer) pcEvt.getNewValue();
                        python_pb.setProgress(progress / 100);
                    } else if (pcEvt.getNewValue() == SwingWorker.StateValue.DONE) {
                        try {
                            python_download_worker.get();
                        } catch (InterruptedException | ExecutionException e) {
                            // handle any errors here
                            e.printStackTrace();
                        }
                    }

                });
                python_download_worker.execute();
            }

            if (firefox_checkbox.isSelected()) {
                final Worker firefox_browser_download_worker = new Worker(firefox_browser_download_string, tmp_firefox_browser, output_firefox_browser);
                firefox_browser_pb.setVisible(true);
                firefox_browser_download_worker.addPropertyChangeListener(pcEvt -> {
                    if ("progress".equals(pcEvt.getPropertyName())) {
                        double progress = (Integer) pcEvt.getNewValue();
                        firefox_browser_pb.setProgress(progress / 100);
                    } else if (pcEvt.getNewValue() == SwingWorker.StateValue.DONE) {
                        try {
                            firefox_browser_download_worker.get();
                        } catch (InterruptedException | ExecutionException e) {
                            // handle any errors here
                            e.printStackTrace();
                        }
                    }

                });
                firefox_browser_download_worker.execute();
            }

            if (firefox_checkbox.isSelected()) {
                final Worker firefox_driver_download_worker = new Worker(firefox_driver_download_string, tmp_firefox_driver, output_firefox_driver);
                firefox_driver_pb.setVisible(true);
                firefox_driver_download_worker.addPropertyChangeListener(pcEvt -> {
                    if ("progress".equals(pcEvt.getPropertyName())) {
                        double progress = (Integer) pcEvt.getNewValue();
                        firefox_driver_pb.setProgress(progress / 100);
                    } else if (pcEvt.getNewValue() == SwingWorker.StateValue.DONE) {
                        try {
                            firefox_driver_download_worker.get();
                        } catch (InterruptedException | ExecutionException e) {
                            // handle any errors here
                            e.printStackTrace();
                        }
                    }

                });
                firefox_driver_download_worker.execute();
            }


            if (chrome_checkbox.isSelected()) {
                final Worker chrome_browser_download_worker = new Worker(chrome_browser_download_string, tmp_chrome_browser, output_chrome_browser);
                chrome_browser_pb.setVisible(true);
                chrome_browser_download_worker.addPropertyChangeListener(pcEvt -> {
                    if ("progress".equals(pcEvt.getPropertyName())) {
                        double progress = (Integer) pcEvt.getNewValue();
                        chrome_browser_pb.setProgress(progress / 100);
                    } else if (pcEvt.getNewValue() == SwingWorker.StateValue.DONE) {
                        try {
                            chrome_browser_download_worker.get();
                        } catch (InterruptedException | ExecutionException e) {
                            // handle any errors here
                            e.printStackTrace();
                        }
                    }

                });
                chrome_browser_download_worker.execute();
            }

            if (chrome_checkbox.isSelected()) {
                final Worker chrome_driver_download_worker = new Worker(chrome_driver_download_string, tmp_chrome_driver, output_chrome_driver);
                chrome_driver_pb.setVisible(true);
                chrome_driver_download_worker.addPropertyChangeListener(pcEvt -> {
                    if ("progress".equals(pcEvt.getPropertyName())) {
                        double progress = (Integer) pcEvt.getNewValue();
                        chrome_driver_pb.setProgress(progress / 100);
                    } else if (pcEvt.getNewValue() == SwingWorker.StateValue.DONE) {
                        try {
                            chrome_driver_download_worker.get();
                        } catch (InterruptedException | ExecutionException e) {
                            // handle any errors here
                            e.printStackTrace();
                        }
                    }

                });
                chrome_driver_download_worker.execute();
            }

            if (git_checkbox.isSelected()) {

                final Worker git_download_worker = new Worker(git_download_string, tmp_git, output_git);
                git_pb.setVisible(true);
                git_download_worker.addPropertyChangeListener(pcEvt -> {
                    if ("progress".equals(pcEvt.getPropertyName())) {
                        double progress = (Integer) pcEvt.getNewValue();
                        git_pb.setProgress(progress / 100);
                    } else if (pcEvt.getNewValue() == SwingWorker.StateValue.DONE) {
                        try {
                            git_download_worker.get();
                        } catch (InterruptedException | ExecutionException e) {
                            // handle any errors here
                            e.printStackTrace();
                        }
                    }

                });
                git_download_worker.execute();
            }
        }
    }


    class Worker extends SwingWorker<Void, Void> {
        private String site;
        private File file;
        private String outputfile;

        Worker(String site, File file, String outputfile) {
            this.site = site;
            this.file = file;
            this.outputfile = outputfile;
        }

        @Override
        protected Void doInBackground() throws Exception {
            DOWNLOADS++;

            Platform.runLater(() -> active_downloads_label.setText("Active Downloads: " + String.valueOf(DOWNLOADS)));
            URL url = new URL(site);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            long filesize = connection.getContentLength();
            long totalDataRead = 0;
            try (java.io.BufferedInputStream in = new java.io.BufferedInputStream(
                    connection.getInputStream())) {
                java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
                try (java.io.BufferedOutputStream bout = new BufferedOutputStream(
                        fos, 1024)) {
                    byte[] data = new byte[1024];
                    int i;
                    while ((i = in.read(data, 0, 1024)) >= 0) {
                        totalDataRead = totalDataRead + i;
                        bout.write(data, 0, i);
                        long percent = (totalDataRead * 100) / filesize;
                        setProgress(toIntExact(percent));
                    }
                }
            }
            return null;
        }

        @Override
        protected void done() {
            InputStream inStream;
            OutputStream outStream;

            try {

                File output = new File(System.getProperty("user.dir") + this.outputfile);
                boolean mkdirs = output.getParentFile().mkdirs();
                boolean newFile = output.createNewFile();
                inStream = new FileInputStream(this.file);
                outStream = new FileOutputStream(output);
                byte[] buffer = new byte[1024];

                int length;
                //copy the file content in bytes
                while ((length = inStream.read(buffer)) > 0) {

                    outStream.write(buffer, 0, length);

                }

                inStream.close();
                outStream.close();
                DOWNLOADS--;
                Platform.runLater(() -> active_downloads_label.setText("Active Downloads: "+String.valueOf(DOWNLOADS)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}