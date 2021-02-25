package Controller;

import Model.User;
import Model.UserFactory;
import View.AdministratorView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AdministratorController {
    private AdministratorView administratorView;
    private Map<String, User> users;
    private List<String> postmans;
    private List<String> coordinators;
    private User.Type currentShowingType;
    private String selectedUserName;
    private MainController mainController;
    private AuthenticationController authenticationController;

    public void classifyUsers(){
        this.users = authenticationController.getUsers();
        postmans = new LinkedList<String>();
        coordinators = new LinkedList<String>();
        for(User user:users.values()){
            User.Type type = user.getType();
            if(type == User.Type.POSTMAN){
                postmans.add(user.getUsername());
            }
            if(type == User.Type.COORDINATOR){
                coordinators.add(user.getUsername());
            }
        }
    }


    private JTable createTable(String[][] tableData, String[] tableCol) {
        DefaultTableModel model = new DefaultTableModel(tableData, tableCol);
        final JTable mainTable = new JTable();
        mainTable.setModel(model);
        mainTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    Object username = mainTable.getValueAt(mainTable.getSelectedRow(), 0);
                    if (username.equals(" "))
                        return;

                    updateSecondaryTable((String)username);
                    selectedUserName = (String)username;
                }
            }
        });
        return mainTable;
    }

    public ActionListener viewPostmansButtonActionListener() {
        return actionEvent ->{
            classifyUsers();
            String[][] tableData = new String[postmans.size() + 10][1];
            int size = postmans.size();
            for (int i = 0; i < size; i++) {
                tableData[i][0] = postmans.get(i);
            }

            //JTable table = administratorView.getDataTable();
            JTable table = createTable(tableData, new String[]{"users"});
            JScrollPane pane = administratorView.getDataTablePane();
            pane.setViewportView(table);
            pane.revalidate();
            currentShowingType = User.Type.POSTMAN;
            table.setVisible(true);
        };
    }
    public ActionListener viewCoordinatorsButtonActionListener(){
        return actionEvent -> {
            String[][] tableData = new String[coordinators.size()+10][1];
            int size = coordinators.size();
            for(int i=0;i<size;i++){
                tableData[i][0] = coordinators.get(i);
            }
            JTable dataTable = createTable(tableData, new String[] {"users"});
            JScrollPane pane = administratorView.getDataTablePane();
            pane.setViewportView(dataTable);
            pane.revalidate();
            currentShowingType = User.Type.COORDINATOR;
            dataTable.setVisible(true);
        };
    }

    public ActionListener updateEntryButtonActionListener(){
    return actionEvent -> {
        String oldUsername = selectedUserName;
        TableModel model = administratorView.getUpdateTable().getModel();
        String name = (String)model.getValueAt(0,1);
        String username = (String)model.getValueAt(1,1);
        String password = (String)model.getValueAt(2,1);
        User user = new UserFactory().getUser(username, name, password, currentShowingType);
        if(administratorView.getUpdateEntryButtonText().contains("Add")){
            authenticationController.addUser(user);
        }
        else {
            authenticationController.updateUser(oldUsername, user);
        }
        if(currentShowingType == User.Type.POSTMAN){
            for(ActionListener a: administratorView.getViewPostmansButton().getActionListeners()){
                a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,null));
            }
        }else if(currentShowingType == User.Type.COORDINATOR){
            for(ActionListener a: administratorView.getViewPostmansButton().getActionListeners()){
                a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,null));
            }
        }
    };
    }

    public ActionListener addEntryButtonActionListener(){
        return actionEvent -> {
            String[][] tableData = {
                    {"name", ""},
                    {"username", ""},
                    {"password", ""}
            };
            String[] tableCol = {"Fields", "Data"};
            DefaultTableModel model = new DefaultTableModel(tableData, tableCol);
            final JTable mainTable = new JTable();
            mainTable.setModel(model);
            administratorView.setUpdateTable(mainTable);
            JScrollPane pane = administratorView.getUpdateTablePane();
            pane.setViewportView(mainTable);
            pane.revalidate();
            administratorView.getUpdateEntryButton().setVisible(true);
            administratorView.setUpdateEntryButtonText("Add");
        };
    }

    public void updateSecondaryTable(String name){
        User user = users.get(name);
        if(user!=null){
            String[][] tableData = {
                    {"name", user.getName()},
                    {"username", user.getUsername()},
                    {"password", user.getPassword()}
            };
            String[] tableCol = {"Fields", "Data"};
            DefaultTableModel model = new DefaultTableModel(tableData, tableCol);
            final JTable mainTable = new JTable();
            mainTable.setModel(model);
            JTable updateTable = administratorView.getUpdateTable();
            updateTable.setModel(model);
            administratorView.setUpdateTable(updateTable);
            JScrollPane tablePane = administratorView.getUpdateTablePane();
            tablePane.setViewportView(updateTable);
            tablePane.revalidate();
            administratorView.getUpdateEntryButton().setVisible(true);
            administratorView.setUpdateEntryButtonText("Update");
        }
    }

    public ActionListener deleteButtonActionListener(){
        return actionEvent -> {
            User user = users.get(selectedUserName);
            if(user != null) {
                authenticationController.deleteUser(user);
            }
        };
    }


    public AdministratorController(MainController mainController, AuthenticationController authenticationController){
        this.authenticationController = authenticationController;
        this.mainController = mainController;
        administratorView = new AdministratorView(this);
        this.users = authenticationController.getUsers();
        classifyUsers();
        administratorView.setVisible(true);
    }

    public WindowListener windowListener(){
        return new WindowListener(){

            @Override
            public void windowOpened(WindowEvent windowEvent) {

            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                mainController.serialize();
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {

            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {

            }
        };

    }
}
