package View;

import Controller.PostmanViewController;

import javax.swing.*;
import java.awt.*;

public class PostmanView extends JFrame{
    private JLabel welcomeLabel;
    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JTable dataTable;
    private JButton viewParcelsButton;
    private JButton searchParcelByIDButton;
    private JTextField parcelIDTextField;
    private JScrollPane dataTablePane;
    private JPanel mapPane;


    protected JTabbedPane getMainTabbedPane(){
        return tabbedPane1;
    }

    public JScrollPane getDataTablePane(){
        return this.dataTablePane;
    }

    public JTable getDataTable(){
        return this.dataTable;
    }

    public void setDataTable(JTable dataTable){
        this.dataTable = dataTable;
    }

    public String getSearchParcelByIDText(){
        return this.parcelIDTextField.getText();
    }

    public void addContainerToMapPane(Object layout){
//        JScrollPane sp = new JScrollPane();
//        sp.setSize(500,500);
//        sp.add((Container)layout);

        try {
            this.mapPane.add((Container) layout);
            this.mapPane.revalidate();
        }catch (NullPointerException e){
            //apare un NPE for some reason aici, dar tot se afiseaza corect
        }
        //this.mapPane.revalidate();
    }

    public PostmanView(){

    }
    public PostmanView(PostmanViewController postmanViewController){
        this.setTitle("Postman View");
        this.setSize(1080,840);
        this.add(rootPanel);
        mapPane.addComponentListener(postmanViewController.mapPaneComponentAdapter());
        viewParcelsButton.addActionListener(postmanViewController.viewParcelsButtonActionListener());
        searchParcelByIDButton.addActionListener(postmanViewController.searchParcelByIDButtonActionListener());
        this.addWindowListener(postmanViewController.windowListener());
    }
}
