import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class App extends Application {
    //---toolBar stuff---//
    ToolBar tbWeb = new ToolBar();
    Button btRefresh = new Button("Refresh");
    Button btBack = new Button("<-");
    Button btForward = new Button("->");
    Button btZoomIn = new Button("+");
    Button btZoomOut = new Button("-");
    //Button btSource = new Button("Source code");
    Button btGo = new Button("Go");
    TextField tfUrl = new TextField();
    Label userLabel = new Label("Enter a URL");

    //---tebPane stuff---//
    TabPane tp = new TabPane();
    Tab tWebBrowser = new Tab("Web Browser URL");

    VBox v1 = new VBox();
    WebView webView = new WebView();
    WebEngine engine = webView.getEngine();
    WebHistory history;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //btSource.setStyle("-fx-background-color: #e3bfea");
        btGo.setStyle("-fx-text-fill: #0d32ec");
        btZoomIn.setStyle("-fx-background-color: #cccccc");
        btZoomOut.setStyle("-fx-background-color: #cccccc");
        userLabel.setStyle("-fx-text-fill: #5cda59");

        tbWeb.getItems().addAll(btBack,btForward,btRefresh,tfUrl,btGo,btZoomIn,btZoomOut,userLabel);
        tp.getTabs().addAll(tWebBrowser);
        v1.getChildren().addAll(tbWeb,tp);
        tfUrl.setPromptText("Type a Url");

        //Action for btGo
        btGo.setOnAction(e -> actionBtGo());
        
        //Action for btRefresh
        btRefresh.setOnAction(e -> actionBtRefresh());

        //Action for btBack
        btBack.setOnAction(e -> actionBtBack());

        //Action for btForwasd
        btForward.setOnAction(e -> actionBtForward());

        //Action for btZoomIn
        btZoomIn.setOnAction(e -> actionBtZoomIn());

        //Action for btZommOut
        btZoomOut.setOnAction(e -> actionBtZoomOut());

        tWebBrowser.setContent(webView);
        try{
            Scene scene = new Scene(v1);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Web Browser");
            primaryStage.setMaximized(true);
            primaryStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void actionBtGo(){
        String s = tfUrl.getText();
        engine.load("https://" + s);
        tWebBrowser.setText(s);
        engine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue == Worker.State.SUCCEEDED){
                System.out.println("Page has been loaded");
                userLabel.setText("Enter a URL");
                userLabel.setStyle("-fx-text-fill: #5cda59");
            }
            else if(newValue == Worker.State.FAILED){
                System.out.println("Loading failed,Pls enter the correct URL");
                userLabel.setText("Loading failed, Pls enter the correct URL");
                userLabel.setStyle("-fx-text-fill: #ec0d0d");
            }
        });
        
    }

    private void actionBtRefresh(){
        engine.reload();
    }

    private void actionBtBack(){
        history = webView.getEngine().getHistory();
        history.go(-1);
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        tfUrl.setText(entries.get(history.getCurrentIndex()).getUrl());
    }

    private void actionBtForward(){
        history = webView.getEngine().getHistory();
        history.go(1);
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        tfUrl.setText(entries.get(history.getCurrentIndex()).getUrl());
    }

    private void actionBtZoomIn(){
        webView.setZoom(webView.getZoom() + 0.10);
    }

    private void actionBtZoomOut(){
        webView.setZoom(webView.getZoom() - 0.10);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
