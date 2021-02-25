package View;

import Controller.AdministratorController;
import Controller.AuthenticationController;
import Model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class AdministratorView extends JFrame{
    private JTable dataTable;
    private JButton viewPostmansButton;
    private JButton viewCoordinatorsButton;
    private JButton deleteEntryButton;
    private JTable updateTable;
    private JButton updateEntryButton;
    private JButton addEntryButton;
    private JScrollPane dataTablePane;
    private JScrollPane updateTablePane;
    private JPanel rootPanel;
    private JPanel tablePanel;

    public void setUpdateEntryButtonText(String text){
        updateEntryButton.setText(text);
    }
    public String getUpdateEntryButtonText(){
        return updateEntryButton.getText();
    }
    public JButton getUpdateEntryButton() {return updateEntryButton;}
    public JTable getDataTable(){
        return dataTable;
    }

    public void setDataTable(JTable dataTable){
        this.dataTable = dataTable;
    }

    public void setUpdateTable(JTable updateTable) { this.updateTable = updateTable; }

    public JScrollPane getDataTablePane(){
        return dataTablePane;
    }

    public JTable getUpdateTable() { return updateTable;}

    public JScrollPane getUpdateTablePane(){
        return updateTablePane;
    }

    public JButton getViewPostmansButton(){ return viewPostmansButton; }

    public JButton getViewCoordinatorsButton(){ return viewCoordinatorsButton; }


    public AdministratorView(AdministratorController administratorController) {
        this.setTitle("Administrator");
        this.setSize(500,300);
        this.add(rootPanel);
        updateEntryButton.setVisible(false);
        //dataTable.setVisible(false);
        updateTable.setVisible(true);
        viewPostmansButton.addActionListener(administratorController.viewPostmansButtonActionListener());
        viewCoordinatorsButton.addActionListener(administratorController.viewCoordinatorsButtonActionListener());
        deleteEntryButton.addActionListener(administratorController.deleteButtonActionListener());
        updateEntryButton.addActionListener(administratorController.updateEntryButtonActionListener());
        addEntryButton.addActionListener(administratorController.addEntryButtonActionListener());
        this.addWindowListener(administratorController.windowListener());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
