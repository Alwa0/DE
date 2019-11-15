package sample ;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {

    //--------------inputs-----------------
    @FXML
    private TextField x0, y0, X, N, Ninit, Nfinal;

    //---------------methods----------------
    @FXML
    private CheckBox exact, euler, impr, runge;

    //----------------errors----------------
    @FXML
    private CheckBox euler_err, impr_err, runge_err, tot_err, all_mtd, all_err;

    @FXML
    private void handle_methods(){
        if (all_mtd.isSelected()){
            exact.setSelected(true);
            euler.setSelected(true);
            impr.setSelected(true);
            runge.setSelected(true);
        }
        if (!all_mtd.isSelected()){
            exact.setSelected(false);
            euler.setSelected(false);
            impr.setSelected(false);
            runge.setSelected(false);
        }
    }
    @FXML
    private void handle_errors(){
        if (all_err.isSelected()){
            euler_err.setSelected(true);
            impr_err.setSelected(true);
            runge_err.setSelected(true);
        }
        if (!all_err.isSelected()){
            euler_err.setSelected(false);
            impr_err.setSelected(false);
            runge_err.setSelected(false);
        }
    }

    @FXML
    void pressButton(){
        Stage stage1 = new Stage();
        stage1.setTitle("Methods");

        Stage stage2 = new Stage();
        stage2.setTitle("Local errors");

        Stage stage3 = new Stage();
        stage3.setTitle("Total error");

        Controller_Builder builder = new Controller_Builder(
                Double.parseDouble(x0.getText()),Double.parseDouble(y0.getText()), Double.parseDouble(X.getText()),
                Integer.parseInt(N.getText()), Integer.parseInt(Ninit.getText()), Integer.parseInt(Nfinal.getText())
        );

        builder.calc(Integer.parseInt(N.getText()));


        boolean[] isSelectedMethods = new boolean[5];
        isSelectedMethods[1] = exact.isSelected();
        isSelectedMethods[2] = euler.isSelected();
        isSelectedMethods[3] = impr.isSelected();
        isSelectedMethods[4] = runge.isSelected();
        boolean[] isSelectedErrors = new boolean[4];
        isSelectedErrors[1] = euler_err.isSelected();
        isSelectedErrors[2] = impr_err.isSelected();
        isSelectedErrors[3] = runge_err.isSelected();

        if(isSelectedMethods[1] || isSelectedMethods[2] || isSelectedMethods[3] || isSelectedMethods[4]) {
            stage1.setScene(builder.graphMethods(isSelectedMethods));
            stage1.show();
        }
        if(isSelectedErrors[1] || isSelectedErrors[2] || isSelectedErrors[3]) {
            stage2.setScene(builder.graphErrors(isSelectedErrors));
            stage2.show();
        }
        if(tot_err.isSelected()){
            builder.calcTotal();
            stage3.setScene(builder.TotalError());
            stage3.show();
        }
    }
}