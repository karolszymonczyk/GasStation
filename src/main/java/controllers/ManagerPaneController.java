package controllers;

import elements.ManagerBill;
import elements.ManagerSale;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;

//TODO tutaj i w sellerze nwm czy wgl potrzebne sa te wszystkie tvc i nie wystarcza tylko tabele tv

//TODO na odwrót niż w bazie jest bill i sale XD

//TODO zmienić amount da double wszędzie bo nie można spredawać benzynki

//TODO dodac jeszcze dodawanie i usuwanie sprzedaży

public class ManagerPaneController {

  public TableView tvSales;
  public TableColumn tvcId;
  public TableColumn tvcData;
  public TableColumn tvcSeller;
  public TableColumn tvcValue;
  public TableView tvBill;
  public TableColumn tvcProduct;
  public TableColumn tvcAmount;
  public TableColumn tvcPrice;

  private MainController controller;
  private LoginPaneController loginController;

  @FXML
  public void initialize() {
    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    tvcId.setCellValueFactory(new PropertyValueFactory<>("id"));
    tvcData.setCellValueFactory(new PropertyValueFactory<>("date"));
    tvcSeller.setCellValueFactory(new PropertyValueFactory<>("seller"));
    tvcValue.setCellValueFactory(new PropertyValueFactory<>("value"));
    tvcProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
    tvcAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    tvcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    addToSaleList();
  }

  private void addToSaleList() {

    //TODO tymczasowe do testów XD
    ArrayList<ManagerBill> elements1 = new ArrayList<>();
    ManagerBill e1 = new ManagerBill("Hot-Dog", 2, 12.98);
    ManagerBill e2 = new ManagerBill("Piwko", 4, 12.99);
    elements1.add(e1);
    elements1.add(e2);

    addSale(1, "2018-09-19 21:37:00", "Robert Siemaszko", 666.66, elements1);

    //TODO tymczasowe do testów XD
    ArrayList<ManagerBill> elements2 = new ArrayList<>();
    ManagerBill e3 = new ManagerBill("Dog-Hog", 1, 9.989);
    ManagerBill e4 = new ManagerBill("LPG", 5, 25.50);
    elements2.add(e3);
    elements2.add(e4);

    addSale(2,"2018-19-19 22:22:22", "Roksana Siema", 420.42, elements2);
  }

  public void addSale(int id, String date, String seller, double value, ArrayList<ManagerBill> elements) {
    ManagerSale managerSale = new ManagerSale(id, date, seller, value, elements);
    tvSales.getItems().add(managerSale);
  }

  public void setController(MainController controller) {
    this.controller = controller;
  }

  public void setLoginController(LoginPaneController LoginController) {
    this.loginController = LoginController;
  }

  public void bLogoutClick(ActionEvent event) {
    controller.setLoginPane();
  }

  public void bShowClick(ActionEvent event) {
    tvBill.getItems().clear();

    ManagerSale selectedItem = (ManagerSale) tvSales.getSelectionModel().getSelectedItem();
    if(selectedItem == null) {
      return;
    }
    ArrayList<ManagerBill> elements = selectedItem.getElements();
    for(ManagerBill element : elements) {
      tvBill.getItems().add(element);
    }
  }

  public void bWorkersClick(ActionEvent event) {
    setViewWorkersPane();
  }

  public void setViewWorkersPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/ViewWorkersPane.fxml"));
    AnchorPane viewWorkersPane = null;
    try {
      viewWorkersPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    ViewWorkersPaneController viewWorkersController = loader.getController();
    viewWorkersController.setController(controller);
    viewWorkersController.setLoginController(loginController);
    controller.setPane(viewWorkersPane);
  }

  public void bDeliversClick(ActionEvent event) {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/ViewDeliversPane.fxml"));
    AnchorPane viewDeliversPane = null;
    try {
      viewDeliversPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    ViewDeliversPaneController viewDeliversController = loader.getController();
    viewDeliversController.setController(controller);
    viewDeliversController.setLoginController(loginController);
    controller.setPane(viewDeliversPane);
  }

  public void bProductsClick(ActionEvent event) {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/ViewProductsPane.fxml"));
    AnchorPane viewProductsPane = null;
    try {
      viewProductsPane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    ViewProductsPaneController viewProductsController = loader.getController();
    viewProductsController.setController(controller);
    viewProductsController.setLoginController(loginController);
    controller.setPane(viewProductsPane);
  }

  public void bRefreshClick(ActionEvent event) {
    System.out.println("REFRESH");
  }

  public void bDeleteSaleClick(ActionEvent event) { //TODO można dodać ze jak nie ma selected item to nic nie robi
    Object selectedItem = tvSales.getSelectionModel().getSelectedItem();
    tvSales.getItems().remove(selectedItem);

    tvBill.getItems().clear();
  }

  public void bAddSaleClick(ActionEvent event) {
    setAddPane();
  }

  private void setAddPane() {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmlFiles/AddSalePane.fxml"));
    AnchorPane addSalePane = null;
    try {
      addSalePane = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    AddSalePaneController addSaleController = loader.getController();
    addSaleController.setController(controller);
    addSaleController.setLoginController(loginController);
    controller.setPane(addSalePane);
  }
}
