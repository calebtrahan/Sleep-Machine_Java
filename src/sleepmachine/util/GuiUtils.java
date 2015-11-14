package sleepmachine.util;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.control.*;

import java.util.Collections;
import java.util.Optional;

public class GuiUtils {

    public static void validate(TextField txtfield, int highvalue, int valtotest) {
        ObservableList<String> styleclass = txtfield.getStyleClass();
        if (valtotest > highvalue) {if (!styleclass.contains("error")) {styleclass.add("error");}}
        else {styleclass.removeAll(Collections.singleton("error"));}
    }

    public static void validate(ChoiceBox<String> choicebox, Boolean val) {
        ObservableList<String> styleclass = choicebox.getStyleClass();
        if (! val ) {if (!styleclass.contains("error")) {styleclass.add("error");}}
        else {styleclass.removeAll(Collections.singleton("error"));}
    }

    public static void validate(TextField txtfield, Boolean val) {
        ObservableList<String> styleclass = txtfield.getStyleClass();
        if (! val ) {if (!styleclass.contains("error")) {styleclass.add("error");}}
        else {styleclass.removeAll(Collections.singleton("error"));}
    }

    public static void validate(Label lbl, Boolean val) {
        ObservableList<String> styleclass = lbl.getStyleClass();
        if (! val ) {if (!styleclass.contains("error")) {styleclass.add("error");}
        } else {styleclass.removeAll(Collections.singleton("error"));}
    }

    public static void setIntegerOnlyTextField(TextField textField, int maxval) {
        final PseudoClass errorClass = PseudoClass.getPseudoClass("error");
        textField.pseudoClassStateChanged(errorClass, true);
        textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.equals("")) {
                try {
                    Integer newval = Integer.parseInt(newValue);
                    validate(textField, maxval, newval);
                } catch (NumberFormatException e) {
                    textField.setText(oldValue);
                }
            }
        });
    }

    public static boolean getanswerdialog(String titletext, String headertext, String contenttext) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(titletext);
        a.setHeaderText(headertext);
        a.setContentText(contenttext);
        Optional<ButtonType> answer = a.showAndWait();
        return answer.isPresent() && answer.get() == ButtonType.OK;
    }

    public static void showinformationdialog(String titletext, String headertext, String contexttext) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titletext);
        a.setHeaderText(headertext);
        a.setContentText(contexttext);
        a.showAndWait();
    }

    public static void showerrordialog(String titletext, String headertext, String contenttext) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(titletext);
        a.setHeaderText(headertext);
        a.setContentText(contenttext);
        a.showAndWait();
    }

    public static void togglecheckboxtext(CheckBox checkbox) {
        if (checkbox.isSelected()) {checkbox.setText("ON");} else {checkbox.setText("OFF");}
    }
}
