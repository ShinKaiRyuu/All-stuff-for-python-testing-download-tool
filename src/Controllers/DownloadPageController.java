package Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.toIntExact;


public class DownloadPageController {

    public DownloadPageController() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream("Configs/URL.properties"));
    }

    protected Properties properties;

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
    public Button download_button;
    public Label selected_items_label;
    public Button cancel_button;
    private int downloads = 0;
    private int selected_items = 0;
    public ArrayList<Worker> workers = new ArrayList<Worker>();


    public void StartExecution(MouseEvent mouseEvent) throws IOException {
        {
            Properties url_prop = properties;


            download_button.setDisable(true);
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

                python_download_string = url_prop.getProperty("URL_PYTHON_WINDOWS");
                tmp_python = File.createTempFile("python", ".exe");
                output_python = "/downloaded/python-3.5.3.exe";

                firefox_browser_download_string = url_prop.getProperty("URL_FIREFOX_BROWSER_WINDOWS");
                tmp_firefox_browser = File.createTempFile("firefox", ".exe");
                output_firefox_browser = "/downloaded/firefox.exe";

                firefox_driver_download_string = url_prop.getProperty("URL_FIREFOX_DRIVER_WINDOWS");
                tmp_firefox_driver = File.createTempFile("geckodriver", ".zip");
                output_firefox_driver = "/downloaded/geckodriver.zip";

                chrome_browser_download_string = url_prop.getProperty("URL_CHROME_BROWSER_WINDOWS");
                tmp_chrome_browser = File.createTempFile("firefox", ".exe");
                output_chrome_browser = "/downloaded/chrome.exe";

                chrome_driver_download_string = url_prop.getProperty("URL_CHROME_DRIVER_WINDOWS");
                tmp_chrome_driver = File.createTempFile("chromedriver", ".zip");
                output_chrome_driver = "/downloaded/chromedriver.zip";

                if (is64bit) {
                    git_download_string = url_prop.getProperty("URL_GIT_WINDOWS64");
                    tmp_git = File.createTempFile("git", ".exe");
                    output_git = "/downloaded/Git-2.11.0.3-64-bit.exe";
                } else {
                    git_download_string = url_prop.getProperty("URL_GIT_WINDOWS");
                    tmp_git = File.createTempFile("git", ".exe");
                    output_git = "/downloaded/Git-2.11.0.3-32-bit.exe";
                }
            } else if (OSNAME.contains("Mac")) {
                python_download_string = url_prop.getProperty("URL_PYTHON_MACOS");
                tmp_python = File.createTempFile("python", ".pkg");
                output_python = "/downloaded/python-3.5.3.pkg";

                firefox_browser_download_string = url_prop.getProperty("URL_FIREFOX_BROWSER_MACOS");
                tmp_firefox_browser = File.createTempFile("firefox", ".dmg");
                output_firefox_browser = "/downloaded/firefox.dmg";

                firefox_driver_download_string = url_prop.getProperty("URL_FIREFOX_DRIVER_MACOS");
                tmp_firefox_driver = File.createTempFile("geckodriver", ".zip");
                output_firefox_driver = "/downloaded/geckodriver.zip";

                chrome_browser_download_string = url_prop.getProperty("URL_CHROME_BROWSER_MACOS");
                tmp_chrome_browser = File.createTempFile("chrome", ".dmg");
                output_chrome_browser = "/downloaded/chrome.dmg";

                chrome_driver_download_string = url_prop.getProperty("URL_CHROME_DRIVER_MACOS");
                tmp_chrome_driver = File.createTempFile("chromedriver", ".zip");
                output_chrome_driver = "/downloaded/chromedriver.zip";

                git_download_string = url_prop.getProperty("URL_GIT_MACOS");
                tmp_git = File.createTempFile("git", ".dmg");
                output_git = "/downloaded/Git-2.11.0.3-64-bit.dmg";
            }

            if (python_checkbox.isSelected()) {
                workers.add(StartBackgroundDownload(python_pb, python_download_string, tmp_python, output_python));
            }

            if (firefox_checkbox.isSelected()) {
                Worker worker = StartBackgroundDownload(firefox_browser_pb, firefox_browser_download_string, tmp_firefox_browser, output_firefox_browser);
                workers.add(worker);
            }

            if (firefox_checkbox.isSelected()) {
                Worker worker = StartBackgroundDownload(firefox_driver_pb, firefox_driver_download_string, tmp_firefox_driver, output_firefox_driver);
                workers.add(worker);
            }

            if (chrome_checkbox.isSelected()) {
                Worker worker = StartBackgroundDownload(chrome_browser_pb, chrome_browser_download_string, tmp_chrome_browser, output_chrome_browser);
                workers.add(worker);
            }

            if (chrome_checkbox.isSelected()) {
                Worker worker = StartBackgroundDownload(chrome_driver_pb, chrome_driver_download_string, tmp_chrome_driver, output_chrome_driver);
                workers.add(worker);
            }
            if (git_checkbox.isSelected()) {
                Worker worker = StartBackgroundDownload(git_pb, git_download_string, tmp_git, output_git);
                workers.add(worker);
            }
            if (workers != null) {
                cancel_button.setVisible(true);
            }
        }

    }


    private Worker StartBackgroundDownload(ProgressBar pb, String download_string, File tmp_file, String output_file) {
        Worker download_worker = new Worker(download_string, tmp_file, output_file);
        pb.setProgress(0);
        pb.setVisible(true);
        download_worker.addPropertyChangeListener(pcEvt -> {
            if ("progress".equals(pcEvt.getPropertyName())) {
                double progress = (Integer) pcEvt.getNewValue();
                pb.setProgress(progress / 100);
            } else if (pcEvt.getNewValue() == SwingWorker.StateValue.DONE) {
                pb.setVisible(false);
                try {
                    download_worker.get();
                } catch (java.util.concurrent.CancellationException | InterruptedException | ExecutionException e) {
                    // handle any errors here
                    //e.printStackTrace();
                }
            }

        });
        download_worker.execute();
        return download_worker;
    }

    public void check_selected_state(ActionEvent actionEvent) {
        CheckBox checkbox = (CheckBox) actionEvent.getSource();
        if (checkbox.isSelected()) {
            selected_items++;
            selected_items_label.setText(String.format("Selected items: %s", selected_items));
        } else {
            selected_items--;
            selected_items_label.setText(String.format("Selected items: %s", selected_items));
        }
    }

    public void canceldownloads(ActionEvent actionEvent) {
        for (Worker wk : workers
                ) {
            wk.cancel(true);

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
            downloads++;

            Platform.runLater(() -> active_downloads_label.setText("Active Downloads: " + String.valueOf(downloads)));
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
                        boolean cancelled = this.isCancelled();
                        if (cancelled) {
                            break;
                        }
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
                boolean cancelled = this.isCancelled();
                if (!cancelled) {
                    File output = new File(System.getProperty("user.dir") + this.outputfile);
                    boolean mkdirs = output.getParentFile().mkdirs();
                    boolean newFile = output.createNewFile();
                    inStream = new FileInputStream(this.file);
                    outStream = new FileOutputStream(output);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inStream.read(buffer)) > 0) {
                        outStream.write(buffer, 0, length);
                    }
                    inStream.close();
                    outStream.close();
                }
                downloads--;
                if (downloads == 0) {
                    download_button.setDisable(false);
                    cancel_button.setVisible(false);
                }
                Platform.runLater(() -> active_downloads_label.setText("Active Downloads: " + String.valueOf(downloads)));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}