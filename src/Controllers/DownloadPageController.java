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
import java.net.URLDecoder;
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
            //BROWSERS
            //CHROME
            String chrome_browser_download_string = null;
            String chrome_driver_download_string = null;
            //FIREFOX
            String firefox_browser_download_string = null;
            String firefox_driver_download_string = null;
            //GIT
            String git_download_string = null;
            //IDE

            String OSNAME = System.getProperty("os.name");

            boolean is64bit;

            if (OSNAME.contains("Windows")) {
                is64bit = (System.getenv("ProgramFiles(x86)") != null);

                python_download_string = url_prop.getProperty("URL_PYTHON_WINDOWS");
                firefox_browser_download_string = url_prop.getProperty("URL_FIREFOX_BROWSER_WINDOWS");
                firefox_driver_download_string = url_prop.getProperty("URL_FIREFOX_DRIVER_WINDOWS");
                chrome_browser_download_string = url_prop.getProperty("URL_CHROME_BROWSER_WINDOWS");
                chrome_driver_download_string = url_prop.getProperty("URL_CHROME_DRIVER_WINDOWS");
                if (is64bit) {
                    git_download_string = url_prop.getProperty("URL_GIT_WINDOWS64");
                } else {
                    git_download_string = url_prop.getProperty("URL_GIT_WINDOWS");
                }
            } else if (OSNAME.contains("Mac")) {
                python_download_string = url_prop.getProperty("URL_PYTHON_MACOS");
                firefox_browser_download_string = url_prop.getProperty("URL_FIREFOX_BROWSER_MACOS");
                firefox_driver_download_string = url_prop.getProperty("URL_FIREFOX_DRIVER_MACOS");
                chrome_browser_download_string = url_prop.getProperty("URL_CHROME_BROWSER_MACOS");
                chrome_driver_download_string = url_prop.getProperty("URL_CHROME_DRIVER_MACOS");
                git_download_string = url_prop.getProperty("URL_GIT_MACOS");
            }

            if (python_checkbox.isSelected()) {
                workers.add(StartBackgroundDownload(python_pb, python_download_string));
            }

            if (firefox_checkbox.isSelected()) {
                Worker worker = StartBackgroundDownload(firefox_browser_pb, firefox_browser_download_string);
                workers.add(worker);
            }

            if (firefox_checkbox.isSelected()) {
                Worker worker = StartBackgroundDownload(firefox_driver_pb, firefox_driver_download_string);
                workers.add(worker);
            }

            if (chrome_checkbox.isSelected()) {
                Worker worker = StartBackgroundDownload(chrome_browser_pb, chrome_browser_download_string);
                workers.add(worker);
            }

            if (chrome_checkbox.isSelected()) {
                Worker worker = StartBackgroundDownload(chrome_driver_pb, chrome_driver_download_string);
                workers.add(worker);
            }
            if (git_checkbox.isSelected()) {
                Worker worker = StartBackgroundDownload(git_pb, git_download_string);
                workers.add(worker);
            }
            if (workers != null) {
                cancel_button.setVisible(true);
            }
        }

    }


    private Worker StartBackgroundDownload(ProgressBar pb, String download_string) {
        Worker download_worker = new Worker(download_string);
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
        private File file = null;
        private String outputfile;

        Worker(String site) {
            this.site = site;
        }

        @Override
        protected Void doInBackground() throws Exception {
            downloads++;

            Platform.runLater(() -> active_downloads_label.setText("Active Downloads: " + String.valueOf(downloads)));
            URL url = new URL(URLDecoder.decode(site,"UTF-8"));
            String url_file = url.getFile();
            outputfile = url_file.substring(url_file.lastIndexOf('/') + 1, url_file.length());
            String name = outputfile.split("\\.(?=[^\\.]+$)", 0)[0];
            String extension = outputfile.split("\\.(?=[^\\.]+$)", 0)[1];
            outputfile = "/Downloads/"+outputfile;
            file = File.createTempFile(name,"."+extension);
            url = new URL(site);
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